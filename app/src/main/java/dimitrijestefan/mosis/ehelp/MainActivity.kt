package dimitrijestefan.mosis.ehelp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import dimitrijestefan.mosis.ehelp.Service.LocationService


import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import dimitrijestefan.mosis.ehelp.Data.FriendData
import dimitrijestefan.mosis.ehelp.Data.UserData
import dimitrijestefan.mosis.ehelp.Data.UsersLocationData


class MainActivity : AppCompatActivity() {

    private lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView:BottomNavigationView = findViewById(R.id.bottomnavbar)
        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(bottomNavigationView,navController)
        mAuth=FirebaseAuth.getInstance()
        var currentUserId=mAuth.currentUser?.uid!!
        startLocationService()

        Log.e("user fire",mAuth.currentUser?.email)

        if(UserData!=null&& UserData.userId!=currentUserId)
        UserData.changeUserReference(currentUserId)
        if(FriendData!=null&&FriendData.currentUserId!=currentUserId)
        FriendData.changeUserReference(currentUserId)
        if(UsersLocationData!=null&&UsersLocationData.currentUserId!=currentUserId)
        UsersLocationData.changeUserReference(currentUserId)
//        createNotificationChannel();
    }



     fun startLocationService() {
        if (!isLocationServiceRunning()) {
            val serviceIntent = Intent(this, LocationService::class.java)
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                startForegroundService(serviceIntent) // Da bi ostao da radi, da se ne bi zaustavio
            } else {
                startService(serviceIntent)
            }

        }
    }


//    private fun createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = getString(R.string.channel_name)
//            val descriptionText = getString(R.string.channel_description)
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val channel = NotificationChannel("101", name, importance).apply {
//                description = descriptionText
//            }
//            // Register the channel with the system
//            val notificationManager: NotificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }


    @Suppress("DEPRECATION")
     fun isLocationServiceRunning(): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.dimitrijestefan.mosis.ehelp.Service.LocationService" == service.service.className) {
                Log.d(
                    "SERVIS",
                    "isLocationServiceRunning: location service is already running."
                )
                return true
            }
        }
        Log.d("SERVIS", "isLocationServiceRunning: location service is not running.")
        return false
    }

//    override fun onBackPressed() {
//        var count:Int= supportFragmentManager.backStackEntryCount
//        if(count==0) {
//            super.onBackPressed()
//            //supportFragmentManager.popBackStackImmediate()
//        }else{
//            supportFragmentManager.popBackStackImmediate()
//        }
//    }


}
