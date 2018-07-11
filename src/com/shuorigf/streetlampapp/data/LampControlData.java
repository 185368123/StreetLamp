package com.shuorigf.streetlampapp.data;

import java.io.Serializable;
import java.util.List;




public class LampControlData extends ResultCodeData {
	
   private Data data;
   

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	
	public class Data {
		private List <LampControl> lamps;
		
		public List <LampControl> getLamps() {
			return lamps;
		}

		public void setLamps(List <LampControl> lamps) {
			this.lamps = lamps;
		}
		
		public class LampControl implements Serializable{
			private static final long serialVersionUID = 1L;
			private	String id;
			private String number;
			private	int status;
			private int isfaulted;
			private String project_name;
			private String network_name;
			private float lamppower;
			private float electricSOC;
			private float battvoltage;
			private String chargestage;
			private String updatetime;
			public String getId() {
				return id;
			}
			public void setId(String id) {
				this.id = id;
			}
			public String getNumber() {
				return number;
			}
			public void setNumber(String number) {
				this.number = number;
			}
			public int getStatus() {
				return status;
			}
			public void setStatus(int status) {
				this.status = status;
			}
			public int getIsfaulted() {
				return isfaulted;
			}
			public void setIsfaulted(int isfaulted) {
				this.isfaulted = isfaulted;
			}
			
			public String getProject_name() {
				return project_name;
			}
			public void setProject_name(String project_name) {
				this.project_name = project_name;
			}
			public String getNetwork_name() {
				return network_name;
			}
			public void setNetwork_name(String network_name) {
				this.network_name = network_name;
			}
			public float getLamppower() {
				return lamppower;
			}
			public void setLamppower(float lamppower) {
				this.lamppower = lamppower;
			}
			public float getElectricSOC() {
				return electricSOC;
			}
			public void setElectricSOC(float electricSOC) {
				this.electricSOC = electricSOC;
			}
			public String getChargestage() {
				return chargestage;
			}
			public void setChargestage(String chargestage) {
				this.chargestage = chargestage;
			}
			public String getUpdatetime() {
				return updatetime;
			}
			public void setUpdatetime(String updatetime) {
				this.updatetime = updatetime;
			}
			public float getBattvoltage() {
				return battvoltage;
			}
			public void setBattvoltage(float battvoltage) {
				this.battvoltage = battvoltage;
			}
			
			
		}

		
	}
	
}
