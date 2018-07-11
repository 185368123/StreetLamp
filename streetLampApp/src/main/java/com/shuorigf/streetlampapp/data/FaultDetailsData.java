package com.shuorigf.streetlampapp.data;



public class FaultDetailsData extends ResultCodeData {
	
   private Data data;
   

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	public class Data {
		private String project_name;
		private String network_no;
		private String lamp_no;
		private String location;
		private String alarm_event;
		private String alarm_time;
		private String network_time;
		private String lampid;
		private int status;
		public String getProject_name() {
			return project_name;
		}
		public void setProject_name(String project_name) {
			this.project_name = project_name;
		}
		public String getNetwork_no() {
			return network_no;
		}
		public void setNetwork_no(String network_no) {
			this.network_no = network_no;
		}
		public String getLamp_no() {
			return lamp_no;
		}
		public void setLamp_no(String lamp_no) {
			this.lamp_no = lamp_no;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getAlarm_event() {
			return alarm_event;
		}
		public void setAlarm_event(String alarm_event) {
			this.alarm_event = alarm_event;
		}
		public String getAlarm_time() {
			return alarm_time;
		}
		public void setAlarm_time(String alarm_time) {
			this.alarm_time = alarm_time;
		}
		public String getNetwork_time() {
			return network_time;
		}
		public void setNetwork_time(String network_time) {
			this.network_time = network_time;
		}
		
		public String getLampid() {
			return lampid;
		}
		public void setLampid(String lampid) {
			this.lampid = lampid;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		
		
	}
		
	
}
