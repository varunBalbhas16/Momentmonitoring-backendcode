package com.irctn.vo.mobile.request;

import java.io.Serializable;

public class TicketTripStartRequestVO implements Serializable {

	private String email;
	
	private Integer userId;
	
	private Integer ticket;
	
	private String comments;
	
	private String startCentre;
	
	private Double startLatitude;
	
	private Double startLongitude;
	
	private Float distanceEstimate;
	
	private Integer driverId;

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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getStartCentre() {
		return startCentre;
	}

	public void setStartCentre(String startCentre) {
		this.startCentre = startCentre;
	}

	public Double getStartLatitude() {
		return startLatitude;
	}

	public void setStartLatitude(Double startLatitude) {
		this.startLatitude = startLatitude;
	}

	public Double getStartLongitude() {
		return startLongitude;
	}

	public void setStartLongitude(Double startLongitude) {
		this.startLongitude = startLongitude;
	}

	public Float getDistanceEstimate() {
		return distanceEstimate;
	}

	public void setDistanceEstimate(Float distanceEstimate) {
		this.distanceEstimate = distanceEstimate;
	}

	public Integer getDriverId() {
		return driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}
	
}
