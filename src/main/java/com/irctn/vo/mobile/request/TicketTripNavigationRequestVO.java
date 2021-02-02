package com.irctn.vo.mobile.request;

import java.io.Serializable;

public class TicketTripNavigationRequestVO implements Serializable {

	private String email;
	
	private Integer userId;
	
	private Integer ticket;
	
	private Integer trip;	
	
	private Double latitude;
	
	private Double longitude;
	
	private String tripMovement;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getTicket() {
		return ticket;
	}

	public void setTicket(Integer ticket) {
		this.ticket = ticket;
	}

	public Integer getTrip() {
		return trip;
	}

	public void setTrip(Integer trip) {
		this.trip = trip;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getTripMovement() {
		return tripMovement;
	}

	public void setTripMovement(String tripMovement) {
		this.tripMovement = tripMovement;
	}
	
}
