package com.shuorigf.streetlampapp.data;

public class LoginData extends ResultCodeData {
	private String username;
	private String client_key;
	private String token;
	private Data data;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getClient_key() {
		return client_key;
	}

	public void setClient_key(String client_key) {
		this.client_key = client_key;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data {
		private String token;
		private String avatar;
		private String company;
		private String realname;
		private int role;

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public String getCompany() {
			return company;
		}

		public void setCompany(String company) {
			this.company = company;
		}

		public String getRealname() {
			return realname;
		}

		public void setRealname(String realname) {
			this.realname = realname;
		}

		public int getRole() {
			return role;
		}

		public void setRole(int role) {
			this.role = role;
		}

		@Override
		public String toString() {
			return "Data [token=" + token + ", avatar=" + avatar + ", company="
					+ company + ", realname=" + realname + ", role=" + role
					+ "]";
		}

	}

	@Override
	public String toString() {
		return "LoginData [username=" + username + ", client_key=" + client_key
				+ ", token=" + token + ", data=" + data + "]";
	}

}
