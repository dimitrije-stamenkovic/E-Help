package dimitrijestefan.mosis.ehelp


import androidx.lifecycle.ViewModel

import dimitrijestefan.mosis.ehelp.data.HelpRequest
import dimitrijestefan.mosis.ehelp.data.HelpRequestManager

class MapViewModel : ViewModel() {


    val filter = false


    fun getRequest():ArrayList<HelpRequest>{
        return HelpRequestManager.requestsList()
    }


    // TODO: Implement the ViewModel
}