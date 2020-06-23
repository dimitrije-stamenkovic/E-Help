package dimitrijestefan.mosis.ehelp.CustomMarker

import android.content.Context
import android.graphics.Bitmap

import android.graphics.drawable.Drawable

import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide

import com.bumptech.glide.load.engine.DiskCacheStrategy

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


public class MyClusterManagerRenderer : DefaultClusterRenderer<ClusterMarker> {
    private lateinit var iconGenerator: IconGenerator
    private lateinit var imageView: ImageView
    private var markerWidth: Int = 10
    private var markerHeight: Int = 10
    private var context: Context?

    constructor(
        context: Context?,
        map: GoogleMap?,
        clusterManager: ClusterManager<ClusterMarker>?
    ) : super(context, map, clusterManager) {
        iconGenerator = IconGenerator(context?.applicationContext)
        imageView = ImageView(context?.applicationContext)
        imageView.layoutParams = ViewGroup.LayoutParams(50, 50)
        imageView.setPadding(2, 2, 2, 2)
        iconGenerator.setContentView(imageView)
        this.context = context

    }


    override fun onBeforeClusterItemRendered(item: ClusterMarker, markerOptions: MarkerOptions) {
        var icon: Bitmap = iconGenerator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.title)
    }


    override fun onClusterItemRendered(clusterItem: ClusterMarker, marker: Marker) {
        super.onClusterItemRendered(clusterItem, marker)
        Glide.with(this.context!!)
            .load("https://firebasestorage.googleapis.com/v0/b/ehelp-277212.appspot.com/o/usersPhoto%2FAoz8pqFKVbPJqYjPzhSYCQBbsHX2%2F3d7ad12e-1951-47f3-bdd8-7a137cc045ee?alt=media&token=94abd7b3-dbce-43f1-a35a-d3338286a11f")
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    imageView.setImageDrawable(resource)
                    var icon: Bitmap = iconGenerator.makeIcon()
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon))
                }

            })
    }


    override fun shouldRenderAsCluster(cluster: Cluster<ClusterMarker>): Boolean {
        return false
    }

}

