package dimitrijestefan.mosis.ehelp.CustomMarker

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import dimitrijestefan.mosis.ehelp.Models.Friend

public class ClusterMarker : ClusterItem {
    private lateinit var position:LatLng
    private lateinit var title:String
    private lateinit var snippet:String
    private lateinit var friend: Friend
    private lateinit var imageUrl:String

    constructor(
        position: LatLng,
        title: String,
        snippet: String,
        friend:Friend
    ) {
        this.position = position
        this.title = title
        this.snippet = snippet
        this.imageUrl = friend.photoUrl
        this.friend=friend
    }

    constructor()

fun getUrl():String?{
    return imageUrl
}
    override fun getSnippet(): String? {
        return snippet
    }

    override fun getTitle(): String? {
        return title
    }

    override fun getPosition(): LatLng {
        return position
    }

    fun getFriend():Friend{
        return  friend
    }

}