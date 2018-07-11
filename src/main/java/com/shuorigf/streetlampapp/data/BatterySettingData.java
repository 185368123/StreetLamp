package com.shuorigf.streetlampapp.data;



public class BatterySettingData extends ResultCodeData{
	private Data data;
	
	public Data getData() {
		return data;
	}


	public void setData(Data data) {
		this.data = data;
	}
	public class Data {
		private String capacity;
		private String batterytype;
		private String vovervoltage;
		private String vlimitedcharge;
		private String vbalancecharge;
		private String vpromotecharge;
		private String vfloatingcharge;
		private String vpromoterecover;
		private String voverdischargerecover;
		private String vundervoltagewarn;
		private String voverdischarge;
		private String balancechargetime;
		private String promotechargetime;
		private String balanceinterval;
		private String tempcompensation;
		private String tempcompmax;
		private String tempcompmin;
		private String turnfloatingcurrent;
		public String getCapacity() {
			return capacity;
		}
		public void setCapacity(String capacity) {
			this.capacity = capacity;
		}
		public String getBatterytype() {
			return batterytype;
		}
		public void setBatterytype(String batterytype) {
			this.batterytype = batterytype;
		}
		public String getVovervoltage() {
			return vovervoltage;
		}
		public void setVovervoltage(String vovervoltage) {
			this.vovervoltage = vovervoltage;
		}
		public String getVlimitedcharge() {
			return vlimitedcharge;
		}
		public void setVlimitedcharge(String vlimitedcharge) {
			this.vlimitedcharge = vlimitedcharge;
		}
		public String getVbalancecharge() {
			return vbalancecharge;
		}
		public void setVbalancecharge(String vbalancecharge) {
			this.vbalancecharge = vbalancecharge;
		}
		public String getVpromotecharge() {
			return vpromotecharge;
		}
		public void setVpromotecharge(String vpromotecharge) {
			this.vpromotecharge = vpromotecharge;
		}
		public String getVfloatingcharge() {
			return vfloatingcharge;
		}
		public void setVfloatingcharge(String vfloatingcharge) {
			this.vfloatingcharge = vfloatingcharge;
		}
		public String getVpromoterecover() {
			return vpromoterecover;
		}
		public void setVpromoterecover(String vpromoterecover) {
			this.vpromoterecover = vpromoterecover;
		}
		public String getVoverdischargerecover() {
			return voverdischargerecover;
		}
		public void setVoverdischargerecover(String voverdischargerecover) {
			this.voverdischargerecover = voverdischargerecover;
		}
		public String getVundervoltagewarn() {
			return vundervoltagewarn;
		}
		public void setVundervoltagewarn(String vundervoltagewarn) {
			this.vundervoltagewarn = vundervoltagewarn;
		}
		public String getVoverdischarge() {
			return voverdischarge;
		}
		public void setVoverdischarge(String voverdischarge) {
			this.voverdischarge = voverdischarge;
		}
		public String getBalancechargetime() {
			return balancechargetime;
		}
		public void setBalancechargetime(String balancechargetime) {
			this.balancechargetime = balancechargetime;
		}
		public String getPromotechargetime() {
			return promotechargetime;
		}
		public void setPromotechargetime(String promotechargetime) {
			this.promotechargetime = promotechargetime;
		}
		public String getBalanceinterval() {
			return balanceinterval;
		}
		public void setBalanceinterval(String balanceinterval) {
			this.balanceinterval = balanceinterval;
		}
		public String getTempcompensation() {
			return tempcompensation;
		}
		public void setTempcompensation(String tempcompensation) {
			this.tempcompensation = tempcompensation;
		}
		public String getTempcompmax() {
			return tempcompmax;
		}
		public void setTempcompmax(String tempcompmax) {
			this.tempcompmax = tempcompmax;
		}
		public String getTempcompmin() {
			return tempcompmin;
		}
		public void setTempcompmin(String tempcompmin) {
			this.tempcompmin = tempcompmin;
		}
		public String getTurnfloatingcurrent() {
			return turnfloatingcurrent;
		}
		public void setTurnfloatingcurrent(String turnfloatingcurrent) {
			this.turnfloatingcurrent = turnfloatingcurrent;
		}
		
		
		
	}
	
	
}
