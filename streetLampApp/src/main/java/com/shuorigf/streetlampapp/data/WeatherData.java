package com.shuorigf.streetlampapp.data;

import java.util.List;



public class WeatherData {
	public static final String SUCCESS_CODE="0";
	private String error;
	private String status;
	private List<Result> results;  
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	
	 public String getError() {
		return error;
	}



	public void setError(String error) {
		this.error = error;
	}



	public List<Result> getResults() {
		return results;
	}



	public void setResults(List<Result> results) {
		this.results = results;
	}



	public static String getSuccessCode() {
		return SUCCESS_CODE;
	}



	public class Result{  
		 private List<Weather> weather_data;  
	     private int pm25;  
		 
	       
	       public List<Weather> getWeather_data() {
			return weather_data;
		}



		public void setWeather_data(List<Weather> weather_data) {
			this.weather_data = weather_data;
		}



		public int getPm25() {
			return pm25;
		}



		public void setPm25(int pm25) {
			this.pm25 = pm25;
		}



		public class Weather{  
	    	   private String date;  
	    	   private String dayPictureUrl;  
	    	   private String nightPictureUrl;  
	    	   private String temperature;  
	    	   private String weather;  
	    	   private String wind;
			public String getDate() {
				return date;
			}
			public void setDate(String date) {
				this.date = date;
			}
			public String getDayPictureUrl() {
				return dayPictureUrl;
			}
			public void setDayPictureUrl(String dayPictureUrl) {
				this.dayPictureUrl = dayPictureUrl;
			}
			public String getNightPictureUrl() {
				return nightPictureUrl;
			}
			public void setNightPictureUrl(String nightPictureUrl) {
				this.nightPictureUrl = nightPictureUrl;
			}
			public String getTemperature() {
				return temperature;
			}
			public void setTemperature(String temperature) {
				this.temperature = temperature;
			}
			public String getWeather() {
				return weather;
			}
			public void setWeather(String weather) {
				this.weather = weather;
			}
			public String getWind() {
				return wind;
			}
			public void setWind(String wind) {
				this.wind = wind;
			}  
	    	   
	    	   
	       }  
	    }  
}
