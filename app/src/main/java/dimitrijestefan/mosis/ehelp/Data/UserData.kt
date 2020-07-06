package dimitrijestefan.mosis.ehelp.Data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import dimitrijestefan.mosis.ehelp.Models.User

object UserData {

    private lateinit var currentUser:User
    lateinit var userId:String
    private set
    private val database:DatabaseReference=FirebaseDatabase.getInstance().getReference()
    private lateinit var usersRef:DatabaseReference
    private lateinit var singleValueListener:ValueEventListener
    private lateinit var mAuth: FirebaseAuth

    init{
        mAuth=FirebaseAuth.getInstance()
        userId = mAuth.currentUser?.uid!!
        usersRef = database.child("Users").child(userId)
        currentUser = User()
        singleValueListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(p0: DataSnapshot) {
                currentUser.email = p0.child("email").value.toString()
                currentUser.number = p0.child("number").value.toString()
                currentUser.username = p0.child("username").value.toString()
                currentUser.name = p0.child("name").value.toString()
                currentUser.lastname = p0.child("lastname").value.toString()
                currentUser.photoUrl = p0.child("photoUrl").value.toString()
                currentUser.points= p0.child("points").getValue(Int::class.java)!!
                currentUser.key = p0.key!!
            }

        }
        usersRef.addValueEventListener(singleValueListener)
    }

    fun returnCurrentUser()= currentUser

    fun changeUserReference(uidUser:String){
        userId=uidUser
        usersRef= database.child("Users").child(uidUser)
        usersRef.addValueEventListener(singleValueListener)
        currentUser= User()
    }

}





