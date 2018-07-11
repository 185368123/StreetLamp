package com.shuorigf.streetlampapp.data;

import java.io.Serializable;



public class LampControlDetailsData extends ResultCodeData {
	
   private Data data;
   

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	
	public class Data implements Serializable{
		private static final long serialVersionUID = 1L;
		private String updatetime;
		private	int status;
		private int isfaulted;
		private String lamp_no;
		private String network_no;
		private String network_id;
		private String project_id;
		private String project_name;
		private String address;
		private double longitude;
		private double latitude;
		private String section;
		private String location;
		private float boardpower;
		private int temper;
		private float sysvoltage;
		private float syscurrent;
		private int lampstatus;
		private int lighteness;
		private float lamppower;
		private int nightlength;
		private int lighttime;
		private float lampvoltage;
		private float lampcurrent;
		private float solarvoltage;
		private float solarpower;
		private float solarcurrent;
		private int solartime;
		private int chargetime;
		private float totalchargeah;
		private float battvoltage;
		private float batttemper;
		private float electricleft;
		private float voltagedaymin;
		private float voltagedaymax;
		private int battstatus;
		private int chargestage;
		private int overtimes;
		private int rundays;
		private int fulltimes;
		private float totalgeneration;
		private float totalconsumption;
		private int electricSOC;
		public String getUpdatetime() {
			return updatetime;
		}
		public void setUpdatetime(String updatetime) {
			this.updatetime = updatetime;
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
		public String getLamp_no() {
			return lamp_no;
		}
		public void setLamp_no(String lamp_no) {
			this.lamp_no = lamp_no;
		}
		public String getNetwork_no() {
			return network_no;
		}
		public void setNetwork_no(String network_no) {
			this.network_no = network_no;
		}
		
		public String getProject_id() {
			return project_id;
		}
		public void setProject_id(String project_id) {
			this.project_id = project_id;
		}
		public String getProject_name() {
			return project_name;
		}
		public void setProject_name(String project_name) {
			this.project_name = project_name;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
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
		public String getSection() {
			return section;
		}
		public void setSection(String section) {
			this.section = section;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public float getBoardpower() {
			return boardpower;
		}
		public void setBoardpower(float boardpower) {
			this.boardpower = boardpower;
		}
		public int getTemper() {
			return temper;
		}
		public void setTemper(int temper) {
			this.temper = temper;
		}
		public float getSysvoltage() {
			return sysvoltage;
		}
		public void setSysvoltage(float sysvoltage) {
			this.sysvoltage = sysvoltage;
		}
		public float getSyscurrent() {
			return syscurrent;
		}
		public void setSyscurrent(float syscurrent) {
			this.syscurrent = syscurrent;
		}
		public int getLampstatus() {
			return lampstatus;
		}
		public void setLampstatus(int lampstatus) {
			this.lampstatus = lampstatus;
		}
		public int getLighteness() {
			return lighteness;
		}
		public void setLighteness(int lighteness) {
			this.lighteness = lighteness;
		}
		public float getLamppower() {
			return lamppower;
		}
		public void setLamppower(float lamppower) {
			this.lamppower = lamppower;
		}
		public float getLampvoltage() {
			return lampvoltage;
		}
		public void setLampvoltage(float lampvoltage) {
			this.lampvoltage = lampvoltage;
		}
		public float getLampcurrent() {
			return lampcurrent;
		}
		public void setLampcurrent(float lampcurrent) {
			this.lampcurrent = lampcurrent;
		}
		public float getSolarvoltage() {
			return solarvoltage;
		}
		public void setSolarvoltage(float solarvoltage) {
			this.solarvoltage = solarvoltage;
		}
		public float getSolarpower() {
			return solarpower;
		}
		public void setSolarpower(float solarpower) {
			this.solarpower = solarpower;
		}
		public float getSolarcurrent() {
			return solarcurrent;
		}
		public void setSolarcurrent(float solarcurrent) {
			this.solarcurrent = solarcurrent;
		}
		public float getTotalchargeah() {
			return totalchargeah;
		}
		public void setTotalchargeah(float totalchargeah) {
			this.totalchargeah = totalchargeah;
		}
		public float getBattvoltage() {
			return battvoltage;
		}
		public void setBattvoltage(float battvoltage) {
			this.battvoltage = battvoltage;
		}
		public float getBatttemper() {
			return batttemper;
		}
		public void setBatttemper(float batttemper) {
			this.batttemper = batttemper;
		}
		public float getElectricleft() {
			return electricleft;
		}
		public void setElectricleft(float electricleft) {
			this.electricleft = electricleft;
		}
		public float getVoltagedaymin() {
			return voltagedaymin;
		}
		public void setVoltagedaymin(float voltagedaymin) {
			this.voltagedaymin = voltagedaymin;
		}
		public float getVoltagedaymax() {
			return voltagedaymax;
		}
		public void setVoltagedaymax(float voltagedaymax) {
			this.voltagedaymax = voltagedaymax;
		}
		public int getBattstatus() {
			return battstatus;
		}
		public void setBattstatus(int battstatus) {
			this.battstatus = battstatus;
		}
		public int getChargestage() {
			return chargestage;
		}
		public void setChargestage(int chargestage) {
			this.chargestage = chargestage;
		}
		public int getOvertimes() {
			return overtimes;
		}
		public void setOvertimes(int overtimes) {
			this.overtimes = overtimes;
		}
		public int getRundays() {
			return rundays;
		}
		public void setRundays(int rundays) {
			this.rundays = rundays;
		}
		public int getFulltimes() {
			return fulltimes;
		}
		public void setFulltimes(int fulltimes) {
			this.fulltimes = fulltimes;
		}
		public float getTotalgeneration() {
			return totalgeneration;
		}
		public void setTotalgeneration(float totalgeneration) {
			this.totalgeneration = totalgeneration;
		}
		public float getTotalconsumption() {
			return totalconsumption;
		}
		public void setTotalconsumption(float totalconsumption) {
			this.totalconsumption = totalconsumption;
		}
		public int getNightlength() {
			return nightlength;
		}
		public void setNightlength(int nightlength) {
			this.nightlength = nightlength;
		}
		public int getLighttime() {
			return lighttime;
		}
		public void setLighttime(int lighttime) {
			this.lighttime = lighttime;
		}
		public int getSolartime() {
			return solartime;
		}
		public void setSolartime(int solartime) {
			this.solartime = solartime;
		}
		public int getChargetime() {
			return chargetime;
		}
		public void setChargetime(int chargetime) {
			this.chargetime = chargetime;
		}
		public int getElectricSOC() {
			return electricSOC;
		}
		public void setElectricSOC(int electricSOC) {
			this.electricSOC = electricSOC;
		}
		public String getNetwork_id() {
			return network_id;
		}
		public void setNetwork_id(String network_id) {
			this.network_id = network_id;
		}
		
		
		
	}
		
	
}
