package com.xongolab.hotellifyr.custom

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class HotelClusterItem(
    private val position: LatLng,
    private val clusterTitle: String,
    private val snippet: String,
) : ClusterItem {
    override fun getPosition(): LatLng = position
    override fun getTitle(): String = clusterTitle
    override fun getSnippet(): String = snippet
    override fun getZIndex(): Float? {
        TODO("Not yet implemented")
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HotelClusterItem) return false
        return position == other.position && clusterTitle == other.clusterTitle && snippet == other.snippet
    }

    override fun hashCode(): Int {
        var result = position.hashCode()
        result = 31 * result + clusterTitle.hashCode()
        result = 31 * result + snippet.hashCode()
        return result
    }
}
