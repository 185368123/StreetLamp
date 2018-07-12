package com.shuorigf.streetlampapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;



public class FaultData extends ResultCodeData {


	/**
	 * data : {"list":{"history_list":[{"id":"368344","lampid":"30459","alarmtype":"128","stralarmtype":"负载开路","status":"1","number":"000002","project":"办公室LoRa测试","network":"285212794","address":"1100007a","updatetime":"2018-06-25 19:31:43","statusStr":"已处理"},{"id":"368343","lampid":"30458","alarmtype":"128","stralarmtype":"负载开路","status":"0","number":"000001","project":"办公室LoRa测试","network":"285212796","address":"1100007c","updatetime":"2018-06-25 19:31:43","statusStr":"未处理"}],"total":1,"fault":"1"}}
	 */

	private DataBean data;

	public DataBean getData() {
		return data;
	}

	public void setData(DataBean data) {
		this.data = data;
	}

	public static class DataBean {
		/**
		 * list : {"history_list":[{"id":"368344","lampid":"30459","alarmtype":"128","stralarmtype":"负载开路","status":"1","number":"000002","project":"办公室LoRa测试","network":"285212794","address":"1100007a","updatetime":"2018-06-25 19:31:43","statusStr":"已处理"},{"id":"368343","lampid":"30458","alarmtype":"128","stralarmtype":"负载开路","status":"0","number":"000001","project":"办公室LoRa测试","network":"285212796","address":"1100007c","updatetime":"2018-06-25 19:31:43","statusStr":"未处理"}],"total":1,"fault":"1"}
		 */

		private ListBean list;

		public ListBean getList() {
			return list;
		}

		public void setList(ListBean list) {
			this.list = list;
		}

		public static class ListBean {
			/**
			 * history_list : [{"id":"368344","lampid":"30459","alarmtype":"128","stralarmtype":"负载开路","status":"1","number":"000002","project":"办公室LoRa测试","network":"285212794","address":"1100007a","updatetime":"2018-06-25 19:31:43","statusStr":"已处理"},{"id":"368343","lampid":"30458","alarmtype":"128","stralarmtype":"负载开路","status":"0","number":"000001","project":"办公室LoRa测试","network":"285212796","address":"1100007c","updatetime":"2018-06-25 19:31:43","statusStr":"未处理"}]
			 * total : 1
			 * fault : 1
			 */

			private int total;
			private String fault;
			private List<HistoryListBean> history_list;

			public int getTotal() {
				return total;
			}

			public void setTotal(int total) {
				this.total = total;
			}

			public String getFault() {
				return fault;
			}

			public void setFault(String fault) {
				this.fault = fault;
			}

			public List<HistoryListBean> getHistory_list() {
				return history_list;
			}

			public void setHistory_list(List<HistoryListBean> history_list) {
				this.history_list = history_list;
			}

			public static class HistoryListBean {
				/**
				 * id : 368344
				 * lampid : 30459
				 * alarmtype : 128
				 * stralarmtype : 负载开路
				 * status : 1
				 * number : 000002
				 * project : 办公室LoRa测试
				 * network : 285212794
				 * address : 1100007a
				 * updatetime : 2018-06-25 19:31:43
				 * statusStr : 已处理
				 */

				private String id;
				private String lampid;
				private String alarmtype;
				private String stralarmtype;
				@SerializedName("status")
				private String statusX;
				private String number;
				private String project;
				private String network;
				private String address;
				private String updatetime;
				private String statusStr;

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

				public String getAlarmtype() {
					return alarmtype;
				}

				public void setAlarmtype(String alarmtype) {
					this.alarmtype = alarmtype;
				}

				public String getStralarmtype() {
					return stralarmtype;
				}

				public void setStralarmtype(String stralarmtype) {
					this.stralarmtype = stralarmtype;
				}

				public String getStatusX() {
					return statusX;
				}

				public void setStatusX(String statusX) {
					this.statusX = statusX;
				}

				public String getNumber() {
					return number;
				}

				public void setNumber(String number) {
					this.number = number;
				}

				public String getProject() {
					return project;
				}

				public void setProject(String project) {
					this.project = project;
				}

				public String getNetwork() {
					return network;
				}

				public void setNetwork(String network) {
					this.network = network;
				}

				public String getAddress() {
					return address;
				}

				public void setAddress(String address) {
					this.address = address;
				}

				public String getUpdatetime() {
					return updatetime;
				}

				public void setUpdatetime(String updatetime) {
					this.updatetime = updatetime;
				}

				public String getStatusStr() {
					return statusStr;
				}

				public void setStatusStr(String statusStr) {
					this.statusStr = statusStr;
				}
			}
		}
	}
}
