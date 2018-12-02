package controller;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;


import database.DatabaseConnection;
import model.Lab;
import model.Reservation;
public class ReservationController {
	private Lab lab;
	public ReservationController(Lab lab) {
		this.lab = lab;
	}
	
	public boolean checkIfAvailable(Time start, Time end, Date date) throws Exception {
		if(start.after(end))
			return false;
		if(start.equals(end))
			return false;
		loadReservation();
		for(Reservation a: lab.getReservations()) {
			if(a.getDateReserved().toString().equals(date.toString())) {
				if(start.after(a.getTimeReservedStart()) && start.before(a.getTimeReservedEnd()) && a.getApprovalStatus().matches("APPROVED"))
					return false;
				if (start.before(a.getTimeReservedStart()) && end.after(a.getTimeReservedStart()) && a.getApprovalStatus().matches("APPROVED"))
					return false;
				if (start.equals(a.getTimeReservedStart()) && a.getApprovalStatus().matches("APPROVED"))
					return false;
			}
		}
		return true;
	}
	
	public void loadReservation() throws Exception {
		String sql = "SELECT timeReservedStart,timeReservedEnd, dateReserved,reservedBy,reason, approvalstatus FROM reservation where labid=?";
		DatabaseConnection db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, this.getLabID());
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			lab.loadReservation(new Reservation(rs.getTime(1), rs.getTime(2), rs.getDate(3), rs.getString(4), rs.getString(5), rs.getString(6)));
		}
		conn.close();
	}
	
	public static int deleteReservation(String reservationID) throws Exception {
		String sql = "DELETE FROM reservation WHERE reservationID = ?;";
		DatabaseConnection db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, reservationID);
		int deleted = ps.executeUpdate();
		conn.close();
		return deleted;
	}
	
	public boolean addNewReservation(Time start, Time end, Date date, String user, String reason, String position) throws Exception {
			DatabaseConnection db = new DatabaseConnection();
			Connection conn = db.getConnection();
			String sql;
			if(position.matches("LECTURER") || position.matches("STUDENT"))
				sql = "INSERT INTO reservation (`dateReserved`,\r\n" + 
					"`timeReservedStart`,\r\n" + 
					"`timeReservedEnd`,\r\n" + 
					"`reservedBy`,\r\n" + 
					"`reason`,\r\n" + 
					"`labID`)\r\n" + 
					"VALUES\r\n" + 
					"(?,?,?,?,?,?);";
			else
				sql = "INSERT INTO reservation (`dateReserved`,\r\n" + 
						"`timeReservedStart`,\r\n" + 
						"`timeReservedEnd`,\r\n" + 
						"`reservedBy`,\r\n" + 
						"`reason`,\r\n" + 
						"`labID`,"
						+ "approvalStatus)\r\n" + 
						"VALUES\r\n" + 
						"(?,?,?,?,?,?,'APPROVED');";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDate(1, date);
			ps.setTime(2, start);
			ps.setTime(3, end);
			ps.setString(4, user);
			ps.setString(5, reason);
			ps.setString(6, this.lab.getId());
			boolean inserted;
			inserted = ps.execute();
			lab.addReservation(new Reservation(start, end, date, user, reason));
			conn.close();
			return inserted;
	}
	
	public static ArrayList<Object[]> getReservedByUser(String userID) throws Exception {
		ArrayList<Object[]> objects = new ArrayList<Object[]>(); 
		String sql = "SELECT lab.labName, timeReservedStart, "
				+ "reservation.timeReservedEnd, reservation.dateReserved, "
				+ "reservation.reason, reservation.approvalstatus,"
				+ "reservation.reservationID\r\n" + 
				"FROM reservation \r\n" + 
				"JOIN lab ON reservation.labID = lab.labID \r\n" + 
				"WHERE reservation.reservedBy = ?";
		DatabaseConnection db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, userID);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			objects.add(new Object[] {rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6), rs.getString(7)});
		}
		conn.close();
		return objects;
	}
	
	public static ArrayList<Object[]> getReservedAll(String approvalStatus) throws Exception {
		ArrayList<Object[]> objects = new ArrayList<Object[]>(); 
		String sql = "SELECT labName,timeReservedStart,"
				+ "reservation.timeReservedEnd,reservation.dateReserved, "
				+ "user.username, reservation.reason, reservation.approvalstatus,"
				+ "reservation.reservationID\r\n" + 
				"FROM reservation\r\n" + 
				"JOIN lab ON reservation.labID = lab.labID\r\n" + 
				"JOIN user on reservation.reservedBy = user.userID\r\n"+ 
				"WHERE reservation.approvalStatus = ?;"; 
		DatabaseConnection db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, approvalStatus);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) 
			objects.add(new Object[] {rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6), rs.getString(7), rs.getString(8)});
		conn.close();
		return objects;
	}
	
	public static ArrayList<Object[]> getReservedByStaff(String userID, String approvalStatus) throws Exception {
		ArrayList<Object[]> objects = new ArrayList<Object[]>(); 
		String sql = "SELECT labName,timeReservedStart,"
				+ "reservation.timeReservedEnd,reservation.dateReserved, "
				+ "user.username, reservation.reason, reservation.approvalstatus,"
				+ "reservation.reservationID\r\n" + 
				"FROM reservation\r\n" + 
				"JOIN lab ON reservation.labID = lab.labID\r\n" + 
				"JOIN user on reservation.reservedBy = user.userID\r\n" + 
				"WHERE RESERVATION.APPROVALSTATUS = ? && lab.staffID = ?;";
		DatabaseConnection db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, approvalStatus);
		ps.setString(2, userID);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) 
			objects.add(new Object[] {rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8)});
		conn.close();
		return objects;
	}
	
	public static int changeStatus(String reservationID, String approvalStatus) throws Exception {
		String sql = "UPDATE `reservation`\r\n" + 
				"		SET\r\n" + 
				"		`approvalStatus` = ?\r\n" + 
				"		WHERE `reservationID` = ?;";
		DatabaseConnection db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, approvalStatus);
		ps.setString(2, reservationID);
		int updated = ps.executeUpdate();
		conn.close();
		return updated;
	}
	
	public String getLabID() {
		return lab.getId();
	}
}