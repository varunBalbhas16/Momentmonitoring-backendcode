package com.irctn.vo.mobile;

import java.io.Serializable;
import java.util.List;

import com.irctn.vo.TicketDetailInProgressVO;

public class TicketProgressListWrapper implements Serializable {

	private String firstName;
	
	private String email;
	
	private String status;
	
	private String errorMessage;
	
	private List<TicketDetailInProgressVO> tickets;

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

	public List<TicketDetailInProgressVO> getTickets() {
		return tickets;
	}

	public void setTickets(List<TicketDetailInProgressVO> tickets) {
		this.tickets = tickets;
	}
}
