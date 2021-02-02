package com.irctn.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_location")
public class Location implements Serializable {

	@Id
	@GeneratedValue
	private Integer locationid;

	private String name;

	private Double latitude;
	private Double longtitude;

	public Integer getLocationid() {
		return locationid;
	}

	public void setLocationid(Integer locationid) {
		this.locationid = locationid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(Double longtitude) {
		this.longtitude = longtitude;
	}

}
