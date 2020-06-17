package dimitrijestefan.mosis.ehelp

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class AddObjectViewModel : ViewModel() {



//    private var liveData = MutableLiveData<ArrayList<HelpRequest>>()
//
//    init {
//        liveData=MyHelpRequests.getData()
//    }


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


//    fun getLiveData():LiveData<ArrayList<HelpRequest>>{
//        return liveData
//    }




    // TODO: Implement the ViewModel
}
