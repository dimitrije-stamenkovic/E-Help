package dimitrijestefan.mosis.ehelp.Models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class HelpRequest(
                       var owner:String?= "",
                       var title:String? ="",
                       var urgency : String? ="",
                       var category: String? ="",
                       var about:String? ="",
                       var latitude:String? ="",
                       var longitude:String? =""){

    @Exclude
    fun toMap() : Map<String,Any?>{
        return mapOf(
            "owner" to owner,
            "title" to title,
            "urgency" to urgency,
            "category" to category,
            "about" to about,
            "latitude" to latitude,
            "longitude" to longitude
        )
    }
}

