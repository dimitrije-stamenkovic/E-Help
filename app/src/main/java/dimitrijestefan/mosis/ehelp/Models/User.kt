package dimitrijestefan.mosis.ehelp.Models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import java.io.Serializable

data class User  (
    var username:String,
    var password:String,
    var email:String,
    var name:String,
    var lastname:String,
    var number:String,
    var photoUrl:String

):Serializable{
    @Exclude
    lateinit var key : String
    constructor():this("","","","","","","")
}

