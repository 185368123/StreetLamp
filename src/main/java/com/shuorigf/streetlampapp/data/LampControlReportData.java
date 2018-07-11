package com.shuorigf.streetlampapp.data;

import java.io.Serializable;

import com.shuorigf.streetlampapp.data.LampControlData.Data.LampControl;

public class LampControlReportData implements Serializable{
	private static final long serialVersionUID = 1L;
	private int color;
	private	String id;
	private String number;
	
	
	
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
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
	
	

}
