package dimitrijestefan.mosis.ehelp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import dimitrijestefan.mosis.ehelp.domain.HelpRequest
import dimitrijestefan.mosis.ehelp.domain.HelpRequestManager

class AddObjectViewModel : ViewModel() {



    //DATA

     var select : Boolean = false
     var location : LatLng? = null



    fun addRequest(request: HelpRequest)= HelpRequestManager.addHelpRequest(request)




    // TODO: Implement the ViewModel
}
