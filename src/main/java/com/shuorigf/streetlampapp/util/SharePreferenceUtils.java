package com.shuorigf.streetlampapp.util;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuorigf.streetlampapp.data.LampControlReportData;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharePreference的工具类, 保存和配置一些设置信息
 * 
 * @author clx
 * 
 */
public class SharePreferenceUtils {

	private static final String SHARE_PREFS_NAME = "config";
	private static SharedPreferences mSharedPreferences;

	public static void putBoolean(Context ctx, String key, boolean value) {
		if (mSharedPreferences == null) {
			mSharedPreferences = ctx.getSharedPreferences(SHARE_PREFS_NAME,
					Context.MODE_PRIVATE);
		}

		mSharedPreferences.edit().putBoolean(key, value).commit();
	}

	public static boolean getBoolean(Context ctx, String key,
			boolean defaultValue) {
		if (mSharedPreferences == null) {
			mSharedPreferences = ctx.getSharedPreferences(SHARE_PREFS_NAME,
					Context.MODE_PRIVATE);
		}

		return mSharedPreferences.getBoolean(key, defaultValue);
	}

	public static void putString(Context ctx, String key, String value) {
		if (mSharedPreferences == null) {
			mSharedPreferences = ctx.getSharedPreferences(SHARE_PREFS_NAME,
					Context.MODE_PRIVATE);
		}

		mSharedPreferences.edit().putString(key, value).commit();
	}

	public static String getString(Context ctx, String key, String defaultValue) {
		if (mSharedPreferences == null) {
			mSharedPreferences = ctx.getSharedPreferences(SHARE_PREFS_NAME,
					Context.MODE_PRIVATE);
		}

		return mSharedPreferences.getString(key, defaultValue);
	}
	

	public static void setDataList(Context ctx, String key, List<LampControlReportData> list) {  
        if (mSharedPreferences == null) {
			mSharedPreferences = ctx.getSharedPreferences(SHARE_PREFS_NAME,
					Context.MODE_PRIVATE);
		}
        if (null == list || list.size() <= 0)  {
        	mSharedPreferences.edit().putString(key, null).commit();
        }else {
        	 Gson gson = new Gson();  
             String strJson = gson.toJson(list);  
             mSharedPreferences.edit().putString(key, strJson).commit();
        }
        
    }  
  
    public static List<LampControlReportData> getDataList(Context ctx, String key,
			String defaultValue) {  
    	if (mSharedPreferences == null) {
			mSharedPreferences = ctx.getSharedPreferences(SHARE_PREFS_NAME,
					Context.MODE_PRIVATE);
		}
        String strJson = mSharedPreferences.getString(key, defaultValue); 
        if (null == strJson) {  
            return null;  
        }  
        Gson gson = new Gson();  
        List<LampControlReportData> datalist = gson.fromJson(strJson, new TypeToken<List<LampControlReportData>>() {  
        }.getType());  
        return datalist;  
  
    }  

}
