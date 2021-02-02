package com.irctn.vo;

import java.io.Serializable;

public class VehicleVO implements Serializable {

	private String district;
	
	private String service;
	
	private String location;
	
	private String registration;

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRegistration() {
		return registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
	}
	
}
