package com.shuorigf.streetlampapp.data;

import org.json.JSONException;
import org.json.JSONObject;

/*
online_rate 	float 	在线率 	Y
lighting_rate 	float 	亮灯率 	Y
failure_rate 	float 	故障率 	Y
total_power 	float 	总发电量(KWH) 	Y
total_install 	float 	总装机容量(KW) 	Y
total_lamp 	int 	总安装灯数量 	Y
total_network 	int 	总网络数量 	Y
coal_saving 	float 	节约标准煤(T) 	Y
co2_emission 	float 	累计减少CO2排放量(KG) 	Y
so2_emission 	float 	累计减少SO2排放量(KG) 	Y
 * 
 * */
public class HomeData extends ResultCodeData{
	private Data data;
	
	public Data getData() {
		return data;
	}


	public void setData(Data data) {
		this.data = data;
	}
	public class Data {
		private float online_rate;
		private float lighting_rate;
		private float failure_rate;
		private float total_power;
		private float total_install;
		private int total_lamp;
		private int total_network;
		private float coal_saving;
		private float co2_emission;
		private float so2_emission;
		
		
		
		public float getOnline_rate() {
			return online_rate;
		}
		public float getLighting_rate() {
			return lighting_rate;
		}
		public float getFailure_rate() {
			return failure_rate;
		}
		public float getTotal_power() {
			return total_power;
		}
		public float getTotal_install() {
			return total_install;
		}
		public int getTotal_lamp() {
			return total_lamp;
		}
		public int getTotal_network() {
			return total_network;
		}
		public float getCoal_saving() {
			return coal_saving;
		}
		public float getCo2_emission() {
			return co2_emission;
		}
		public float getSo2_emission() {
			return so2_emission;
		}
		
		
	}
	
	
}
