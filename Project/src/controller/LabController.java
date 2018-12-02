package controller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import database.DatabaseConnection;
import model.Lab;

public class LabController {
	private static String sql;
	private static DatabaseConnection db;
	public LabController() {}
	
	public static ArrayList<Lab> allLab () throws Exception {
		ArrayList<Lab> labs = new ArrayList<Lab>();
		labs.clear();
		sql = "SELECT * FROM lab"
				+ "ORDER BY labName;";
		db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next())
			labs.add(new Lab(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4), rs.getString(5), rs.getString(6)));
		conn.close();
		return labs;
	}
	
	public static ArrayList<String> allLabName () throws Exception {
		ArrayList<String> labs = new ArrayList<String>();
		labs.clear();
		sql = "SELECT * FROM lab\r\n" + 
				"ORDER BY labName";
		db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next())
			labs.add(rs.getString(2));
		conn.close();
		return labs;
	}

	public int deleteLab(Lab lab) throws Exception {//Not recommended to delete lab, update the lab instead
		sql = "DELETE FROM lab WHERE labID = ?;";
		db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, lab.getId());
		int deleted = ps.executeUpdate();
		conn.close();
		return deleted;
	}
	
	public Lab searchLabByAbbrev(String labAbbrev) throws Exception {
		sql = "SELECT labID,\r\n" + 
				"    labName,\r\n" + 
				"    labAbbrev,\r\n" + 
				"    labLocation,\r\n" + 
				"    staffID\r\n" + 
				"FROM lab\r\n" + 
				"WHERE labAbbrev = ?;"; 
		db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, labAbbrev);
		ResultSet rs = ps.executeQuery();
		if(rs.next())
			return new Lab(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
		else
			return null;
	}
	
	public static Lab searchLabByName(String labName) throws Exception {
		sql = "SELECT labID,\r\n" + 
				"    labName,\r\n" + 
				"    labAbbrev,\r\n" + 
				"    labLocation,\r\n" + 
				"    staffID,\r\n" +
				"    labType\r\n" +
				"FROM lab\r\n" + 
				"WHERE labName = ?;"; 
		db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, labName);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) 
			return new Lab(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
		else
			return null;
	}
	
	
	public ArrayList<Lab> searchLabByStaffID (String staffID) throws Exception {
		ArrayList<Lab> labs = new ArrayList<Lab>();
		sql = "SELECT labID,\r\n" + 
				"    labName,\r\n" + 
				"    labAbbrev,\r\n" + 
				"    labLocation,\r\n" + 
				"    staffID\r,\n" +
				"    labType\r\n" + 
				"FROM lab\r\n" + 
				"WHERE staffID = ?;"; 
		db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, staffID);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			labs.add(new Lab(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),rs.getString(6)));
			while(rs.next())
				labs.add(new Lab(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),rs.getString(6)));
			return labs;
		}
		else
			return null;
	}
	
	public static ArrayList<Lab> searchLabByType (String labType) throws Exception {
		ArrayList<Lab> labs = new ArrayList<Lab>();
		sql = "SELECT labID,\r\n" + 
				"    labName,\r\n" + 
				"    labAbbrev,\r\n" + 
				"    labLocation,\r\n" + 
				"    staffID,\r\n" +
				"    labType\r\n" +
				"FROM lab\r\n" + 
				"WHERE labType = ?;"; 
		db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, labType);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			labs.add(new Lab(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),rs.getString(6)));
			while(rs.next())
				labs.add(new Lab(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),rs.getString(6)));
			return labs;
		}
		else
			return null;
	}
 
	public static int addLab(String name, String abbrev, String location, String staffID, String labType) throws Exception {
		sql = "INSERT INTO `labreservationsystem`.`lab`\r\n" + 
				"(`labName`,\r\n" + 
				"`labAbbrev`,\r\n" + 
				"`labLocation`,\r\n" + 
				"`staffID`,\r\n" + 
				"`labType`)\r\n" +
				"VALUES\r\n" + 
				"(?,\r\n" + 
				"?,\r\n" + 
				"?,\r\n" + 
				"?,\r\n" +
				"?);";
		db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, name);
		ps.setString(2, abbrev);
		ps.setString(3, location);
		ps.setString(4, staffID);
		ps.setString(5, labType);
		System.out.println(ps.toString());
		int inserted = ps.executeUpdate();
		conn.close();
		return inserted;
	}
	
	public static ArrayList<Object[]> getAllLab () throws Exception {
		ArrayList<Object[]> objects = new ArrayList<Object[]>(); 
		String sql = "select labID, labAbbrev, labName, labType, labLocation, user.username from lab \r\n" + 
				"join user \r\n" + 
				"on user.userID = lab.staffID;"; 
		DatabaseConnection db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) 
			objects.add(new Object[] {rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)});
		conn.close();
		return objects;
	}
	
	public static int updateLab(String labID, String name, String abbrev, String location,String staffID, String labType) throws Exception {
		sql = "UPDATE `lab`\r\n" + 
				"SET\r\n" + 
				"`labName` = ?,\r\n" + 
				"`labAbbrev` = ?,\r\n" + 
				"`labLocation` = ?,\r\n" + 
				"`staffID` = ?,\r\n" +
				"`labType` = ?\r\n" + 
				"WHERE `labID` = ?;";
		db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, name);
		ps.setString(2, abbrev);
		ps.setString(3, location);
		ps.setString(4, staffID);
		ps.setString(5, labType);
		ps.setString(6, labID);
		int updated = ps.executeUpdate();
		conn.close();
		return updated;
	}
}