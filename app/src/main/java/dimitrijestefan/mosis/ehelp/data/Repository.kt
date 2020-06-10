package dimitrijestefan.mosis.ehelp.data


import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dimitrijestefan.mosis.ehelp.Models.HelpRequest


object Repository {
    private val help_requests = ArrayList<HelpRequest>()
    private val FIREBASE_CHILD : String = "HelpRequests"

    private lateinit var database :DatabaseReference
    private lateinit var auth:FirebaseAuth




    fun currentUserId():String{
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        return user!!.uid
    }


    fun getHelpRequestList(): ArrayList<HelpRequest> = help_requests

    fun getHelpRequest(id:Int): HelpRequest = help_requests.get(id)

    fun addHelpRequest(title:String,urgency:String,category:String,latitude:String,longitude:String,about:String){

        val userId = currentUserId()
        database = FirebaseDatabase.getInstance().getReference()

        val key = database.child(FIREBASE_CHILD).push().key


        var req = HelpRequest(userId,title,urgency,category,about,latitude,longitude)


        if (key == null ){
            Log.w("error","Couldn't get push key for posts")
            return
        }

        help_requests.add(req)

        val reqValues = req.toMap()

        database.child("Users").child(userId).child("HelpRequests").child(key).setValue(reqValues)
        database.child("HelpRequest").child(key).setValue(reqValues)

    }

    fun deleteHelpRequest(id:Int) = help_requests.removeAt(id)

}