package com.shuorigf.streetlampapp.network;

import java.util.ArrayList; 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;






import com.google.gson.Gson;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.WeatherData;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.util.Base64;

public class NetManager {
	private String TAG="NetManager";
	public static String SUCCESS ="0000" ; 
	public static String NO_USER_NAME ="0001" ; 
	public static String NO_TOKEN ="0002" ; 
	public static String NO_CLIENT_KEY ="0003" ; 
	public static String LOGIN_INFORMATION_HAS_EXPIRED ="0004" ;
	public static String PASSWORD_IS_NULL ="0100" ; 
	public static String USER_NAME_OR_PASSWORD_ERROR ="0101" ; 
	public static String USER_NON_EXISTENT ="0102" ; 
	
	
	
	private static final String SERVER_URL1 = "http://139.196.213.241/api/";
	private static final String SERVER_URL = "http://123.57.20.89/api/";
	private static final String SERVER_URL_ = "http://test.solar-iot.com/api/";
	private static final String SERVER_URL_NEW = "http://new.solar-iot.com/api/";
	//see -> http://139.196.213.241/doc/api.html
	//    normal: http://123.57.20.89/api/
	//   test: http://139.196.213.241/api/
	public static final String GETWEATHERDAT_URL="http://api.openweathermap.org/data/2.5/weather?q=Zhuhai,uk&appid=193053bb6a1ab74fb430a4606bbc3ba2&lang=zh_cn";
	public static final String LOGIN_URL =SERVER_URL+ "login/verify";//1.1
	public static final String HOMEDATA_URL =SERVER_URL+ "home/data";//2.1
	public static final String DEVICEDATA_URL =SERVER_URL+ "device/data";//3.1
	public static final String PROJECT_LIST_URL =SERVER_URL+"device/project/get";//3.1.1
	public static final String PROJECT_DETAILS_URL =SERVER_URL+"device/project/detail";//3.1.2
	public static final String PROVINCE_LIST_URL =SERVER_URL+"home/province_list";//3.1.3
	public static final String PROJECT_ADD_OR_EDIT_URL =SERVER_URL+"device/project/save";//3.1.4
	public static final String PROJECT_DEL_URL =SERVER_URL+"device/project/del";//3.1.5
	public static final String PROJECT_INSPECTION_URL =SERVER_URL+"device/project/patrol";//3.1.6
	public static final String NETWORK_LIST_URL =SERVER_URL+"device/network/get";//3.2.1
	public static final String NETWORK_DEL_URL =SERVER_URL+"device/network/del";//3.2.2
	public static final String NETWORK_DETAILS_URL =SERVER_URL+"device/network/detail";//3.2.3
	public static final String NETWORK_ADD_OR_EDIT_URL =SERVER_URL+"device/network/save";//3.2.4
	public static final String LAMP_CONTROL_LIST_URL =SERVER_URL+"device/lampcontrol/get";//3.3.1
	public static final String LAMP_CONTROL_UPDATE_URL =SERVER_URL+"device/lampcontrol/update";//3.3.2
	public static final String LAMP_CONTROL_TURN_ON_OFF_URL =SERVER_URL+"device/lampcontrol/turnonoff";//3.3.3
	public static final String LAMP_CONTROL_DIMMING_URL =SERVER_URL+"device/lampcontrol/dimming";//3.3.4
	public static final String LAMP_CONTROL_DEL_URL =SERVER_URL+"device/lampcontrol/del";//3.3.5
	public static final String LAMP_CONTROL_ADD_OR_EDIT_URL =SERVER_URL+"device/lampcontrol/save";//3.3.6
	public static final String LAMP_CONTROL_DETAILS_URL =SERVER_URL+"device/lampcontrol/detail";//3.3.8
	public static final String LAMP_CONTROL_LOAD_SETTING_URL =SERVER_URL+"device/lampcontrol/load_setting";//3.3.9
	public static final String LAMP_CONTROL_LOAD_SETTING_URL_NEW =SERVER_URL_NEW+"device/lampcontrol/load_setting";
	public static final String LAMP_CONTROL_BATTERY_SETTING_URL =SERVER_URL+"device/lampcontrol/battery_setting";//3.3.9
	public static final String LAMP_UPDATE_HISTORY_URL =SERVER_URL+"device/lampcontrol/viewloglist";//3.3.11
	public static final String REPORT_URL =SERVER_URL+"report/data";//4.0
	public static final String FAULT_LIST_URL =SERVER_URL+"alarm/get";//5.0.1
	public static final String FAULT_DETAILS_URL =SERVER_URL+"alarm/detail";//5.0.2
	public static final String FAULT_UPDATE_URL =SERVER_URL+"alarm/set";//5.0.3
	public static final String FAULT_DEL_URL =SERVER_URL+"alarm/del";//5.0.4
	public static final String FAULT_LIST_URL_NEW =SERVER_URL_NEW+"alarm/get";//5.0.1
	public static final String FILTER_PROJECT_URL =SERVER_URL_NEW+"device/project/get";
	public static final String FILTER_FAILURE_TYPE_URL =SERVER_URL_NEW+"home/alarm_type_list";
	public static final String FILTER_DEAL_URL =SERVER_URL+"device/project/get";

	private LoginData mLoginData=null;

	private static NetManager manager = null;

	private NetManager() {
	};

	public synchronized static NetManager getInstance() {
		if (null == manager) {
			synchronized (NetManager.class) {
				manager = new NetManager();
			}
		}
		return manager;
	}

	
	@SuppressLint("NewApi")
	public String callApiForUrl(String url,List<NameValuePair> pair)
	{
		String result = "";
		NameValuePair pairusername = new BasicNameValuePair("username", mLoginData.getClient_key());
		NameValuePair pairimei = new BasicNameValuePair("client_key", mLoginData.getClient_key());
		NameValuePair pairtoken = new BasicNameValuePair("token", mLoginData.getClient_key());
		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
		pairList.add(pairusername);
		pairList.add(pairtoken);
		pairList.add(pairimei);
		
		if(pair != null)
		{
			pairList.addAll(pair);
		}
		
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		try
		{
			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(
					pairList);
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(requestHttpEntity);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(httpPost);
			result =  EntityUtils.toString(response.getEntity(), "UTF-8");
			Log.d(TAG,"result:"+result);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	@SuppressLint("NewApi")
	public WeatherData getWeatherData(String local)
	{

		String result = null;
		WeatherData data;
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		try
		{
			HttpPost httpPost = new HttpPost(GETWEATHERDAT_URL);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(httpPost);
			result =  EntityUtils.toString(response.getEntity(), "UTF-8");
			Log.d(TAG,"result:"+result);
			Gson gson = new Gson();
			data = gson.fromJson(result, WeatherData.class);
			return data;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
}
