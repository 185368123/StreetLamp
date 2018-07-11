package com.shuorigf.streetlampapp.data;

import java.util.List;

public class ReportData extends ResultCodeData {
	private Data data;

	public Data getDate() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data {
		private Report report1;
		private Report report2;

		public Report getReport1() {
			return report1;
		}

		public void setReport1(Report report1) {
			this.report1 = report1;
		}

		public Report getReport2() {
			return report2;
		}

		public void setReport2(Report report2) {
			this.report2 = report2;
		}

		public class Report {
			private List<String> categories;
			private List<Series> series;

			public List<String> getCategories() {
				return categories;
			}

			public void setCategories(List<String> categories) {
				this.categories = categories;
			}

			public List<Series> getSeries() {
				return series;
			}

			public void setSeries(List<Series> series) {
				this.series = series;
			}

			public class Series {
				private List<Float> data;
				private String name;

				public List<Float> getData() {
					return data;
				}

				public void setData(List<Float> data) {
					this.data = data;
				}

				public String getName() {
					return name;
				}

				public void setName(String name) {
					this.name = name;
				}

			}

		}

	}

}
