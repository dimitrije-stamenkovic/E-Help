package dimitrijestefan.mosis.ehelp
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dimitrijestefan.mosis.ehelp.Adapters.RecycleViewAdapterAddFriends
import dimitrijestefan.mosis.ehelp.AddObjectFragment.Companion.newInstance
import dimitrijestefan.mosis.ehelp.FriendFragments.UserProfileFragment
import dimitrijestefan.mosis.ehelp.Models.User
import kotlinx.android.synthetic.main.item_find_friend.view.*


class AddFriendFragment : Fragment(),RecycleViewAdapterAddFriends.OnItemClickListener,RecycleViewAdapterAddFriends.OnImageClickListener {

    private lateinit var mFindUserRecyclerView:RecyclerView
    private lateinit var mFriendsRequestRecyclerView:RecyclerView
    private lateinit var mSearchUser:EditText
    private lateinit var mDatabaseRef:DatabaseReference
    private lateinit var FirebaseRecyclerAdapter : FirebaseRecyclerAdapter<User, UsersViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            val view= inflater.inflate(R.layout.fragment_add_friend, container, false)

            mSearchUser=view.findViewById(R.id.editSearchUser)
            mFindUserRecyclerView=view.findViewById(R.id.recyclerViewFindUsers)
            mFriendsRequestRecyclerView=view.findViewById(R.id.recyclerViewFriendsRequest)
            mDatabaseRef=FirebaseDatabase.getInstance().getReference("Users")
            mFindUserRecyclerView.setHasFixedSize(true)
           mFindUserRecyclerView.setLayoutManager(LinearLayoutManager(requireContext()))
        mFindUserRecyclerView.layoutManager= LinearLayoutManager(requireContext(),
            LinearLayout.VERTICAL, false)
            mFriendsRequestRecyclerView.setLayoutManager(LinearLayoutManager(requireContext()))

            return  view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFriendsRequestRecyclerView.layoutManager= LinearLayoutManager(requireContext(),
            LinearLayout.VERTICAL, false)
        var users= ArrayList<User>()
        users.add(User("Maname","Maname","Maname","Maname","Maname","Maname","Maname"))
        users.add(User("Maname1","Maname","Maname","Maname","Maname","Maname","Maname"))
        users.add(User("Maname1","Maname","Maname","Maname","Maname","Maname","Maname"))
        users.add(User("Maname1","Maname","Maname","Maname","Maname","Maname","Maname"))
        users.add(User("Maname1","Maname","Maname","Maname","Maname","Maname","Maname"))
        users.add(User("Maname1","Maname","Maname","Maname","Maname","Maname","Maname"))
    var adapter= RecycleViewAdapterAddFriends(users, this, this)
        mFriendsRequestRecyclerView.setAdapter(adapter)
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
            ) {
                val searchText = mSearchUser.getText().toString().trim()
                loadFirebaseData(searchText)
            }
        } )


    }

    private  fun loadFirebaseData(searchText:String){
        if(searchText.isEmpty()){


            mFindUserRecyclerView.adapter = FirebaseRecyclerAdapter
            FirebaseRecyclerAdapter.stopListening()

        }else {
            val firebaseSearchQuery = mDatabaseRef.orderByChild("email").startAt(searchText).endAt(searchText + "\uf8ff")
            val options: FirebaseRecyclerOptions<User> = FirebaseRecyclerOptions.Builder<User>()
                .setQuery(firebaseSearchQuery, User::class.java)
                .build()
            FirebaseRecyclerAdapter =object :FirebaseRecyclerAdapter<User,UsersViewHolder>(options){
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
                    val view:View=LayoutInflater.from(parent.context).inflate(R.layout.item_find_friend,parent,false)
                    return  UsersViewHolder(view);
                }

                override fun onBindViewHolder(holder: UsersViewHolder, position: Int, model: User) {
                    holder.mview.userNameF.setText(model.email)
                    holder.mview.userLastnameF.setText(model.name)
                    holder.mview.userameF.setText(model.username)
                    holder.bind(model,this@AddFriendFragment,getRef(position).key)

                }

            }
            mFindUserRecyclerView.adapter = FirebaseRecyclerAdapter
            FirebaseRecyclerAdapter.startListening()

        }
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }

    override fun onItemClick(position: String?) {

       // Toast.makeText(this.activity, position, Toast.LENGTH_SHORT).show()
//        val userFrag= UserProfileFragment()
//        val manager=requireActivity().supportFragmentManager
//        val transaction= manager.beginTransaction()
//        transaction.add(R.id.prob1,userFrag)
//        transaction.addToBackStack("AddFriendFragment")
//        transaction.commit()
        var bundle= bundleOf("ReceiverId" to position)

        this.findNavController().navigate(R.id.userProfileFragment,bundle)
//       requireActivity().supportFragmentManager
//           .beginTransaction()
//           .replace(R.id.nav_host_fragment_container,UserProfileFragment())
//           .commit()


    }

    override fun onImageClick(position: Int) {
        Toast.makeText(this.activity, "kliknuta slika "+position.toString(), Toast.LENGTH_LONG).show()
    }


    public class UsersViewHolder(var mview : View) : RecyclerView.ViewHolder(mview) {
        var title: TextView = mview.findViewById(dimitrijestefan.mosis.ehelp.R.id.userNameF)
        var title2: TextView =mview.findViewById(dimitrijestefan.mosis.ehelp.R.id.userNameF)
        fun bind(user:User, clickListener: RecycleViewAdapterAddFriends.OnItemClickListener, position:String?){
            itemView.setOnClickListener {
                clickListener.onItemClick(position)
            }
        }

    }

}
