package com.shuorigf.streetlampapp.util;

import android.content.Context;
import android.location.LocationManager;

public class locationUtils {
	
    /** 
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的 
     * @param context 
     * @return true 表示开启 
     */  
    public static boolean isOpen(Context context) {  
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);  
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);  
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);  
        if (gps || network) {  
            return true;  
        }  
  
        return false;  
    }  

}
