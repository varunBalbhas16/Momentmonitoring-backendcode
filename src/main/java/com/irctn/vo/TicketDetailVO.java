package com.irctn.vo;

import java.io.Serializable;

public class TicketDetailVO implements Serializable {
	
	private Integer ticket;
	private String zone;
	private String centre;
	private String date;
	private String time;
	private String address;
	private String coordinator;
	private String contact;
	private String description;
	private String latitude;
	private String longitude;
	private String status;
	private Boolean selfTicket;
	
	public Integer getTicket() {
		return ticket;
	}
	public void setTicket(Integer ticket) {
		this.ticket = ticket;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getCentre() {
		return centre;
	}
	public void setCentre(String centre) {
		this.centre = centre;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCoordinator() {
		return coordinator;
	}
	public void setCoordinator(String coordinator) {
		this.coordinator = coordinator;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Boolean getSelfTicket() {
		return selfTicket;
	}
	public void setSelfTicket(Boolean selfTicket) {
		this.selfTicket = selfTicket;
	}
	
}
