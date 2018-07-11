package com.shuorigf.streetlampapp.data;

import java.util.ArrayList;
import java.util.List;

public class DeviceLampcontrolGetData {
	public String status="";
	public String msg ="";
	public  DeviceLampcontrolDatas data=new DeviceLampcontrolDatas();
	
	public class DeviceLampcontrolDatas{
		public List<DeviceLampcontrolData>lamps = new ArrayList<DeviceLampcontrolData>();
	}
	public class DeviceLampcontrolData{
		public int id;
		public int number;
		public int status;
		public int isfaulted;
		public String network_name="";
		public int lamppower;
		public int electricSOC;
		public String chargestage="";
		public String updatetime="";
	}
	
}
