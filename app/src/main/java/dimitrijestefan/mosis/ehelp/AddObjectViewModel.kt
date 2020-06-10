package dimitrijestefan.mosis.ehelp

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import dimitrijestefan.mosis.ehelp.Models.HelpRequest
import dimitrijestefan.mosis.ehelp.data.Repository

class AddObjectViewModel : ViewModel() {



    //DATA
     var location : LatLng? = null
     var title:String? = null
     var urgency : Int? = null
     var category: Int? = null
     var about:String? = null



    //STATE
    var select : Boolean = false

    fun changeSelect (){
        select = select.not()
    }





    // TODO: Implement the ViewModel
}
