package dimitrijestefan.mosis.ehelp.Service


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import dimitrijestefan.mosis.ehelp.Models.GeoPoint

class LocationService : Service()  {
    private var mLocationManager: LocationManager? = null

    var mLocationListeners = arrayOf(
        LocationListener(LocationManager.GPS_PROVIDER),
        LocationListener(LocationManager.NETWORK_PROVIDER)
    )


    class LocationListener(provider: String) : android.location.LocationListener {

        private var mCurrentUser: FirebaseUser?
        val current_uid: String
        private var database: DatabaseReference


        init {
            Log.e(TAG, "LocationListener $provider")

            mCurrentUser = FirebaseAuth.getInstance().currentUser
            current_uid = mCurrentUser!!.uid
            database = FirebaseDatabase.getInstance().getReference().child("UsersLocations")
                .child(current_uid)


        }

        override fun onLocationChanged(location: Location) {
        //    Log.e(TAG, "onLocationChanged: $location")
            var geo_point = GeoPoint(location.latitude, location.longitude)
            try {
                database.setValue(geo_point)
                    .addOnCompleteListener(OnCompleteListener() {
                      //  if (it.isSuccessful)
                       //     Log.d(TAG, "onComplete: inserted user location into database")
                    })
            } catch (e: NullPointerException) {
                Log.e(TAG, e.message)
            }

        }

        override fun onProviderDisabled(provider: String) {
            Log.e(TAG, "onProviderDisabled: $provider")
        }

        override fun onProviderEnabled(provider: String) {
            Log.e(TAG, "onProviderEnabled: $provider")
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Log.e(TAG, "onStatusChanged: $provider")
        }
    }

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand")
        super.onStartCommand(intent, flags, startId)
        return Service.START_STICKY
    }

    override fun onCreate() {
        Log.e(TAG, "onCreate")

        var CHANNEL_ID = "0"
        var channel: NotificationChannel = NotificationChannel(
            CHANNEL_ID, "Location Service",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val notification =
            NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("").setContentText("")
                .build()
        startForeground(1, notification)
        initializeLocationManager()

        try {
            mLocationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL.toLong(), LOCATION_DISTANCE,
                mLocationListeners[1]
            )
        } catch (ex: java.lang.SecurityException) {
            Log.i(TAG, "fail to request location update, ignore", ex)
        } catch (ex: IllegalArgumentException) {
            Log.d(TAG, "network provider does not exist, " + ex.message)
        }

        try {
            mLocationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, LOCATION_INTERVAL.toLong(), LOCATION_DISTANCE,
                mLocationListeners[0]
            )
        } catch (ex: java.lang.SecurityException) {
            Log.i(TAG, "fail to request location update, ignore", ex)
        } catch (ex: IllegalArgumentException) {
            Log.d(TAG, "gps provider does not exist " + ex.message)
        }

    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy")
        super.onDestroy()
        if (mLocationManager != null) {
            for (i in mLocationListeners.indices) {
                try {
                    mLocationManager!!.removeUpdates(mLocationListeners[i])
                } catch (ex: Exception) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex)
                }

            }
        }
    }

    private fun initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager")
        if (mLocationManager == null) {
            mLocationManager =
                applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
    }

    companion object {
        private val TAG = "BOOMBOOMTESTGPS"
        private val LOCATION_INTERVAL = 1000
        private val LOCATION_DISTANCE = 0f
    }
}