package com.shuorigf.streetlampapp.data;

import java.io.Serializable;

public class ProjectDetailsData extends ResultCodeData {

	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data implements Serializable {
		private static final long serialVersionUID = 1L;
		private String project_name;
		private String project_no;
		private String customer;
		private String company;
		private String province_id;
		private String province;
		private String address;
		private int install_num;
		private int network_num;
		private int patrol_time;

		public String getProject_name() {
			return project_name;
		}

		public String getProject_no() {
			return project_no;
		}

		public String getCustomer() {
			return customer;
		}

		public String getCompany() {
			return company;
		}

		public String getProvince_id() {
			return province_id;
		}

		public String getProvince() {
			return province;
		}

		public String getAddress() {
			return address;
		}

		public int getInstall_num() {
			return install_num;
		}

		public int getNetwork_num() {
			return network_num;
		}

		public int getPatrol_time() {
			return patrol_time;
		}

		public void setProject_name(String project_name) {
			this.project_name = project_name;
		}

		public void setProject_no(String project_no) {
			this.project_no = project_no;
		}

		public void setCustomer(String customer) {
			this.customer = customer;
		}

		public void setCompany(String company) {
			this.company = company;
		}

		public void setProvince_id(String province_id) {
			this.province_id = province_id;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public void setInstall_num(int install_num) {
			this.install_num = install_num;
		}

		public void setNetwork_num(int network_num) {
			this.network_num = network_num;
		}

		public void setPatrol_time(int patrol_time) {
			this.patrol_time = patrol_time;
		}

	}

}
