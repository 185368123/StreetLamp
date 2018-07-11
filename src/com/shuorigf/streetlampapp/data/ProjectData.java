package com.shuorigf.streetlampapp.data;

import java.util.List;

import com.bigkoo.pickerview.model.IPickerViewData;



public class ProjectData extends ResultCodeData {
	
   private Data data;
   

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	
	public class Data {
		private List <Project> projects;

		public List <Project> getProjects() {
			return projects;
		}

		public void setProjects(List <Project> projects) {
			this.projects = projects;
		}
		
		
		public class Project implements IPickerViewData{
			private	String id;
			private String name;
			private String tag;
			private int network;
			public String getId() {
				return id;
			}
			public String getName() {
				return name;
			}
			public String getTag() {
				return tag;
			}
			public int getNetwork() {
				return network;
			}
			public void setId(String id) {
				this.id = id;
			}
			public void setName(String name) {
				this.name = name;
			}
			public void setTag(String tag) {
				this.tag = tag;
			}
			public void setNetwork(int network) {
				this.network = network;
			}
			@Override
			public String getPickerViewText() {
				return name;
			}
			
		}
	}
	
}
