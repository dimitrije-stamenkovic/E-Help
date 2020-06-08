package dimitrijestefan.mosis.ehelp


import androidx.lifecycle.ViewModel

import dimitrijestefan.mosis.ehelp.data.HelpRequest
import dimitrijestefan.mosis.ehelp.data.HelpRequestManager

class MapViewModel : ViewModel() {


    var filter = false
    var reset = false

    var help_requests = HelpRequestManager.requestsList()
    internal lateinit var filtered_requests : List<HelpRequest>


    fun filterRequests(title:String,urgency:String,category:String){
        filtered_requests =  help_requests.filter{ it.title.contains(title) && it.urgency==urgency && it.category==category }
    }



    // TODO: Implement the ViewModel
}