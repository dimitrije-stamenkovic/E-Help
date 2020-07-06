package dimitrijestefan.mosis.ehelp.Models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class User  (
    var username:String,
    var password:String,
    var email:String,
    var name:String,
    var lastname:String,
    var number:String,
    var photoUrl:String,
    var points:Int

):Serializable{
    @get:Exclude
    lateinit var key : String
    @Exclude @set:Exclude @get:Exclude
    lateinit var mToken : String
    constructor():this("","","","","","","",0)
}

