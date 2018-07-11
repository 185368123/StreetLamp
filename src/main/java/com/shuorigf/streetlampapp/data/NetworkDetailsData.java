package com.shuorigf.streetlampapp.data;

import java.io.Serializable;



public class NetworkDetailsData extends ResultCodeData {
	
   private Data data;
   

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	
	public class Data implements Serializable{
		private static final long serialVersionUID = 1L;
		private String network_no;
		private String network_name;
		private int status;
		private String section;
		private String simcard;
		private String regpack;
		private double longitude;
		private double latitude;
		private int lampcount;
		private String createtime;
		public String getNetwork_no() {
			return network_no;
		}
		public void setNetwork_no(String network_no) {
			this.network_no = network_no;
		}
		public String getNetwork_name() {
			return network_name;
		}
		public void setNetwork_name(String network_name) {
			this.network_name = network_name;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public String getSection() {
			return section;
		}
		public void setSection(String section) {
			this.section = section;
		}
		public String getSimcard() {
			return simcard;
		}
		public void setSimcard(String simcard) {
			this.simcard = simcard;
		}
		public String getRegpack() {
			return regpack;
		}
		public void setRegpack(String regpack) {
			this.regpack = regpack;
		}
		public double getLongitude() {
			return longitude;
		}
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
		public double getLatitude() {
			return latitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		public int getLampcount() {
			return lampcount;
		}
		public void setLampcount(int lampcount) {
			this.lampcount = lampcount;
		}
		public String getCreatetime() {
			return createtime;
		}
		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}
		
		
	}
		
	
}
