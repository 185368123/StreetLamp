package com.shuorigf.streetlampapp.info;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.shuorigf.streetlampapp.app.StreetlampApp;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class GetLocationInfo {
	//baidu 
	// 	8563038 		GKsIZNxNXGdG5NjWsZvwMj5eVxf9qwYA 	
	
	private static GetLocationInfo mPrivateInfo;
	
	
    private GetLocationInfo() {
		super();
	}
    public static GetLocationInfo getInstance()
    {
    	if(mPrivateInfo == null)
    	{
    		mPrivateInfo = new GetLocationInfo();
    	}
    	return mPrivateInfo;
    }


    public String getCityLocation()
    {
		GetLocationInfoForGoogle.getCNBylocation(StreetlampApp.getContextObject());
    	return GetLocationInfoForGoogle.cityName;
    }

   
}
