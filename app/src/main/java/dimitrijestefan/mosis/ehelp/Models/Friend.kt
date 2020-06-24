package dimitrijestefan.mosis.ehelp.Models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Friend(var name:String, var lastname:String,
                  var email:String,
                  var photoUrl:String, var username:String){
    @get:Exclude
    var userId:String=""
    constructor():this("","","","","")
}

