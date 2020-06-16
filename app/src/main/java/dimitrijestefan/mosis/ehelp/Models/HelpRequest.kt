package dimitrijestefan.mosis.ehelp.Models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class HelpRequest(
    var userId:String?= "",
    var title:String? ="",
    var urgency : String? ="",
    var category: String? ="",
    var about:String? ="",
    var latitude:String? ="",
    var longitude:String? =""){

    @Exclude lateinit var key : String

    @Exclude
    fun toMap() : Map<String,Any?>{
        return mapOf(
            "userId" to userId,
            "title" to title,
            "urgency" to urgency,
            "category" to category,
            "about" to about,
            "latitude" to latitude,
            "longitude" to longitude
        )
    }
}

