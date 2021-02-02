package com.irctn.vo;

import java.io.Serializable;

public class TicketHistogramVO implements Serializable {

	private String month;
	private long tickets;
	private long selfTickets;
	private long adminTickets;
	
	public TicketHistogramVO(String month, long tickets, long selfTickets, long adminTickets) {
		super();
		this.month = month;
		this.tickets = tickets;
		this.selfTickets = selfTickets;
		this.adminTickets = adminTickets;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public long getTickets() {
		return tickets;
	}
	public void setTickets(long tickets) {
		this.tickets = tickets;
	}
	public long getSelfTickets() {
		return selfTickets;
	}
	public void setSelfTickets(long selfTickets) {
		this.selfTickets = selfTickets;
	}
	public long getAdminTickets() {
		return adminTickets;
	}
	public void setAdminTickets(long adminTickets) {
		this.adminTickets = adminTickets;
	}	
	
}
