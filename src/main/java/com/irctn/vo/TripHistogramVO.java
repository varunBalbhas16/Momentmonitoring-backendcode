package com.irctn.vo;

import java.io.Serializable;

public class TripHistogramVO implements Serializable {

	private String month;
	private double totalTripKms;
	private double totalTripCost;
	private long totalTickets;
	
	public TripHistogramVO() { }
	
	public TripHistogramVO(String month, double totalTripKms, double totalTripCost, long totalTickets) {
		super();
		this.month = month;
		this.totalTripKms = totalTripKms;
		this.totalTripCost = totalTripCost;
		this.totalTickets = totalTickets;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public double getTotalTripKms() {
		return totalTripKms;
	}

	public void setTotalTripKms(double totalTripKms) {
		this.totalTripKms = totalTripKms;
	}

	public double getTotalTripCost() {
		return totalTripCost;
	}

	public void setTotalTripCost(double totalTripCost) {
		this.totalTripCost = totalTripCost;
	}

	public long getTotalTickets() {
		return totalTickets;
	}

	public void setTotalTickets(long totalTickets) {
		this.totalTickets = totalTickets;
	}
	
	
	
}
