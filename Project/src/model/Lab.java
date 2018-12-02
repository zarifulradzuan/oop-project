package model;
import java.util.ArrayList;

public class Lab {
	
	private String id;
	private String name;
	private String abbrev;
	private String location;
	private ArrayList<Reservation> reservations = new ArrayList<Reservation>();
	private String staffID;
	private String labType;
	
	public Lab() {}
	public Lab(String name, String abbrev, String location, String staffID, String labType) { //NEW LAB DOES NOT HAVE ID; AUTO GENERATED
		this.name=name;
		this.abbrev = abbrev;
		this.location=location;
		this.staffID = staffID;
		this.labType = labType;
	}
	public Lab(String id, String name, String abbrev, String location, String staffID, String labType) { //EXISTING
		this.id=id;
		this.name=name;
		this.abbrev = abbrev;
		this.location=location;
		this.staffID = staffID;
		this.labType = labType;
	}
	public void loadReservation(Reservation reservation) throws Exception {
		this.reservations.add(reservation);
	}
	
	public void addReservation(Reservation reservation) {
		this.reservations.add(reservation);
	}
	
	public ArrayList<Reservation> getReservations(){
		return reservations;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getAbbrev() {
		return abbrev;
	}
	public void setAbbrev(String abbrev) {
		this.abbrev = abbrev;
	}
	public String getStaffID() {
		return staffID;
	}
	public void setStaffID(String staffID) {
		this.staffID = staffID;
	}
	public String getLabType() {
		return labType;
	}
	public void setLabType(String labType) {
		this.labType = labType;
	}
}
