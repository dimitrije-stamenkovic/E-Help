package dimitrijestefan.mosis.ehelp


import android.annotation.SuppressLint
import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import dimitrijestefan.mosis.ehelp.Activity.SignInActivity
import dimitrijestefan.mosis.ehelp.Data.UserData
import dimitrijestefan.mosis.ehelp.Service.LocationService
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    private lateinit var mDrawerToggle:ActionBarDrawerToggle


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
    }


}
