package dimitrijestefan.mosis.ehelp.FriendFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import dimitrijestefan.mosis.ehelp.Data.MyHelpRequests
import dimitrijestefan.mosis.ehelp.Models.FriendRequest
import dimitrijestefan.mosis.ehelp.Models.User
import dimitrijestefan.mosis.ehelp.R
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.*

class UserProfileFragment : Fragment() {

    private lateinit var usersRef: DatabaseReference
    private  lateinit var friendsRequestsRef:DatabaseReference
    private  lateinit var friendsRef:DatabaseReference
    private  var senderId:String=""
    private  var receiverId:String=""
    private lateinit var mAuth:FirebaseAuth
    private  lateinit var CurrentState:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth=FirebaseAuth.getInstance()
        receiverId=arguments?.getString("ReceiverId")?: ""
        senderId=mAuth.currentUser!!.uid
        usersRef=FirebaseDatabase.getInstance().getReference().child("Users")
        friendsRequestsRef=FirebaseDatabase.getInstance().getReference().child("FriendsRequests")
        friendsRef=FirebaseDatabase.getInstance().getReference().child("Friends")
        CurrentState="not_friends"
        usersRef.child(receiverId).addValueEventListener( object :ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(dataSnap: DataSnapshot) {
                if(dataSnap.exists())
                {
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
        val view=inflater.inflate(R.layout.fragment_user_profile, container, false)


        // Inflate the layout for this fragment
        Toast.makeText(this.requireContext(),arguments?.getString("url"),Toast.LENGTH_SHORT).show()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         btnCancelRequest.visibility=View.INVISIBLE
        btnCancelRequest.isEnabled=false
        ValidateRequest()
    }


    private  fun ValidateRequest(){
        if(!senderId.equals(receiverId)){
            btnSendRequest.setOnClickListener {
                btnSendRequest.isEnabled=false
                if(CurrentState.equals("not_friends")){
                    SendFriendRequest()
                }
                if(CurrentState.equals("request_sent")){
                    CancelFriendRequest()
                }
                if(CurrentState.equals("request_received")){
                    AcceptFriendRequest()
                }
                if(CurrentState.equals("friends")){
                    Unfriend()
                }
            }
        }
        else{
            btnCancelRequest.visibility=View.INVISIBLE
            btnSendRequest.visibility=View.INVISIBLE
        }

    }

    private  fun SendFriendRequest(){
        var email:String=mAuth.currentUser?.email?:""
        var frRequest:FriendRequest= FriendRequest(senderId,email,email,email,"received")

        friendsRequestsRef.child(senderId).child(receiverId)
            .child("request_type").setValue("sent")
            .addOnCompleteListener {task ->

                if(task.isSuccessful)
                {
                    friendsRequestsRef.child(receiverId).child(senderId)
                        //.child("request_type").setValue("received")
                        .setValue(frRequest)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful)
                            {
                                btnSendRequest.isEnabled=true
                                this.CurrentState="request_sent"
                                btnSendRequest.setText("Cancle friend request")
                            }
                        }
                }
            }
    }

    private  fun CancelFriendRequest(){
        friendsRequestsRef.child(senderId).child(receiverId)
            .removeValue()
            .addOnCompleteListener {task ->

                if(task.isSuccessful)
                {
                    friendsRequestsRef.child(receiverId).child(senderId)
                        .removeValue()
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful)
                            {
                                btnSendRequest.isEnabled=true
                                this.CurrentState="not_friends"
                                btnSendRequest.setText("Send friend request")
                            }
                        }
                }
            }

    }

    private fun AcceptFriendRequest(){

       friendsRef.child(senderId).child(receiverId).child("date").setValue(1).addOnCompleteListener { task ->
           if(task.isSuccessful){
               friendsRef.child(receiverId).child(senderId).child("date").setValue(1).addOnCompleteListener {task ->
                   if(task.isSuccessful)
                   {
                       friendsRequestsRef.child(senderId).child(receiverId)
                           .removeValue()
                           .addOnCompleteListener {task ->

                               if(task.isSuccessful)
                               {
                                   friendsRequestsRef.child(receiverId).child(senderId)
                                       .removeValue()
                                       .addOnCompleteListener { task ->
                                           if(task.isSuccessful)
                                           {
                                               btnSendRequest.isEnabled=false
                                               this.CurrentState="friends"
                                               btnSendRequest.setText("Send friend request")
                                           }
                                       }
                               }
                           }
                   }
               }
           }
       }
    }

    private fun Unfriend(){
        friendsRef.child(senderId).child(receiverId)
            .removeValue()
            .addOnCompleteListener {task ->

                if(task.isSuccessful)
                {
                    friendsRef.child(receiverId).child(senderId)
                        .removeValue()
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful)
                            {
                                btnSendRequest.isEnabled=true
                                this.CurrentState="not_friends"
                                btnSendRequest.setText("Send friend request")
                            }
                        }
                }
            }

    }



    private fun MaintenanceOfButtons(){
        friendsRequestsRef.child(senderId)
            .addListenerForSingleValueEvent( object : ValueEventListener{
                override fun onCancelled(dataSnap: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(dataSnap: DataSnapshot) {
                    if(dataSnap.hasChild(receiverId)){
                        var request_type:String = dataSnap.child(receiverId).child("request_type").getValue().toString()
                        if(request_type.equals("sent"))
                        {
                            CurrentState="request_sent"
                            btnSendRequest.setText("Cancel set request")
                        }
                        else if(request_type.equals("received")){

                            CurrentState="request_received"
                            btnSendRequest.setText("Accept friend request")
                            btnCancelRequest.isVisible=true
                            btnCancelRequest.isEnabled=true
                            btnCancelRequest.setOnClickListener {
                                CancelFriendRequest()
                            }
                        }
                    }
                    else
                    {
                        friendsRef.child(senderId).addListenerForSingleValueEvent(object :ValueEventListener{
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                            override fun onDataChange(dataSnap: DataSnapshot) {
                               if(dataSnap.hasChild(receiverId)){
                                   CurrentState="friends"
                                   btnSendRequest.setText("Unfriend user")
                                   btnCancelRequest.visibility=View.INVISIBLE
                                   btnCancelRequest.isEnabled=false
                               }
                            }

                        })

                    }
                }

            }

            )
    }





}