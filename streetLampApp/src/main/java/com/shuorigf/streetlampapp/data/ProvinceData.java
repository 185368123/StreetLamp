package com.shuorigf.streetlampapp.data;

import java.util.List;

import com.bigkoo.pickerview.model.IPickerViewData;


public class ProvinceData extends ResultCodeData{
	private Data data;
	
	public Data getData() {
		return data;
	}


	public void setData(Data data) {
		this.data = data;
	}
	public class Data {
		private List<Province> provinces;
		
		
		public List<Province> getProvinces() {
			return provinces;
		}


		public void setProvinces(List<Province> provinces) {
			this.provinces = provinces;
		}


		public class Province implements IPickerViewData{
			private String id;
			private String name;
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
			@Override
			public String getPickerViewText() {
				return name;
			}
			
			
		}
		
	}
	
}
