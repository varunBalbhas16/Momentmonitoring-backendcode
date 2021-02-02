package com.irctn.vo.mobile.request;

import java.io.Serializable;

public class TicketUpdateRequestVO implements Serializable {

	private String email;
	
	private Integer userId;
	
	private Integer ticket;
	
	private String comments;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getTicket() {
		return ticket;
	}

	public void setTicket(Integer ticket) {
		this.ticket = ticket;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
}
