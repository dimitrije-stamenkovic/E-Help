package dimitrijestefan.mosis.ehelp.FriendFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import dimitrijestefan.mosis.ehelp.Data.UserData
import dimitrijestefan.mosis.ehelp.Models.Friend
import dimitrijestefan.mosis.ehelp.Models.FriendRequest
import dimitrijestefan.mosis.ehelp.Models.User
import dimitrijestefan.mosis.ehelp.R
import kotlinx.android.synthetic.main.fragment_user_profile.*

class UserProfileFragment : Fragment() {

    private lateinit var usersRef: DatabaseReference
    private lateinit var friendsRequestsRef: DatabaseReference
    private lateinit var friendsRef: DatabaseReference
   // private var senderUser.key: String = ""
  //  private var receiverUser.key: String = ""
 //   private lateinit var mAuth: FirebaseAuth
    private lateinit var CurrentState: String
    private lateinit var receiverUser:User
    private lateinit var senderUser:User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  mAuth = FirebaseAuth.getInstance()
        var bundle: Bundle? =arguments
        receiverUser=bundle?.getSerializable("ReceiverUser") as User
       // receiverUser.key=receiverUser.key
        senderUser=UserData.returnCurrentUser()
       // senderUser.key = mAuth.currentUser!!.uid
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users")
        friendsRequestsRef = FirebaseDatabase.getInstance().getReference().child("FriendsRequests")
        friendsRef = FirebaseDatabase.getInstance().getReference().child("Friends")
        CurrentState = "not_friends"
        usersRef.child(receiverUser.key).addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnap: DataSnapshot) {
                if (dataSnap.exists()) {
                    nameAddUser.setText(dataSnap.child("name").getValue().toString())
                    emailAddUser.setText(dataSnap.child("email").getValue().toString())
                    lastnameAddUser.setText(dataSnap.child("lastname").getValue().toString())
                    MaintenanceOfButtons()
                }
            }
        }
        )


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnCancelRequest.visibility = View.INVISIBLE
        btnCancelRequest.isEnabled = false
        btnSendRequest.setBackgroundResource(R.drawable.confirm_btn)
        ValidateRequest()
    }


    private fun ValidateRequest() {
        if (!senderUser.key.equals(receiverUser.key)) {
            btnSendRequest.setOnClickListener {
                btnSendRequest.isEnabled = false
                if (CurrentState.equals("not_friends")) {

                    SendFriendRequest()
                }
                if (CurrentState.equals("request_sent")) {
                    CancelFriendRequest()
                }
                if (CurrentState.equals("request_received")) {
                    AcceptFriendRequest()
                }
                if (CurrentState.equals("friends")) {
                    Unfriend()
                }
            }
        } else {
            btnCancelRequest.visibility = View.INVISIBLE
            btnSendRequest.visibility = View.INVISIBLE
        }

    }

    private fun SendFriendRequest() {
      //  var email: String = mAuth.currentUser?.email ?: ""
      //  var frRequest: FriendRequest = FriendRequest(senderUser.key, email, email, email, "received")
      //  var frRequest:FriendRequest= FriendRequest(senderUser.key,senderUser.name, senderUser.lastname,senderUser.email,"received")
        var friendSender:Friend=Friend(senderUser.name,senderUser.lastname,senderUser.email,senderUser.photoUrl,senderUser.username)
        var frRequest:FriendRequest= FriendRequest(friendSender,"received")
        friendsRequestsRef.child(senderUser.key).child(receiverUser.key)
            .child("request_type").setValue("sent")
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    friendsRequestsRef.child(receiverUser.key).child(senderUser.key)
                        //.child("request_type").setValue("received")
                        .setValue(frRequest)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                this.CurrentState = "request_sent"
                                btnSendRequest.isEnabled = true
                                btnSendRequest.setBackgroundResource(R.drawable.btn_red_cancel)
                                btnSendRequest.setText("Cancel friend request")
                            }
                        }
                }
            }
    }

    private fun CancelFriendRequest() {
        friendsRequestsRef.child(senderUser.key).child(receiverUser.key)
            .removeValue()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    friendsRequestsRef.child(receiverUser.key).child(senderUser.key)
                        .removeValue()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                btnSendRequest.isEnabled = true
                                this.CurrentState = "not_friends"
                                btnCancelRequest.visibility = View.INVISIBLE
                                btnCancelRequest.isEnabled = false
                                btnSendRequest.setBackgroundResource(R.drawable.confirm_btn)
                                btnSendRequest.setText("Send friend request")
                            }
                        }
                }
            }

    }

    private fun AcceptFriendRequest() {
        var friendSender:Friend= Friend(senderUser.name,senderUser.lastname,senderUser.email,senderUser.photoUrl,senderUser.username)
        var friendReceiver:Friend= Friend(receiverUser.name,receiverUser.lastname,receiverUser.email,receiverUser.photoUrl,receiverUser.username)

        friendsRef.child(senderUser.key).child(receiverUser.key).setValue(friendReceiver)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    friendsRef.child(receiverUser.key).child(senderUser.key).setValue(friendSender)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                friendsRequestsRef.child(senderUser.key).child(receiverUser.key)
                                    .removeValue()
                                    .addOnCompleteListener { task ->

                                        if (task.isSuccessful) {
                                            friendsRequestsRef.child(receiverUser.key).child(senderUser.key)
                                                .removeValue()
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        btnSendRequest.isEnabled = true
                                                        btnCancelRequest.isEnabled = false
                                                        btnCancelRequest.visibility = View.INVISIBLE
                                                        this.CurrentState = "friends"
                                                        btnSendRequest.setBackgroundResource(R.drawable.btn_red_cancel)
                                                        btnSendRequest.setText("Unfriend")
                                                    }
                                                }
                                        }
                                    }
                            }
                        }
                }
            }
    }

    private fun Unfriend() {
        friendsRef.child(senderUser.key).child(receiverUser.key)
            .removeValue()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    friendsRef.child(receiverUser.key).child(senderUser.key)
                        .removeValue()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                btnSendRequest.isEnabled = true
                                this.CurrentState = "not_friends"
                                btnSendRequest.setBackgroundResource(R.drawable.confirm_btn)
                                btnSendRequest.setText("Send friend request")
                            }
                        }
                }
            }

    }


    private fun MaintenanceOfButtons() {
        friendsRequestsRef.child(senderUser.key)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(dataSnap: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(dataSnap: DataSnapshot) {
                    if (dataSnap.hasChild(receiverUser.key)) {
                        var request_type: String =
                            dataSnap.child(receiverUser.key).child("request_type").getValue().toString()
                        if (request_type.equals("sent")) {
                            CurrentState = "request_sent"
                            btnSendRequest.setText("Cancel friend request")
                            btnSendRequest.setBackgroundResource(R.drawable.btn_red_cancel)
                        } else if (request_type.equals("received")) {

                            CurrentState = "request_received"
                            btnSendRequest.setText("Accept friend request")
                            btnCancelRequest.setBackgroundResource(R.drawable.btn_red_cancel)
                            btnCancelRequest.isVisible = true
                            btnCancelRequest.isEnabled = true
                            btnCancelRequest.setOnClickListener {
                                CancelFriendRequest()
                            }
                        }
                    } else {
                        friendsRef.child(senderUser.key)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                                override fun onDataChange(dataSnap: DataSnapshot) {
                                    if (dataSnap.hasChild(receiverUser.key)) {
                                        CurrentState = "friends"
                                        btnSendRequest.setText("Unfriend")
                                        btnSendRequest.setBackgroundResource(R.drawable.btn_red_cancel)
                                        btnCancelRequest.visibility = View.INVISIBLE
                                        btnCancelRequest.isEnabled = false
                                    }
                                }

                            })

                    }
                }

            }

            )
    }


}