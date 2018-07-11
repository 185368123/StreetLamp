package com.shuorigf.streetlampapp.data;

import java.util.List;



public class FaultData extends ResultCodeData {
	
   private Data data;
   

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	
	public class Data {
		private List <Fault> alarm;
		private int total;

		
		public List<Fault> getAlarm() {
			return alarm;
		}


		public void setAlarm(List<Fault> alarm) {
			this.alarm = alarm;
		}

		public int getTotal() {
			return total;
		}


		public void setTotal(int total) {
			this.total = total;
		}

		public class Fault{
			private	String id;
			private	int lampid;
			private	int alarmtype;
			private String stralarmtype;
			private int status;
			private String updatetime;
			private String number;
			private String project;
			private String network;
			public String getId() {
				return id;
			}
			public void setId(String id) {
				this.id = id;
			}
			public int getLampid() {
				return lampid;
			}
			public void setLampid(int lampid) {
				this.lampid = lampid;
			}
			public int getAlarmtype() {
				return alarmtype;
			}
			public void setAlarmtype(int alarmtype) {
				this.alarmtype = alarmtype;
			}
			public String getStralarmtype() {
				return stralarmtype;
			}
			public void setStralarmtype(String stralarmtype) {
				this.stralarmtype = stralarmtype;
			}
			public int getStatus() {
				return status;
			}
			public void setStatus(int status) {
				this.status = status;
			}
			public String getUpdatetime() {
				return updatetime;
			}
			public void setUpdatetime(String updatetime) {
				this.updatetime = updatetime;
			}
			public String getNumber() {
				return number;
			}
			public void setNumber(String number) {
				this.number = number;
			}
			public String getProject() {
				return project;
			}
			public void setProject(String project) {
				this.project = project;
			}
			public String getNetwork() {
				return network;
			}
			public void setNetwork(String network) {
				this.network = network;
			}
			
			
			
		}
	}
	
}
