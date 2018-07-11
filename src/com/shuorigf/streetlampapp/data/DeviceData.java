package com.shuorigf.streetlampapp.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DeviceData extends ResultCodeData {
	
	private Data data;
	

	public Data getData() {
		return data;
	}


	public void setData(Data data) {
		this.data = data;
	}

	
	
	public class Data{
		private int total_project;
		private int total_network;
		private int total_lamp;
		private List<Project> projects;
		private List<Network> networks;
		private List<LampData> lamps;
		
		
		
		public int getTotal_project() {
			return total_project;
		}

		public void setTotal_project(int total_project) {
			this.total_project = total_project;
		}

		public int getTotal_network() {
			return total_network;
		}

		public void setTotal_network(int total_network) {
			this.total_network = total_network;
		}

		public int getTotal_lamp() {
			return total_lamp;
		}

		public void setTotal_lamp(int total_lamp) {
			this.total_lamp = total_lamp;
		}

		public List<Project> getProjects() {
			return projects;
		}

		public void setProjects(ArrayList<Project> projects) {
			this.projects = projects;
		}

		public List<Network> getNetworks() {
			return networks;
		}

		public void setNetworks(ArrayList<Network> networks) {
			this.networks = networks;
		}

		public List<LampData> getLamps() {
			return lamps;
		}

		public void setLamps(ArrayList<LampData> lamps) {
			this.lamps = lamps;
		}

		public class Project{
			private	String id;
			private String name;
			private int status;
			public String getId() {
				return id;
			}
			public void setId(String id) {
				this.id = id;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public int getStatus() {
				return status;
			}
			public void setStatus(int status) {
				this.status = status;
			}
			
			
		};
		
		public class Network{
			private	int id;
			private String name;
			public int getId() {
				return id;
			}
			public void setId(int id) {
				this.id = id;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			
			
		};
		
		public class LampData implements Serializable{
			
			private static final long serialVersionUID = 1L;
			private String id;
			private int  status;
			private int  isfaulted;
			private double longitude;
			private double latitude;
			public String getId() {
				return id;
			}
			public void setId(String id) {
				this.id = id;
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
			
		}

		
	}

	
}
