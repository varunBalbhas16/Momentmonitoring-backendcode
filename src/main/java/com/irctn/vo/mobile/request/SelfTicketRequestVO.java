package com.irctn.vo.mobile.request;

import java.io.Serializable;
import java.util.Date;

public class SelfTicketRequestVO implements Serializable {

	private String email;
	
	private Integer userId;
	
	private String centre;
	
	private String address;
	
	private Double latitude;
	
	private Double longitude;
	
	private Date dateofappointment;
	
	private String hourofappointment;
	
	private String description;
	

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

	public String getCentre() {
		return centre;
	}

	public void setCentre(String centre) {
		this.centre = centre;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public Date getDateofappointment() {
		return dateofappointment;
	}

	public void setDateofappointment(Date dateofappointment) {
		this.dateofappointment = dateofappointment;
	}

	public String getHourofappointment() {
		return hourofappointment;
	}

	public void setHourofappointment(String hourofappointment) {
		this.hourofappointment = hourofappointment;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
