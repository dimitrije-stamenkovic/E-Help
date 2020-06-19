package dimitrijestefan.mosis.ehelp.FriendFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dimitrijestefan.mosis.ehelp.Adapters.*
import dimitrijestefan.mosis.ehelp.Models.FriendRequest
import dimitrijestefan.mosis.ehelp.R
import kotlinx.android.synthetic.main.fragment_add_friend.*
import kotlinx.android.synthetic.main.fragment_user_profile.*

class AddFriendFragment : Fragment(), OnDeleteImgClickListener, OnAddImgClickListener {

    private lateinit var mFriendsRequestRecyclerView: RecyclerView
    private lateinit var friendsRequestsRef: DatabaseReference
    private lateinit var friendsRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var RequestsFirebaseRecyclerAdapter: FirebaseRecyclerAdapter<FriendRequest, RequestsViewHolder>
    private var currenUserId: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        currenUserId = mAuth.currentUser!!.uid
        friendsRequestsRef = FirebaseDatabase.getInstance().getReference().child("FriendsRequests")
        friendsRef = FirebaseDatabase.getInstance().getReference().child("Friends")

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_friend, container, false)
        mFriendsRequestRecyclerView = view.findViewById(R.id.recyclerViewFriendsRequest)
        mFriendsRequestRecyclerView.setHasFixedSize(true)
        mFriendsRequestRecyclerView.setLayoutManager(LinearLayoutManager(requireContext()))
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnFindUsers.setOnClickListener {
            this.findNavController().navigate(R.id.findUserFragment)
        }
        loadFriendsRequests()
    }

    private fun loadFriendsRequests() {

        //val firebaseSearchQuery = mDatabaseRef.orderByChild("email").startAt(searchText).endAt(searchText + "\uf8ff")
        val firebaseSearchQuery =
            friendsRequestsRef.child(currenUserId).orderByChild("request_type").equalTo("received")
        val options: FirebaseRecyclerOptions<FriendRequest> =
            FirebaseRecyclerOptions.Builder<FriendRequest>()
                .setQuery(firebaseSearchQuery, FriendRequest::class.java)
                .build()
        RequestsFirebaseRecyclerAdapter =
            object : FirebaseRecyclerAdapter<FriendRequest, RequestsViewHolder>(options) {

                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): RequestsViewHolder {
                    val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_friend_request, parent, false)
                    return RequestsViewHolder(view)
                }

                override fun onBindViewHolder(
                    holder: RequestsViewHolder,
                    position: Int,
                    model: FriendRequest
                ) {
                    holder.name.setText(model.emailSender)
                    holder.lastname.setText(model.emailSender)
                    holder.email.setText(model.emailSender)
                    holder.bind(model, this@AddFriendFragment, this@AddFriendFragment)
                }

            }
        mFriendsRequestRecyclerView.adapter = RequestsFirebaseRecyclerAdapter
        RequestsFirebaseRecyclerAdapter.startListening()

    }


    override fun onDeleteImgClick(userId: String?) {
      //  Toast.makeText(this.activity, userId, Toast.LENGTH_SHORT).show()
        DeleteFriendRequest(userId?:"",currenUserId)
    }

    override fun onAddImgClick(userId: String?) {
       // Toast.makeText(this.activity, userId, Toast.LENGTH_SHORT).show()
        ConfirmFriendRequest(currenUserId,userId?:"")
    }


    private fun DeleteFriendRequest(senderId:String,receiverId:String) {
        friendsRequestsRef.child(senderId).child(receiverId)
            .removeValue()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    friendsRequestsRef.child(receiverId).child(senderId)
                        .removeValue()

                }
            }
    }

    private fun ConfirmFriendRequest(senderId: String,receiverId: String) {
        friendsRef.child(senderId).child(receiverId).child("date").setValue(1)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    friendsRef.child(receiverId).child(senderId).child("date").setValue(1)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                friendsRequestsRef.child(senderId).child(receiverId)
                                    .removeValue()
                                    .addOnCompleteListener { task ->

                                        if (task.isSuccessful) {
                                            friendsRequestsRef.child(receiverId).child(senderId)
                                                .removeValue()
                                        }
                                    }
                            }
                        }
                }
            }
    }

}
