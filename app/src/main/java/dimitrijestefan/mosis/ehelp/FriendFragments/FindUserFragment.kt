package dimitrijestefan.mosis.ehelp.FriendFragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dimitrijestefan.mosis.ehelp.Adapters.OnUserClickListener
import dimitrijestefan.mosis.ehelp.Adapters.UsersViewHolder
import dimitrijestefan.mosis.ehelp.Models.User
import dimitrijestefan.mosis.ehelp.R

import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions


class FindUserFragment : Fragment(), OnUserClickListener {

    private lateinit var mFindUserRecyclerView: RecyclerView
    private lateinit var mSearchUser: EditText
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var FirebaseRecyclerAdapter: FirebaseRecyclerAdapter<User, UsersViewHolder>
    private var currenUserId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        currenUserId = mAuth.currentUser!!.uid
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_find_user, container, false)
        mSearchUser = view.findViewById(R.id.editSearchUser)
        mFindUserRecyclerView = view.findViewById(R.id.recyclerViewFindUsers)
        mFindUserRecyclerView.setHasFixedSize(true)
        mFindUserRecyclerView.setLayoutManager(LinearLayoutManager(requireContext()))
        //ova linija ce vidimo
        mFindUserRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSearchUser.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            )
            {
                val searchText = mSearchUser.getText().toString().trim()
                loadUsersFirebaseData(searchText)
            }
        })

    }


    private fun loadUsersFirebaseData(searchText: String) {
        if (searchText.isEmpty()) {
            if(this::FirebaseRecyclerAdapter.isInitialized) {

           mFindUserRecyclerView.adapter = FirebaseRecyclerAdapter

           FirebaseRecyclerAdapter.stopListening()
            }

        } else {
            val firebaseSearchQuery =
                mDatabaseRef.orderByChild("email").startAt(searchText).endAt(searchText + "\uf8ff")
            val options: FirebaseRecyclerOptions<User> = FirebaseRecyclerOptions.Builder<User>()
                .setQuery(firebaseSearchQuery, User::class.java)
                .build()

            FirebaseRecyclerAdapter =
                object : FirebaseRecyclerAdapter<User, UsersViewHolder>(options) {
                    override fun onCreateViewHolder(
                        parent: ViewGroup,
                        viewType: Int
                    ): UsersViewHolder {
                        val view: View = LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_find_friend, parent, false)
                        return UsersViewHolder(view);
                    }

                    override fun onBindViewHolder(
                        holder: UsersViewHolder,
                        position: Int,
                        model: User
                    ) {
                        holder.name.setText(model.name)
                        holder.lastname.setText(model.lastname)
                        holder.email.setText(model.email)
                        model.key=getRef(position).key!!
                        holder.bind(model, this@FindUserFragment)
                    }

                }
            mFindUserRecyclerView.adapter = FirebaseRecyclerAdapter
            FirebaseRecyclerAdapter.startListening()

        }
    }


    override fun onUserClick(user: User) {
       // var bundle = bundleOf("ReceiverUser" to user)
        var bundle:Bundle= Bundle()
        bundle.putSerializable("ReceiverUser",user)
        this.findNavController().navigate(R.id.userProfileFragment, bundle)
    }


}