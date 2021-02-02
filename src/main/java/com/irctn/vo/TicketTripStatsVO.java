package com.irctn.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TicketTripStatsVO implements Serializable {

	//Startdate, DPM name, Trip start, Trip End, Start Location, End Location, Ticket #, distance, cost
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy")
	private Date date;
	
	private Integer ticket;
	
	private Float distance;
	
	private Float distanceTravelled;
	
	private BigDecimal cost;
	
	private String dpm;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM hh:mm a")
	private Date tripStart;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM hh:mm a")
	private Date tripEnd;
	
	private String startLocation;
	
	private String endLocation;
	
	private String type;
	
	private String description;
	
	private String photo;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getTicket() {
		return ticket;
	}

	public void setTicket(Integer ticket) {
		this.ticket = ticket;
	}

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getDpm() {
		return dpm;
	}

	public void setDpm(String dpm) {
		this.dpm = dpm;
	}

	public Date getTripStart() {
		return tripStart;
	}

	public void setTripStart(Date tripStart) {
		this.tripStart = tripStart;
	}

	public Date getTripEnd() {
		return tripEnd;
	}

	public void setTripEnd(Date tripEnd) {
		this.tripEnd = tripEnd;
	}

	public String getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	}

	public String getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(String endLocation) {
		this.endLocation = endLocation;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Float getDistanceTravelled() {
		return distanceTravelled;
	}

	public void setDistanceTravelled(Float distanceTravelled) {
		this.distanceTravelled = distanceTravelled;
	}
	
	
	
}
