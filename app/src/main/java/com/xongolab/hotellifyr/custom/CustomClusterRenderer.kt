package com.xongolab.hotellifyr.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.xongolab.hotellifyr.R

class CustomClusterRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<HotelClusterItem>
) : DefaultClusterRenderer<HotelClusterItem>(context, map, clusterManager) {

    private val iconGenerator = IconGenerator(context)
    // Inflate the custom cluster layout and set it in IconGenerator
    @SuppressLint("InflateParams")
    private val clusterView: View =
        LayoutInflater.from(context).inflate(R.layout.custom_cluster_icon, null)

    init {
        iconGenerator.setContentView(clusterView)
    }

    override fun onBeforeClusterRendered(
        cluster: Cluster<HotelClusterItem>,
        markerOptions: MarkerOptions
    ) {
        val predominantSnippet = cluster.items
            .groupingBy { it.snippet }
            .eachCount()
            .maxByOrNull { it.value }?.key ?: "Default"

        val clusterIconResId = R.drawable.ic_location_secondary

        val clusterIcon = BitmapDescriptorFactory.fromResource(clusterIconResId)
        markerOptions.icon(clusterIcon)
    }

    @SuppressLint("SetTextI18n")
    private fun createClusterIcon(count: Int): Bitmap {
        val textView = clusterView.findViewById<TextView>(R.id.cluster_count)
        if (textView != null) {
            Log.d("CustomClusterRenderer", "Updating cluster count: $count+")
            textView.text = "$count+"
        } else {
            Log.e("CustomClusterRenderer", "TextView (R.id.cluster_count) not found!")
        }
        return iconGenerator.makeIcon()
    }

    override fun shouldRenderAsCluster(cluster: Cluster<HotelClusterItem>): Boolean {
        Log.d("CustomClusterRenderer", "Should render cluster: ${cluster.size > 1}")
        return cluster.size > 1 // Only cluster when there are more than one item
    }

    override fun onClusterItemRendered(clusterItem: HotelClusterItem, marker: Marker) {
        super.onClusterItemRendered(clusterItem, marker)
        try {
            val iconResId = R.drawable.ic_location_secondary

            val bitmapDraw = ContextCompat.getDrawable(context, iconResId) as BitmapDrawable
            val scaledBitmap = Bitmap.createScaledBitmap(bitmapDraw.bitmap, 100, 100, false)
            val descriptor = BitmapDescriptorFactory.fromBitmap(scaledBitmap)
            marker.setIcon(descriptor) // Safely set the icon
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}