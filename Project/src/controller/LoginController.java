package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.DatabaseConnection;
import model.User;

public class LoginController {
	private DatabaseConnection db;
	private Connection conn; 
	private User user = new User();
	public LoginController() {}
	public boolean doLogin(String username, char[] passwordChar) throws Exception {
		db = new DatabaseConnection();
		conn = db.getConnection();
		String password = "";
		for(char a: passwordChar)
			password+=a;
		String sql;
		sql = "SELECT * FROM user WHERE username=? AND password=?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, username);
		ps.setString(2, password);
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()) { 
			setUser(new User(rs.getString(1),rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5)));
			conn.close();
			return true;
		}
		else {
			conn.close();
			return false;
		}
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
