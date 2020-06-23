package dimitrijestefan.mosis.ehelp


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import dimitrijestefan.mosis.ehelp.Data.FriendData
import dimitrijestefan.mosis.ehelp.Data.MyHelpRequestsData
import dimitrijestefan.mosis.ehelp.Data.UsersLocationData
import dimitrijestefan.mosis.ehelp.Models.GeoPoint
import kotlinx.android.synthetic.main.fragment_rank_list.*

/**
 * A simple [Fragment] subclass.
 */
class RankListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rank_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


       MyHelpRequestsData.onRequestsChange.observe(viewLifecycleOwner, Observer {

           if (MyHelpRequestsData.requests != null){
               temp.text = MyHelpRequestsData.requests.toString()

           }

       })
        UsersLocationData.onUserLocationChanged.observe(viewLifecycleOwner, Observer {

          //  var lista :ArrayList<GeoPoint> =UsersLocationData.onUserLocationChanged.value[0].toString()
            if(UsersLocationData.onUserLocationChanged!=null){
           // var geoPoint = UsersLocationData.onUserLocationChanged?.value[0]
            Log.e("Lista usera", UsersLocationData.onUserLocationChanged.value?.last()?.latitude.toString())
            }
        })
        UsersLocationData.onFriendLocationChanged.observe(viewLifecycleOwner, Observer {
            if(UsersLocationData.onFriendLocationChanged!=null){
                Log.e("Lista prijatelja", UsersLocationData.onFriendLocationChanged.value?.first()?.latitude.toString())

            }
        })
        FriendData.onFriendsListChanged.observe(viewLifecycleOwner, Observer {
            if(FriendData.onFriendsListChanged!=null)
                Log.e("Update friend", FriendData.onFriendsListChanged.value?.last()?.userId.toString())
        })
    }
}
