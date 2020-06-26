package dimitrijestefan.mosis.ehelp


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.Gravity.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.firebase.auth.FirebaseAuth
import dimitrijestefan.mosis.ehelp.Activity.SignInActivity
import dimitrijestefan.mosis.ehelp.Data.UserData
import dimitrijestefan.mosis.ehelp.Models.User
import dimitrijestefan.mosis.ehelp.Service.LocationService
import dimitrijestefan.mosis.ehelp.ViewModels.RankViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_rank_list.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    private lateinit var mDrawerToggle:ActionBarDrawerToggle
    private lateinit var mCurrentUser:User
    private val rankViewModel by lazy {
        ViewModelProvider(requireActivity()).get(RankViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCurrentUser=UserData.returnCurrentUser()
    }


    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_profile, container, false)
        val toolbar:Toolbar=view.findViewById<Toolbar>(R.id.rightMenuToolbar)
        val drawerLayout= view.findViewById<DrawerLayout>(R.id.drawer_layout)
        toolbar.inflateMenu(R.menu.profile_right_menu)
        toolbar.setOnMenuItemClickListener {
            if(it.itemId==R.id.show_settings) {
                drawerLayout.openDrawer(Gravity.END)
            }
             true
        }

        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDrawerToggle=ActionBarDrawerToggle(this.requireActivity(),drawer_layout,rightMenuToolbar,R.string.open, R.string.close)
        mDrawerToggle.isDrawerIndicatorEnabled = false

        switch1.setOnCheckedChangeListener { _ , isChecked ->

            val serviceIntent = Intent(requireContext(), LocationService::class.java)

            if(isChecked){
//
                requireActivity().startService(serviceIntent)

            }else
            {
//
                requireActivity().stopService(serviceIntent)

            }
        }

        mDrawerToggle!!.syncState()
        btnSignOut.setOnClickListener {
         var mAuth:FirebaseAuth=FirebaseAuth.getInstance()
            val intent= Intent(requireContext(), SignInActivity::class.java)
            val serviceIntent = Intent(requireContext(), LocationService::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            requireActivity().stopService(serviceIntent)
            mAuth.signOut()
            startActivity(intent)
            requireActivity().finish()
        }
        rankViewModel.getUserRank(mCurrentUser.key,this.requireContext())
        setupObserver()
        txtProfileName.setText(mCurrentUser.name)
        txtProfileLastname.setText(mCurrentUser.lastname)
        txtProfileNumber.setText(mCurrentUser.number)
        txtProfileUsername.setText(mCurrentUser.username)
        Glide.with(this.requireContext())
            .load(mCurrentUser.photoUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageViewProfile)

    }

    fun setupObserver(){
        rankViewModel.userRank?.observe(viewLifecycleOwner, Observer {
            txtProfilePoints.setText(rankViewModel.userRank.value.toString())
        })
    }


}
