package model;
import java.sql.Time;
import java.sql.Date;
public class Reservation {
	private Date dateReserved;
	private Time timeReservedStart;
	private Time timeReservedEnd;
	private String reason;
	private String reservedBy; 
	private String approvalStatus;
	
	public Reservation(Time timeReservedStart, Time timeReservedEnd, Date dateReserved, String reservedBy, String reason) {
		this.dateReserved=dateReserved;
		this.timeReservedStart=timeReservedStart;
		this.timeReservedEnd=timeReservedEnd;
		this.reason = reason;
		this.reservedBy = reservedBy;
		this.approvalStatus = "IN REVIEW";
	}
	
	public Reservation(Time timeReservedStart, Time timeReservedEnd, Date dateReserved, String reservedBy, String reason, String approvalStatus) {
		this.dateReserved=dateReserved;
		this.timeReservedStart=timeReservedStart;
		this.timeReservedEnd=timeReservedEnd;
		this.reason = reason;
		this.reservedBy = reservedBy;
		this.approvalStatus = approvalStatus;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReservedBy() {
		return reservedBy;
	}

	public void setReservedBy(String reservedBy) {
		this.reservedBy = reservedBy;
	}

	public Date getDateReserved() {
		return dateReserved;
	}

	public void setDateReserved(Date dateReserved) {
		this.dateReserved = dateReserved;
	}

	public Time getTimeReservedStart() {
		return timeReservedStart;
	}

	public void setTimeReservedStart(Time timeReservedStart) {
		this.timeReservedStart = timeReservedStart;
	}

	public Time getTimeReservedEnd() {
		return timeReservedEnd;
	}

	public void setTimeReservedEnd(Time timeReservedEnd) {
		this.timeReservedEnd = timeReservedEnd;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	
	
}
