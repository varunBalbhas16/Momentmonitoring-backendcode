package com.irctn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_selfticket")
public class SelfTicket implements Serializable {

	@Id
	@GeneratedValue
	private Integer selfticketid;
	
	private String centre;
	
	private String address;
	
	private Double latitude;
	
	private Double longitude;
	
	private Integer createdby;
	
	private Date createdon;

	public Integer getSelfticketid() {
		return selfticketid;
	}

	public void setSelfticketid(Integer selfticketid) {
		this.selfticketid = selfticketid;
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

	public Integer getCreatedby() {
		return createdby;
	}

	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}

	public Date getCreatedon() {
		return createdon;
	}

	public void setCreatedon(Date createdon) {
		this.createdon = createdon;
	}
	
}
