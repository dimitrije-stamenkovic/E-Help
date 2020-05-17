package dimitrijestefan.mosis.ehelp

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import dimitrijestefan.mosis.ehelp.data.HelpRequest
import dimitrijestefan.mosis.ehelp.data.HelpRequestManager

class AddObjectViewModel : ViewModel() {



    //DATA
     var select : Boolean = false
     var location : LatLng? = null



    fun changeSelect (){
        select = select.not()
    }

    fun addRequest(request: HelpRequest)= HelpRequestManager.addHelpRequest(request)




    // TODO: Implement the ViewModel
}
