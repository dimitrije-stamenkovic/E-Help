package dimitrijestefan.mosis.ehelp

import android.location.Location
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import dimitrijestefan.mosis.ehelp.Data.AllHelpRequestsData
import dimitrijestefan.mosis.ehelp.Models.Friend

import dimitrijestefan.mosis.ehelp.Models.HelpRequest
//import dimitrijestefan.mosis.ehelp.Data.Repository

class MapViewModel : ViewModel() {


    var filter = false
    var reset = false

    lateinit var current_location : LatLng
    lateinit var title:String
    lateinit var urgency: String
    lateinit var category: String
    lateinit var radius : String
    lateinit var clickedFriend: Friend
    fun isClickedFriendInitialized() = this::clickedFriend.isInitialized
    var boolShowUserFriends:Boolean= true



//    var help_requests = Repository.getHelpRequestList()
    lateinit var filtered_requests : ArrayList<HelpRequest>


//    fun filterRequests(){
//        for(request in AllHelpRequestsData.requests){
//            if()
//        }
//       // filtered_requests =  AllHelpRequestsData.requests.filter{ it.title!!.contains(title) && it.urgency==urgency && it.category==category }
//    }



    // TODO: Implement the ViewModel
}