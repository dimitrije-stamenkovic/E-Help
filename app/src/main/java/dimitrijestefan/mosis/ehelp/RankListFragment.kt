package dimitrijestefan.mosis.ehelp


import android.app.VoiceInteractor
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.common.api.internal.ActivityLifecycleObserver.of
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dimitrijestefan.mosis.ehelp.Adapters.RankViewHolder
import dimitrijestefan.mosis.ehelp.Data.*
import dimitrijestefan.mosis.ehelp.Models.GeoPoint
import dimitrijestefan.mosis.ehelp.Models.User
import dimitrijestefan.mosis.ehelp.ViewModels.RankViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_rank_list.*
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class RankListFragment : Fragment() {

    private lateinit var mUsersRecyclerView:RecyclerView
    private lateinit var mUsersFirebaseRecyclerAdapter: FirebaseRecyclerAdapter<User, RankViewHolder>
    private lateinit var mUsersDatabaseRef:DatabaseReference
    private lateinit var mCurrentUser:User
    private val rankViewModel by lazy {
        ViewModelProvider(requireActivity()).get(RankViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUsersDatabaseRef=FirebaseDatabase.getInstance().getReference().child("Users")
        mCurrentUser=UserData.returnCurrentUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_rank_list, container, false)
        mUsersRecyclerView=view.findViewById(R.id.recyclerViewRank)
        mUsersRecyclerView.setHasFixedSize(true)
        var manager:LinearLayoutManager= LinearLayoutManager(requireContext())
        manager.reverseLayout=true
        manager.stackFromEnd=true
        mUsersRecyclerView.setLayoutManager(manager)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtUserPoints.setText(mCurrentUser.points.toString())
        rankViewModel.getUserRank(mCurrentUser.key,this.requireContext())
        setupObserver()
        loadRankList()
        Glide.with(this.requireContext())
            .load(mCurrentUser.photoUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageViewRank)

    }

    private fun loadRankList(){

        val firebaseSearchQuery=mUsersDatabaseRef.orderByChild("points").limitToLast(10)
        val options:FirebaseRecyclerOptions<User> =
        FirebaseRecyclerOptions.Builder<User>()
            .setQuery(firebaseSearchQuery,User::class.java)
            .build()
        mUsersFirebaseRecyclerAdapter=object :FirebaseRecyclerAdapter<User,RankViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
                val view:View= LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_rank_user,parent,false)
                return RankViewHolder(view);
            }

            override fun onBindViewHolder(holder: RankViewHolder, position: Int, model: User) {
                var rank=10-position
                var points:String= model.points?.toString()?:"0"
                holder.rankNumber.setText(rank.toString())
                holder.rankPoints.setText(points + " pts" )
                holder.rankUsername.setText(model.username)
            }


        }
        mUsersRecyclerView.adapter=mUsersFirebaseRecyclerAdapter
        mUsersFirebaseRecyclerAdapter.startListening()

    }

    fun setupObserver(){
        rankViewModel.userRank?.observe(viewLifecycleOwner, Observer {
            txtUserRank.setText(rankViewModel.userRank.value.toString())
        })
    }
}
