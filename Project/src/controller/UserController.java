package controller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import database.DatabaseConnection;
import model.User;

public class UserController {
	private static String sql;
	private static DatabaseConnection db;
	public UserController() {}
	public int updateUser(User user) throws Exception {
		if(searchByUser(user.getUserName())==null) {	
		sql="UPDATE `labreservationsystem`.`user`\r\n" + 
				"SET\r\n" +  
				"`username` = ?,\r\n" + 
				"`password` = ?,\r\n" + 
				"`position` = ?,\r\n" + 
				"`phone` = ?\r\n" + 
				"WHERE `username` = ?;";
		db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, user.getUserName());
		ps.setString(2, user.getPassword());
		ps.setString(3, user.getPosition());
		ps.setString(4, user.getPhone());
		ps.setString(5, user.getUserName());
		int inserted = ps.executeUpdate();
		conn.close();
		return inserted;
		}
		else
			return 0;
	}
	public int deleteUser(String username) throws Exception {
		sql = "DELETE FROM `labreservationsystem`.`user`\r\n" + 
				"WHERE username = ?;\r\n";
		db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, username);
		int deleted = ps.executeUpdate();
		conn.close();
		return deleted;
	}
	public int addUser(String username, String password, String position, String phone) throws Exception {
		DatabaseConnection db = new DatabaseConnection();
		Connection conn = db.getConnection();
		String sql = "INSERT INTO `user`\r\n" + 
				"(`username`,\r\n" + 
				"`password`,\r\n" + 
				"`position`,\r\n" + 
				"`phone`)\r\n" + 
				"VALUES\r\n" + 
				"(?,?,?,?);";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, username);
		ps.setString(2, password);
		ps.setString(3, position);
		ps.setString(4, phone);
		return ps.executeUpdate();
	}
	public static User searchByUser(String username) throws Exception {
		sql = "SELECT * FROM USER WHERE username = ?;"; 
		db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		if(rs.next())
			return new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
		else
			return null;
	}
	
	public static ArrayList<User> searchByPosition(String position) throws Exception {
		ArrayList<User> users = new ArrayList<User>();
		sql = "SELECT * FROM USER WHERE position = ?;"; 
		db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, position);
		ResultSet rs = ps.executeQuery();
		while(rs.next())
			 users.add(new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
		return users;
	}
	
	public static ArrayList<String> listAllUsername(ArrayList<User> users){
		ArrayList<String> usernames = new ArrayList<String>();
		for (int i = 0; i<users.size();i++)
			usernames.add(users.get(i).getUserName());
		return usernames;
	}
	
}