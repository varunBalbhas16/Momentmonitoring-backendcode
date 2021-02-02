package com.irctn.vo.mobile;

import java.io.Serializable;
import java.util.List;

import com.irctn.vo.ShortTicketVO;

public class TicketVOListWrapper implements Serializable {

	private String firstName;
	
	private String email;
	
	private String status;
	
	private List<ShortTicketVO> tickets;

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

	public List<ShortTicketVO> getTickets() {
		return tickets;
	}

	public void setTickets(List<ShortTicketVO> tickets) {
		this.tickets = tickets;
	}
	
}
