package com.irctn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_vehicle")
public class Vehicle implements Serializable {

	@Id
	@GeneratedValue
	private Integer vehicleid;
	
	private Integer districtid;
	
	private String service;
	
	private String location;
	
	private String registration;
	
	private Date createdon;
	
	public Vehicle() {}
	
	public Vehicle(String registration) {
		this.registration = registration;
	}

	public Integer getVehicleid() {
		return vehicleid;
	}

	public void setVehicleid(Integer vehicleid) {
		this.vehicleid = vehicleid;
	}

	public Integer getDistrictid() {
		return districtid;
	}

	public void setDistrictid(Integer districtid) {
		this.districtid = districtid;
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

	public Date getCreatedon() {
		return createdon;
	}

	public void setCreatedon(Date createdon) {
		this.createdon = createdon;
	}
	
}
