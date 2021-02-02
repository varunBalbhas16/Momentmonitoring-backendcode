package com.irctn.vo;

import java.io.Serializable;
import java.util.Date;

public class TicketHistoryVO implements Serializable {

	private Integer tickethistoryid;	
	private Integer ticket;
	private String status;
	private Integer dpm;
	private Date logdate;
	private String comments;
	
	public Integer getTickethistoryid() {
		return tickethistoryid;
	}
	public void setTickethistoryid(Integer tickethistoryid) {
		this.tickethistoryid = tickethistoryid;
	}
	public Integer getTicket() {
		return ticket;
	}
	public void setTicket(Integer ticket) {
		this.ticket = ticket;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getDpm() {
		return dpm;
	}
	public void setDpm(Integer dpm) {
		this.dpm = dpm;
	}
	public Date getLogdate() {
		return logdate;
	}
	public void setLogdate(Date logdate) {
		this.logdate = logdate;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
	
}
