package dimitrijestefan.mosis.ehelp.Models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class GeoPoint (var latitude:Double,var longitude:Double){

    @get:Exclude
    var userId:String=""


  constructor():this(0.0,0.0)
}