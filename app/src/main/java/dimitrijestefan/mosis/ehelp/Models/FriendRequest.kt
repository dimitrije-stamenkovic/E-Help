package dimitrijestefan.mosis.ehelp.Models


data class FriendRequest(
//    var uidSender:String?="",
//    var nameSender:String="",
//    var lastnameSender:String="",
//    var emailSender:String="",
    var userSender:Friend,
    var request_type:String=""
) {
    constructor():this(Friend(),"")
}