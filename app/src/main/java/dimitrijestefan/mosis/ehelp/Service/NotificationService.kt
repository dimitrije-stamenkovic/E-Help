package dimitrijestefan.mosis.ehelp.Service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dimitrijestefan.mosis.ehelp.R

class NotificationService :FirebaseMessagingService() {

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        if(FirebaseAuth.getInstance().currentUser!=null)
            saveTokenToFirebase(newToken)
    }

    override fun onMessageReceived(remoteMess: RemoteMessage) {
        super.onMessageReceived(remoteMess)
        var builder = NotificationCompat.Builder(this,"101")
            .setSmallIcon(R.drawable.ic_action_map)
            .setContentTitle(remoteMess.notification?.title)
            .setContentText(remoteMess.notification?.body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        var notification:Notification=builder.build()
        var notimanager:NotificationManagerCompat= NotificationManagerCompat.from(applicationContext)
        notimanager.notify(112,notification)

    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel();
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("101", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }



    companion object{
        private lateinit var  currentUserRef:DatabaseReference
        private lateinit var  currentUser:FirebaseUser
        private lateinit var mAuth:FirebaseAuth

        init {
            mAuth=FirebaseAuth.getInstance()
            currentUser= mAuth.currentUser!!
            currentUserRef=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.uid)
        }

        fun saveTokenToFirebase(newToken: String?){
            currentUserRef.child("mToken").setValue(newToken)
        }

        fun deleteTokenToFirebase(){
            currentUserRef.child("mToken").removeValue()
        }

    }
}