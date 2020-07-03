package dimitrijestefan.mosis.ehelp.Adapters

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import de.hdodenhof.circleimageview.CircleImageView
import dimitrijestefan.mosis.ehelp.MapViewModel
import dimitrijestefan.mosis.ehelp.Models.Friend
import dimitrijestefan.mosis.ehelp.R
import kotlinx.android.synthetic.main.fragment_profile.*


class CustomInfoWindowFriendAdapter: GoogleMap.InfoWindowAdapter {

    private lateinit var mContext: Context
    private lateinit var mWindow: View
   private  lateinit var mapViewModel:MapViewModel


    constructor(mViewModel:MapViewModel,mContext: Context) {
        this.mapViewModel=mViewModel
        this.mContext = mContext
        this.mWindow=LayoutInflater.from(mContext).inflate(R.layout.custom_info_window_friend, null)
      //  this.mFriend=mFriend
    }

    private fun renderWindow(marker:Marker?){
        if(mapViewModel.clickedFriend!=null){
            var friend:Friend= mapViewModel.clickedFriend!!
            mWindow.findViewById<TextView>(R.id.txtCustomIWFriendName).setText(friend.name)
            mWindow.findViewById<TextView>(R.id.txtCustomIWFriendLastName).setText(friend.lastname)
            mWindow.findViewById<TextView>(R.id.txtCustomIWFriendEmail).setText(friend.email)
            mWindow.findViewById<TextView>(R.id.txtCustomIWFriendUsername).setText(friend.username)
            var imageView: ImageView = mWindow.findViewById(R.id.CustomIWCircleView)


        }

       //mView.findViewById<TextView>(R.id.txtCustomIWFriendName).setText(mFriend.username)
    }

    override fun getInfoContents(p0: Marker?): View {
       renderWindow(p0)
       // Log.e("Cist","Uso u custom w1")
        return mWindow
    }

    override fun getInfoWindow(p0: Marker?): View {
        renderWindow(p0)
       // Log.e("Cist","Uso u custom w2")
        return mWindow
    }


}