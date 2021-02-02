package com.irctn.vo;

import java.io.Serializable;
import java.util.Date;

public class TicketTripVO implements Serializable {

	private Integer ticketTripId;
	
	private Integer ticket;
	
	private Integer trip;
	
	private Date tripStartDate;
	
	private Date tripEndDate;
	
	private Float distance;
	
	private Float distanceTravelled;
	
	private Integer dpmid;
	
	
	private Double tripStartLatitude;
	
	private Double tripStartLongitude;

	public Integer getTicketTripId() {
		return ticketTripId;
	}

	public void setTicketTripId(Integer ticketTripId) {
		this.ticketTripId = ticketTripId;
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

	public Date getTripStartDate() {
		return tripStartDate;
	}

	public void setTripStartDate(Date tripStartDate) {
		this.tripStartDate = tripStartDate;
	}

	public Date getTripEndDate() {
		return tripEndDate;
	}

	public void setTripEndDate(Date tripEndDate) {
		this.tripEndDate = tripEndDate;
	}

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public Integer getDpmid() {
		return dpmid;
	}

	public void setDpmid(Integer dpmid) {
		this.dpmid = dpmid;
	}

	public Double getTripStartLatitude() {
		return tripStartLatitude;
	}

	public void setTripStartLatitude(Double tripStartLatitude) {
		this.tripStartLatitude = tripStartLatitude;
	}

	public Double getTripStartLongitude() {
		return tripStartLongitude;
	}

	public void setTripStartLongitude(Double tripStartLongitude) {
		this.tripStartLongitude = tripStartLongitude;
	}
	
	public Float getDistanceTravelled() {
		return distanceTravelled;
	}
	
	public void setDistanceTravelled(Float distanceTravelled) {
		this.distanceTravelled = distanceTravelled;
	}
	
}
