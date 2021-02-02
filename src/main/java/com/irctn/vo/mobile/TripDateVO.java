package com.irctn.vo.mobile;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TripDateVO implements Serializable {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy")
	private Date tripDate;
	
	private List<TripCostVO> trips;

	public Date getTripDate() {
		return tripDate;
	}

	public void setTripDate(Date tripDate) {
		this.tripDate = tripDate;
	}

	public List<TripCostVO> getTrips() {
		return trips;
	}

	public void setTrips(List<TripCostVO> trips) {
		this.trips = trips;
	}
	
}
