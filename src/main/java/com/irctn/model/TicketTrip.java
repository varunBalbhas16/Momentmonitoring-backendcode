package com.irctn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_tickettrip")
public class TicketTrip implements Serializable {

	@Id
	@GeneratedValue
	private Integer tickettripid;
	
	private Integer ticket;
	
	private Integer trip;
	
	private Date tripstartdate;
	
	private Date tripenddate;
	
	private Float distance;
	
	private Integer dpmid;
	
	private Double tripstartlatitude;
	
	private Double tripstartlongitude;

	public Integer getTickettripid() {
		return tickettripid;
	}

	public void setTickettripid(Integer tickettripid) {
		this.tickettripid = tickettripid;
	}

	public Integer getTicket() {
		return ticket;
	}

	public void setTicket(Integer ticket) {
		this.ticket = ticket;
	}

	public Integer getTrip() {
		return trip;
	}

	public void setTrip(Integer trip) {
		this.trip = trip;
	}

	public Date getTripstartdate() {
		return tripstartdate;
	}

	public void setTripstartdate(Date tripdate) {
		this.tripstartdate = tripdate;
	}

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public Integer getDpmid() {
		return dpmid;
	}

	public void setDpmid(Integer dpmid) {
		this.dpmid = dpmid;
	}

	public Date getTripenddate() {
		return tripenddate;
	}

	public void setTripenddate(Date tripenddate) {
		this.tripenddate = tripenddate;
	}

	public Double getTripstartlatitude() {
		return tripstartlatitude;
	}

	public void setTripstartlatitude(Double tripstartlatitude) {
		this.tripstartlatitude = tripstartlatitude;
	}

	public Double getTripstartlongitude() {
		return tripstartlongitude;
	}

	public void setTripstartlongitude(Double tripstartlongitude) {
		this.tripstartlongitude = tripstartlongitude;
	}
	
}
