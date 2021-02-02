package com.irctn.vo.mobile;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TripDateCostVO {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy")
	private Date tripDate;
	
	private Integer ticket;
	
	private Float kms;
	
	private String cost;

	public Date getTripDate() {
		return tripDate;
	}

	public void setTripDate(Date tripDate) {
		this.tripDate = tripDate;
	}

	public Integer getTicket() {
		return ticket;
	}

	public void setTicket(Integer ticket) {
		this.ticket = ticket;
	}

	public Float getKms() {
		return kms;
	}

	public void setKms(Float kms) {
		this.kms = kms;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	
	
}
