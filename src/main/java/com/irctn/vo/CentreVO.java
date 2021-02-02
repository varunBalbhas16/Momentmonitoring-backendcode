package com.irctn.vo;

import java.io.Serializable;

public class CentreVO implements Serializable{
	
	private Integer centreId;
	
	private String centreName;
	
	private String centreCode;
	
	private String address;
	
	private Integer zoneId;
	
	private String zone;
	
	private Integer headId;
	
	private String centreHead;
	
	private Double latitude;
	
	private Double longitude;
	
	private String landline;

	public Integer getCentreId() {
		return centreId;
	}

	public void setCentreId(Integer centreId) {
		this.centreId = centreId;
	}

	public String getCentreName() {
		return centreName;
	}

	public void setCentreName(String centreName) {
		this.centreName = centreName;
	}

	public String getCentreCode() {
		return centreCode;
	}

	public void setCentreCode(String centreCode) {
		this.centreCode = centreCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getZoneId() {
		return zoneId;
	}

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public Integer getHeadId() {
		return headId;
	}

	public void setHeadId(Integer headId) {
		this.headId = headId;
	}

	public String getCentreHead() {
		return centreHead;
	}

	public void setCentreHead(String centreHead) {
		this.centreHead = centreHead;
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
