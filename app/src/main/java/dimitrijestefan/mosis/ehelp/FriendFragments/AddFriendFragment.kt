package dimitrijestefan.mosis.ehelp.FriendFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dimitrijestefan.mosis.ehelp.Adapters.OnAddImgClickListener
import dimitrijestefan.mosis.ehelp.Adapters.OnDeleteImgClickListener
import dimitrijestefan.mosis.ehelp.Adapters.RequestsViewHolder
import dimitrijestefan.mosis.ehelp.Data.UserData
import dimitrijestefan.mosis.ehelp.Models.Friend
import dimitrijestefan.mosis.ehelp.Models.FriendRequest
import dimitrijestefan.mosis.ehelp.Models.User
import dimitrijestefan.mosis.ehelp.R
import kotlinx.android.synthetic.main.fragment_add_friend.*

class AddFriendFragment : Fragment(), OnDeleteImgClickListener, OnAddImgClickListener {

    private lateinit var mFriendsRequestRecyclerView: RecyclerView
    private lateinit var friendsRequestsRef: DatabaseReference
    private lateinit var friendsRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var RequestsFirebaseRecyclerAdapter: FirebaseRecyclerAdapter<FriendRequest, RequestsViewHolder>
    private var currenUserId: String = ""
    private lateinit var currentUser: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        currenUserId = mAuth.currentUser!!.uid
        currentUser = UserData.returnCurrentUser()
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
                    holder.name.setText(model.userSender.name)
                    holder.lastname.setText(model.userSender.lastname)
                    holder.email.setText(model.userSender.email)
                    model.userSender.userId = getRef(position).key!!
                    holder.bind(model, this@AddFriendFragment, this@AddFriendFragment)
                }

            }
        mFriendsRequestRecyclerView.adapter = RequestsFirebaseRecyclerAdapter
        RequestsFirebaseRecyclerAdapter.startListening()

    }


    override fun onDeleteImgClick(userSender: Friend) {
        //  Toast.makeText(this.activity, userId, Toast.LENGTH_SHORT).show()
        DeleteFriendRequest(userSender)
    }

    override fun onAddImgClick(userSender: Friend) {
        // Toast.makeText(this.activity, userId, Toast.LENGTH_SHORT).show()
        ConfirmFriendRequest(userSender)
    }


    private fun DeleteFriendRequest(sender: Friend) {
        friendsRequestsRef.child(sender.userId).child(currentUser.key)
            .removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    friendsRequestsRef.child(currentUser.key).child(sender.userId)
                        .removeValue()

                }
            }
    }

    private fun ConfirmFriendRequest(receiverFriend: Friend) {
        var senderFriend: Friend = Friend(
            currentUser.name,
            currentUser.lastname,
            currentUser.email,
            currentUser.photoUrl,
            currentUser.username
        )
        senderFriend.userId = currentUser.key
        friendsRef.child(senderFriend.userId).child(receiverFriend.userId).setValue(receiverFriend)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    friendsRef.child(receiverFriend.userId).child(senderFriend.userId)
                        .setValue(senderFriend)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                friendsRequestsRef.child(senderFriend.userId)
                                    .child(receiverFriend.userId)
                                    .removeValue()
                                    .addOnCompleteListener { task ->

                                        if (task.isSuccessful) {
                                            friendsRequestsRef.child(receiverFriend.userId)
                                                .child(senderFriend.userId)
                                                .removeValue()
                                        }
                                    }
                            }
                        }
                }
            }
    }

}
