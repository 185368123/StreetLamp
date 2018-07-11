package com.shuorigf.streetlampapp.data;

import java.io.Serializable;
import java.util.List;

import com.bigkoo.pickerview.model.IPickerViewData;



public class NetworkData extends ResultCodeData {
	
   private Data data;
   

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	
	public class Data {
		private List <Network> networks;

		public List <Network> getNetworks() {
			return networks;
		}

		public void setNetworks(List <Network> networks) {
			this.networks = networks;
		}
		
		
		public class Network implements Serializable,IPickerViewData{
			private static final long serialVersionUID = 1L;
			private	String id;
			private String name;
			private int lamp;
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
			public int getLamp() {
				return lamp;
			}
			public void setLamp(int lamp) {
				this.lamp = lamp;
			}
			@Override
			public String getPickerViewText() {
				return name;
			}
			public int getStatus() {
				return status;
			}
			public void setStatus(int status) {
				this.status = status;
			}
			
		}
	}
	
}
