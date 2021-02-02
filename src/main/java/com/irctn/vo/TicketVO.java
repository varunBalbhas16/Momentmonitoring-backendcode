package com.irctn.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TicketVO implements Serializable {

	private Integer ticketId;
	private Integer ticket;
	private Integer centreId;
	private Integer departmentId;
	private Integer zoneId;
	private String description;
	private Integer coordinatorId;
	private String coordinatorcontact;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date appointment;
	
	private String timeSlot;
	
	private String status;
	private Integer dpmId;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date startdatetime;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date enddatetime;
	private String photo;
	private String signature;
	
	private String zoneName;
	private String centreName;
	private String departmentName;
	private String coordinator;
	private String dpm;
	
	private Boolean selfTicket;
	
	private Integer selfTicketId;
	
	public Integer getTicketId() {
		return ticketId;
	}
	public void setTicketId(Integer ticketId) {
		this.ticketId = ticketId;
	}
	public Integer getTicket() {
		return ticket;
	}
	public void setTicket(Integer ticket) {
		this.ticket = ticket;
	}
	public Integer getCentreId() {
		return centreId;
	}
	public void setCentreId(Integer centreId) {
		this.centreId = centreId;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getCoordinatorId() {
		return coordinatorId;
	}
	public void setCoordinatorId(Integer coordinatorId) {
		this.coordinatorId = coordinatorId;
	}
	public String getCoordinatorcontact() {
		return coordinatorcontact;
	}
	public void setCoordinatorcontact(String coordinatorcontact) {
		this.coordinatorcontact = coordinatorcontact;
	}
	public Date getAppointment() {
		return appointment;
	}
	public void setAppointment(Date appointment) {
		this.appointment = appointment;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getDpmId() {
		return dpmId;
	}
	public void setDpmId(Integer dpmId) {
		this.dpmId = dpmId;
	}
	public Date getStartdatetime() {
		return startdatetime;
	}
	public void setStartdatetime(Date startdatetime) {
		this.startdatetime = startdatetime;
	}
	public Date getEnddatetime() {
		return enddatetime;
	}
	public void setEnddatetime(Date enddatetime) {
		this.enddatetime = enddatetime;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getCentreName() {
		return centreName;
	}
	public void setCentreName(String centreName) {
		this.centreName = centreName;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getCoordinator() {
		return coordinator;
	}
	public void setCoordinator(String coordinator) {
		this.coordinator = coordinator;
	}
	public String getDpm() {
		return dpm;
	}
	public void setDpm(String dpm) {
		this.dpm = dpm;
	}
	public String getTimeSlot() {
		return timeSlot;
	}
	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}
	public Integer getZoneId() {
		return zoneId;
	}
	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public Boolean getSelfTicket() {
		return selfTicket;
	}
	public void setSelfTicket(Boolean selfTicket) {
		this.selfTicket = selfTicket;
	}
	public Integer getSelfTicketId() {
		return selfTicketId;
	}
	public void setSelfTicketId(Integer selfTicketId) {
		this.selfTicketId = selfTicketId;
	}
	
	
}
