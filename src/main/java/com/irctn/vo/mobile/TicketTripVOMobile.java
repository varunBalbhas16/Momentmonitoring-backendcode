package com.irctn.vo.mobile;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TicketTripVOMobile implements Serializable {

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
	
	private Integer tripNumber;
	private Float distance;
	private Double tripStartLatitude;
	private Double tripStartLongitude;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm a")
	private Date tripStartDate;

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

	public Integer getTripNumber() {
		return tripNumber;
	}

	public void setTripNumber(Integer tripNumber) {
		this.tripNumber = tripNumber;
	}

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
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

	public Date getTripStartDate() {
		return tripStartDate;
	}

	public void setTripStartDate(Date tripStartDate) {
		this.tripStartDate = tripStartDate;
	}
	
	
}
