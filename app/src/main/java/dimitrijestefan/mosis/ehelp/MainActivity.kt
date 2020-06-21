package dimitrijestefan.mosis.ehelp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import dimitrijestefan.mosis.ehelp.Service.LocationService


import kotlinx.android.synthetic.main.activity_main.*
import android.app.ActivityManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import dimitrijestefan.mosis.ehelp.Data.UserData


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView:BottomNavigationView = findViewById(R.id.bottomnavbar)
        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(bottomNavigationView,navController)

        startLocationService()


    }

    private fun startLocationService() {
        if (!isLocationServiceRunning()) {
            val serviceIntent = Intent(this, LocationService::class.java)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                this@MainActivity.startForegroundService(serviceIntent) // Da bi ostao da radi, da se ne bi zaustavio
            } else {
                startService(serviceIntent)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun isLocationServiceRunning(): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("dimitrijestefan.mosis.ehelp.LocationService" == service.service.className) {
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
