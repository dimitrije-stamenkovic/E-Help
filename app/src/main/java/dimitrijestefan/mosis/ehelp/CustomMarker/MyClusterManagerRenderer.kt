package dimitrijestefan.mosis.ehelp.CustomMarker

import android.content.Context
import android.graphics.Bitmap

import android.graphics.drawable.Drawable

import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget

import com.bumptech.glide.request.target.SimpleTarget

import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.GoogleMap

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import de.hdodenhof.circleimageview.CircleImageView
import dimitrijestefan.mosis.ehelp.R
import dimitrijestefan.mosis.ehelp.R.color


public class MyClusterManagerRenderer : DefaultClusterRenderer<ClusterMarker> {
    private lateinit var iconGenerator: IconGenerator
   // private lateinit var imageView: ImageView
    private lateinit var circleImageView: CircleImageView
    private var markerWidth: Int =0
    private var markerHeight: Int = 0
    private var context: Context?

    constructor(
        context: Context?,
        map: GoogleMap?,
        clusterManager: ClusterManager<ClusterMarker>?
    ) : super(context, map, clusterManager) {

        iconGenerator = IconGenerator(context?.applicationContext)
    //    imageView = ImageView(context?.applicationContext)
        circleImageView= CircleImageView(context?.applicationContext)
        this.context = context
        markerWidth= context?.resources?.getDimension(R.dimen.custom_marker_image)?.toInt()!!
        markerHeight=context?.resources?.getDimension(R.dimen.custom_marker_image)?.toInt()!!
       // imageView.layoutParams  = ViewGroup.LayoutParams(markerWidth, markerHeight)
        circleImageView.layoutParams  = ViewGroup.LayoutParams(markerWidth, markerHeight)

       // var padding:Int=context?.resources?.getDimension(R.dimen.custom_marker_padding)?.toInt()!!
       // imageView.setPadding(padding,padding,padding,padding)
       // circleImageView.setPadding(padding,padding,padding,padding)
       // iconGenerator.setContentView(imageView)
        iconGenerator.setContentView(circleImageView)

    }

    override fun onBeforeClusterItemRendered(item: ClusterMarker, markerOptions: MarkerOptions) {
        var icon: Bitmap = iconGenerator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.title)
    }


    override fun onClusterItemRendered(clusterItem: ClusterMarker, marker: Marker) {
        super.onClusterItemRendered(clusterItem, marker)
        Glide.with(this.context!!)
            .load(clusterItem.getUrl())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                   // imageView.setImageDrawable(resource)
                    circleImageView.setImageDrawable(resource)
                    var icon: Bitmap = iconGenerator.makeIcon()
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon))
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })
    }

    override fun shouldRenderAsCluster(cluster: Cluster<ClusterMarker>): Boolean {
        return false
    }

}

