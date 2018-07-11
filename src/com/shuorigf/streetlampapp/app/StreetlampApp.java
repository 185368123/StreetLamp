package com.shuorigf.streetlampapp.app;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.service.LocationService;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import android.app.Application;
import android.content.Context;

public class StreetlampApp extends Application {
	   private static Context context;  
//	   public LocationService locationService;
	   
	   public LoginData mLoginData;
	      
	    @Override  
	    public void onCreate() {  
	        context = getApplicationContext();
//	        locationService = new LocationService(getApplicationContext());
//	        SDKInitializer.initialize(getApplicationContext());
	        
	        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
	        OkHttpClient okHttpClient = new OkHttpClient.Builder()
	        .connectTimeout(15000L, TimeUnit.MILLISECONDS)
	        .readTimeout(15000L, TimeUnit.MILLISECONDS)
	        .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
	        .build();
	        
	        OkHttpUtils.initClient(okHttpClient);
	    }  

	    public static Context getContextObject(){  
	        return context;  
	    }  
}
