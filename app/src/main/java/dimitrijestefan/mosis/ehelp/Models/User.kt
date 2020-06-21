package dimitrijestefan.mosis.ehelp.Models

import com.google.firebase.database.Exclude

data class User (
    var username:String,
    var password:String,
    var email:String,
    var name:String,
    var lastname:String,
    var number:String,
    var photoUrl:String

){
    @Exclude
    lateinit var key : String
    constructor():this("","","","","","","")
}

