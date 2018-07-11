package com.shuorigf.streetlampapp.data;

import java.util.List;

import com.bigkoo.pickerview.model.IPickerViewData;



public class UpdateHistoryData extends ResultCodeData {
	
   private Data data;
   

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	
	public class Data {
		private int total;
		private List <UpdateHistory> list;
		
		
		public int getTotal() {
			return total;
		}


		public void setTotal(int total) {
			this.total = total;
		}


		public List<UpdateHistory> getList() {
			return list;
		}


		public void setList(List<UpdateHistory> list) {
			this.list = list;
		}


		public class UpdateHistory {
			private	String id;
			private String lampid;
			private String syscurrent;
			private String sysvoltage;
			private String temper;
			private String lampcurrent;
			private String lampvoltage;
			private String lamppower;
			private String solarcurrent;
			private String solarvoltage;
			private String solarpower;
			private String electricleft;
			private String battvoltage;
			private String batttemper;
			private String updatetime;
			public String getId() {
				return id;
			}
			public void setId(String id) {
				this.id = id;
			}
			public String getLampid() {
				return lampid;
			}
			public void setLampid(String lampid) {
				this.lampid = lampid;
			}
			public String getSyscurrent() {
				return syscurrent;
			}
			public void setSyscurrent(String syscurrent) {
				this.syscurrent = syscurrent;
			}
			public String getSysvoltage() {
				return sysvoltage;
			}
			public void setSysvoltage(String sysvoltage) {
				this.sysvoltage = sysvoltage;
			}
			public String getTemper() {
				return temper;
			}
			public void setTemper(String temper) {
				this.temper = temper;
			}
			
			
			public String getLampcurrent() {
				return lampcurrent;
			}
			public void setLampcurrent(String lampcurrent) {
				this.lampcurrent = lampcurrent;
			}
			public String getLampvoltage() {
				return lampvoltage;
			}
			public void setLampvoltage(String lampvoltage) {
				this.lampvoltage = lampvoltage;
			}
			public String getLamppower() {
				return lamppower;
			}
			public void setLamppower(String lamppower) {
				this.lamppower = lamppower;
			}
			public String getSolarcurrent() {
				return solarcurrent;
			}
			public void setSolarcurrent(String solarcurrent) {
				this.solarcurrent = solarcurrent;
			}
			public String getSolarvoltage() {
				return solarvoltage;
			}
			public void setSolarvoltage(String solarvoltage) {
				this.solarvoltage = solarvoltage;
			}
			public String getSolarpower() {
				return solarpower;
			}
			public void setSolarpower(String solarpower) {
				this.solarpower = solarpower;
			}
			
			public String getElectricleft() {
				return electricleft;
			}
			public void setElectricleft(String electricleft) {
				this.electricleft = electricleft;
			}
			public String getBattvoltage() {
				return battvoltage;
			}
			public void setBattvoltage(String battvoltage) {
				this.battvoltage = battvoltage;
			}
			public String getBatttemper() {
				return batttemper;
			}
			public void setBatttemper(String batttemper) {
				this.batttemper = batttemper;
			}
			public String getUpdatetime() {
				return updatetime;
			}
			public void setUpdatetime(String updatetime) {
				this.updatetime = updatetime;
			}
			
		}

		
	}
	
}
