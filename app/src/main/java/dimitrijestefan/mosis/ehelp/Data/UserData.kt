package dimitrijestefan.mosis.ehelp.Data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import dimitrijestefan.mosis.ehelp.Models.User
import dimitrijestefan.mosis.ehelp.Models.UserLocation

object UserData {
    private val usersLocationList: ArrayList<UserLocation>
    private val usersList: ArrayList<User>
    private var usersIndexMapping = HashMap<String, Int>()
    private var mCurrentUser: FirebaseUser?
    val current_uid: String
    private var database: DatabaseReference
    var currentUserProfile = User()


    init {
        mCurrentUser = FirebaseAuth.getInstance().currentUser
        current_uid = mCurrentUser!!.uid
        database = FirebaseDatabase.getInstance().getReference()
        usersList = ArrayList()
        usersLocationList = ArrayList()


    }


    fun recreateKeyIndexMapping() {
        usersIndexMapping.clear()
//        for ( i in 0..(requests.size)){
//            myHelpRequestsIndexMapping.put(requests.get(i).key,i)
//        }

        for (user in usersList.indices) {
            usersIndexMapping.put(usersList.get(user).key, user)
        }
    }


    fun fetchCurrenUser() {

        var current_user_ref = database.child("Users").child(current_uid)


        current_user_ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {


                currentUserProfile.email = p0.child("email").value.toString()
                currentUserProfile.number = p0.child("number").value.toString()
                currentUserProfile.username = p0.child("username").value.toString()
                currentUserProfile.name = p0.child("name").value.toString()
                currentUserProfile.lastname = p0.child("lastname").value.toString()
                currentUserProfile.photoUrl = p0.child("photoUrl").value.toString()
                currentUserProfile.key = p0.child("key").value.toString()

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }

    fun getCurrentUser() = currentUserProfile



}