package dimitrijestefan.mosis.ehelp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dimitrijestefan.mosis.ehelp.domain.HelpRequest
import dimitrijestefan.mosis.ehelp.domain.HelpRequestManager

class AddObjectViewModel : ViewModel() {

//    private val status = MutableLiveData<Boolean>()
//
//    val observableStatus : LiveData<Boolean>
//        get() = status
//


//    val selectCoords = MutableLiveData<Int>(0)
//
//    private val lat = MutableLiveData<String>()
//
//    fun setLat(latitude:String){
//        lat.value=latitude
//    }
//
//    fun getLat(): LiveData<String> = lat


    private var lat = MutableLiveData<String>()
    var pom:Int?=null

    fun setLat(latitude:String) {
        lat.value=latitude
    }

    fun getLat():LiveData<String>{
        return lat
    }


    fun addRequest(request: HelpRequest)= HelpRequestManager.addHelpRequest(request)



//    fun addRequest(request: HelpRequest){
//        status.value=try{
//            HelpRequestManager.addHelpRequest(request)
//            true
//        }
//        catch (e:IllegalArgumentException){
//            false
//        }
//    }
    // TODO: Implement the ViewModel
}
