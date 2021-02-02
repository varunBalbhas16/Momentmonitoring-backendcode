package com.irctn.vo.mobile;

import java.io.Serializable;
import java.util.List;

public class TicketTripCostWrapper implements Serializable {

	private String firstName;
	
	private String email;
	
	private String status;
	
	private String errorMessage;
	
	private List<TripDateVO> tripList;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<TripDateVO> getTripList() {
		return tripList;
	}

	public void setTripList(List<TripDateVO> tripList) {
		this.tripList = tripList;
	} 
	
}
