package com.shuorigf.streetlampapp.mapconverge;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiyi.qi on 16/10/10.
 */

public class Cluster {
    private LatLng mLatLng;
    private List<ClusterItem> mClusterItems;
    private Marker mMarker;

    Cluster(LatLng latLng) {
        mLatLng = latLng;
        mClusterItems = new ArrayList<>();
    }

    void addClusterItem(ClusterItem clusterItem) {
        mClusterItems.add(clusterItem);
    }

    public int getClusterCount() {
        return mClusterItems.size();
    }

    LatLng getCenterLatLng() {
        return mLatLng;
    }

    Marker getMarker() {
        return mMarker;
    }

    void setMarker(Marker marker) {
        mMarker = marker;
    }

    public List<ClusterItem> getClusterItems() {
        return mClusterItems;
    }
}
