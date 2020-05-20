package dimitrijestefan.mosis.ehelp.data

object HelpRequestManager {
    private val help_requests = ArrayList<HelpRequest>()
    private val filtered_help_requests = ArrayList<HelpRequest>()

    fun requestsList(): ArrayList<HelpRequest> = help_requests


    fun getFilteredList(): ArrayList<HelpRequest> = filtered_help_requests


    fun setFilteredList(title:String,radius:Int,category:String,urgency : String){
        //TODO user object must be created so coordinates of center can be extracted :)

    }

    fun getHelpRequest(id:Int):HelpRequest = help_requests.get(id)

    fun addHelpRequest(request: HelpRequest){
        help_requests.add(request)
    }

    fun deleteHelpRequest(id:Int) = help_requests.removeAt(id)

}