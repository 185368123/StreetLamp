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
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.DeviceData;
import com.shuorigf.streetlampapp.data.DeviceLampcontrolGetData;
import com.shuorigf.streetlampapp.data.HomeData;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.ProjectData;
import com.shuorigf.streetlampapp.data.WeatherData;
import com.zhy.http.okhttp.OkHttpUtils;

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
	public static final String LAMP_CONTROL_BATTERY_SETTING_URL =SERVER_URL+"device/lampcontrol/battery_setting";//3.3.9
	public static final String LAMP_UPDATE_HISTORY_URL =SERVER_URL+"device/lampcontrol/viewloglist";//3.3.11
	public static final String REPORT_URL =SERVER_URL+"report/data";//4.0
	public static final String FAULT_LIST_URL =SERVER_URL+"alarm/get";//5.0.1
	public static final String FAULT_DETAILS_URL =SERVER_URL+"alarm/detail";//5.0.2
	public static final String FAULT_UPDATE_URL =SERVER_URL+"alarm/set";//5.0.3
	public static final String FAULT_DEL_URL =SERVER_URL+"alarm/del";//5.0.4
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
//	public LoginData getLoginData(String user,String pwd,String imei)
//	{
//		Map<String, String> params = new HashMap<String, String>();
//        params.put("username", user);
//        params.put("password", pwd);
//        params.put("client_key", imei);
//        OkHttpUtils
//                .post()
//                .url(LOGIN_URL)
//                .params(params)
//                .build()
//                .execute(new GenericsCallback<LoginData>(new JsonGenericsSerializator())
//                      {
//                	
//                	
//                      @Override
//                      public void onError(Call call, Exception e, int id)
//                      {
//                    	  e.printStackTrace();
//                    	  Log.e(TAG, "LoginData:"+e.getMessage());
//                      }
//					@Override
//					public void onResponse(LoginData loginData, int id) {
//						 mLoginData = loginData;
//					}
//                  });
//		
//		
//		return mLoginData;
//	}
//	public HomeData getHomeData()
//	{
//		return  getHomeData(mLoginData.getUsername(),mLoginData.getMsg(),mLoginData.getClient_key());
//	}
//
//	@SuppressLint("NewApi")
//	public HomeData getHomeData(String user,String imei,String token)
//	{
//		String result = null;
//		NameValuePair pairusername = new BasicNameValuePair("username", user);
//		NameValuePair pairimei = new BasicNameValuePair("client_key", imei);
//		NameValuePair pairtoken = new BasicNameValuePair("token", token);
//		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
//		pairList.add(pairusername);
//		pairList.add(pairtoken);
//		pairList.add(pairimei);
//		Log.d(TAG,"user:"+user+",imei:"+imei+",token:"+token);
//		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		StrictMode.setThreadPolicy(policy);
//		try
//		{
//			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(
//					pairList);
//			HttpPost httpPost = new HttpPost(HOMEDATA_URL);
//			httpPost.setEntity(requestHttpEntity);
//			HttpClient httpClient = new DefaultHttpClient();
//			HttpResponse response = httpClient.execute(httpPost);
//			result =  EntityUtils.toString(response.getEntity(), "UTF-8");
//			Log.d(TAG,"result:"+result);
//
//			//     if (result.startsWith("{")) {
//			try {
//				mHomeData = new HomeData( new JSONObject(result));
//				return mHomeData;
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//			//}
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		return mHomeData;
//	}
//	@SuppressLint("NewApi")
//	public DeviceData getDeviceData(int projectID)
//	{
//		DeviceData data=null;
//		String result = null;
//		NameValuePair pairusername = new BasicNameValuePair("username", mLoginData.getMsg());
//		NameValuePair pairimei = new BasicNameValuePair("client_key", mLoginData.getMsg());
//		NameValuePair pairtoken = new BasicNameValuePair("token", mLoginData.getMsg());
//		NameValuePair project_id = new BasicNameValuePair("project_id",projectID+"");
//		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
//		pairList.add(pairusername);
//		pairList.add(pairtoken);
//		pairList.add(pairimei);
//		if(projectID >=0)
//		{
//			pairList.add(project_id);
//		}	
//		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		StrictMode.setThreadPolicy(policy);
//		try
//		{
//			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(
//					pairList);
//			HttpPost httpPost = new HttpPost(DEVICEDATA_URL);
//			httpPost.setEntity(requestHttpEntity);
//			HttpClient httpClient = new DefaultHttpClient();
//			HttpResponse response = httpClient.execute(httpPost);
//			result =  EntityUtils.toString(response.getEntity(), "UTF-8");
//			Log.d(TAG,"result:"+result);
//			try {
//				data = new DeviceData( new JSONObject(result));
//				return data;
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		return data;
//	}

//
//	@SuppressLint("NewApi")
//	public ProjectData getProjectData(String keyword)
//	{
//		ProjectData data=null;
//		String result = null;
//		NameValuePair pairusername = new BasicNameValuePair("username", mLoginData.getClient_key());
//		NameValuePair pairimei = new BasicNameValuePair("client_key", mLoginData.getMsg());
//		NameValuePair pairtoken = new BasicNameValuePair("token", mLoginData.getMsg());
//		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
//		pairList.add(pairusername);
//		pairList.add(pairtoken);
//		pairList.add(pairimei);
//		if(keyword != null  && !keyword.equals(""))
//		{
//			NameValuePair keywordpair = new BasicNameValuePair("keyword",keyword);
//			pairList.add(keywordpair);
//		}	
//		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		StrictMode.setThreadPolicy(policy);
//		try
//		{
//			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(
//					pairList);
//			HttpPost httpPost = new HttpPost(PROJECTGET_URL);
//			httpPost.setEntity(requestHttpEntity);
//			HttpClient httpClient = new DefaultHttpClient();
//			HttpResponse response = httpClient.execute(httpPost);
//			result =  EntityUtils.toString(response.getEntity(), "UTF-8");
//			Log.d(TAG,"result:"+result);
//			try {
//				data = new ProjectData( new JSONObject(result));
//				return data;
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		return data;
//	}
	
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
	
//	public DeviceLampcontrolGetData getDeviceLampcontrolGetData(int project_id , String keyword ,int status)
//	{
//		DeviceLampcontrolGetData data=null;
//		NameValuePair project_id_pair = new BasicNameValuePair("project_id",project_id+"");
//		NameValuePair keyword_pair = new BasicNameValuePair("keyword",keyword);
//		NameValuePair status_pair = new BasicNameValuePair("status",status+"");
//		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
//		if(project_id < 0)
//		{
//			pairList.add(project_id_pair);
//		}
//		if(keyword != null)
//		{
//			pairList.add(keyword_pair);
//		}
//		if(status == 0 || 1 == status)
//		{
//			pairList.add(status_pair);
//		}
//		String result = callApiForUrl(DEVICELAMPCONTORLGET_URL,pairList);
//		
//		Gson gson = new Gson();
//		data = gson.fromJson(result, DeviceLampcontrolGetData.class);
//		return data;
//	}
	
}
