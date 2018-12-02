package model;


public class User{
	private String userID;
	private String userName;
	private String password;
	private String position;
	private String phone;
	public User() {}
	public User(String userID, String userName, String password, String position, String phone) {
		this.userID = userID;
		this.userName = userName;
		this.password = password;
		this.position = position;
		this.phone = phone;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
