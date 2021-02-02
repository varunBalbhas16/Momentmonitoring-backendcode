package com.irctn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_tickettripnavigation")
public class TicketTripNavigation implements Serializable {

	@Id
	@GeneratedValue
	private Integer tickettripnavigationid;
	
	private Integer tickettripid;
	
	private Date triptime;
	
	private Double latitude;
	
	private Double longitude;

	public Integer getTickettripnavigationid() {
		return tickettripnavigationid;
	}

	public void setTickettripnavigationid(Integer tickettripnavigationid) {
		this.tickettripnavigationid = tickettripnavigationid;
	}

	public Integer getTickettripid() {
		return tickettripid;
	}

	public void setTickettripid(Integer tickettripid) {
		this.tickettripid = tickettripid;
	}

	public Date getTriptime() {
		return triptime;
	}

	public void setTriptime(Date triptime) {
		this.triptime = triptime;
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
	
}
