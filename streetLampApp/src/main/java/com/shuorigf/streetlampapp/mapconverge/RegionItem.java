package com.shuorigf.streetlampapp.mapconverge;

import com.amap.api.maps.model.LatLng;

/**
 * Created by yiyi.qi on 16/10/10.
 */

public class RegionItem implements ClusterItem {
    private LatLng mLatLng;
    private String status;
    private String isfaulted;
    private String lamp_id;
    public RegionItem(LatLng latLng,String isfaulted, String status,String lamp_id) {
        mLatLng=latLng;
        this.isfaulted=isfaulted;
        this.status=status;
        this.lamp_id=lamp_id;
    }

    @Override
    public LatLng getPosition() {
        // TODO Auto-generated method stub
        return mLatLng;
    }
    public String getStatus(){
        return status;
    }

    public String getIsfaulted() {
        return isfaulted;
    }

    public String getLamp_id() {
        return lamp_id;
    }
}
