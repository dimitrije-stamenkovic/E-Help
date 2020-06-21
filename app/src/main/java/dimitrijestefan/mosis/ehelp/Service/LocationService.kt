package dimitrijestefan.mosis.ehelp.Service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dimitrijestefan.mosis.ehelp.Models.GeoPoint
import java.lang.NullPointerException

class LocationService: Service(){
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    val TAG = "LocationService"

    private lateinit var mFusedLocationClient : FusedLocationProviderClient
    private val UPDATE_INTERVAL : Long = 4*1000 // 4 secs
    private val FASTEST_INTERVAL : Long = 2000 // 2 secs

    private var mCurrentUser: FirebaseUser?
    val current_uid: String
    private  var database : DatabaseReference



    init {
    mCurrentUser = FirebaseAuth.getInstance().currentUser
    current_uid = mCurrentUser!!.uid
    database = FirebaseDatabase.getInstance().getReference().child("UsersLocations").child(current_uid)
    }

    override fun onCreate() {
        super.onCreate()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        if(Build.VERSION.SDK_INT>26){
            var CHANNEL_ID = "my_channel_1"
            var channel : NotificationChannel = NotificationChannel(CHANNEL_ID,"My Channel",NotificationManager.IMPORTANCE_DEFAULT)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(this,CHANNEL_ID).setContentTitle("").setContentText("").build()
            startForeground(1,notification)

        }
    }

    override fun onStartCommand(intent: Intent, flags:Int, startId:Int):Int{
        Log.d(TAG,"onStartCommand: called.")
        getLocation()
        return START_NOT_STICKY
    }

    private fun getLocation() {
        val mLocationRequestHighAccuracy = LocationRequest()
        mLocationRequestHighAccuracy.fastestInterval = FASTEST_INTERVAL
        mLocationRequestHighAccuracy.interval = UPDATE_INTERVAL
        mLocationRequestHighAccuracy.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        var LocationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {

                Log.d(TAG,"onLocationResult: got location result.")

                var location = p0?.lastLocation


                var userCords = GeoPoint(location!!.latitude, location!!.longitude)

                saveUserLocation(userCords)



                super.onLocationResult(p0)
            }

            override fun onLocationAvailability(p0: LocationAvailability?) {
                super.onLocationAvailability(p0)
            }
        }

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            Log.d(TAG,"getLocation: stoppoing the location service")
            stopSelf()
            return
        }

        Log.d(TAG,"getLocation: getting location information")
        mFusedLocationClient.requestLocationUpdates(mLocationRequestHighAccuracy,LocationCallback, Looper.myLooper())


    }

    private fun saveUserLocation (userCords:GeoPoint){
        try{
            database.setValue(userCords)
                .addOnCompleteListener(OnCompleteListener(){
                    if(it.isSuccessful)
                        Log.d(TAG,"onComplete: inserted user location into database")
                })
        }catch (e : NullPointerException){
            Log.e(TAG,e.message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this,"Location tracking is disabled",Toast.LENGTH_SHORT).show()
    }


}

