package dimitrijestefan.mosis.ehelp.data

object HelpRequestManager {
    private val help_requests = ArrayList<HelpRequest>()


    fun requestsList(): ArrayList<HelpRequest> = help_requests

    fun getHelpRequest(id:Int):HelpRequest = help_requests.get(id)

    fun addHelpRequest(request: HelpRequest){
        help_requests.add(request)
    }

    fun deleteHelpRequest(id:Int) = help_requests.removeAt(id)

}