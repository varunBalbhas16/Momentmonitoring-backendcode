package com.irctn.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_centre")
public class Centre implements Serializable {
	
	@Id
	@GeneratedValue
	private Integer centreid;
	
	private String centrename;
	private String centrecode;
	private String address;
	
	private Integer zoneid;
	private Integer headid;
	
	private Double latitude;
	private Double longitude;
	
	private String landline;
	
	public Integer getCentreid() {
		return centreid;
	}
	public void setCentreid(Integer centreid) {
		this.centreid = centreid;
	}
	public String getCentrename() {
		return centrename;
	}
	public void setCentrename(String centrename) {
		this.centrename = centrename;
	}
	public String getCentrecode() {
		return centrecode;
	}
	public void setCentrecode(String centrecode) {
		this.centrecode = centrecode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getZoneid() {
		return zoneid;
	}
	public void setZoneid(Integer zoneid) {
		this.zoneid = zoneid;
	}
	public Integer getHeadid() {
		return headid;
	}
	public void setHeadid(Integer headid) {
		this.headid = headid;
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
	public String getLandline() {
		return landline;
	}
	public void setLandline(String landline) {
		this.landline = landline;
	}
	
}
