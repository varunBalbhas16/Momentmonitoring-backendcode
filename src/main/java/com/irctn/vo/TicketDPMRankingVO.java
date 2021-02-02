package com.irctn.vo;

import java.io.Serializable;

public class TicketDPMRankingVO implements Serializable {

	private int rank;
	private String dpm;
	private int tickets;
	
	public TicketDPMRankingVO(int rank, String dpm, int tickets) {
		super();
		this.rank = rank;
		this.dpm = dpm;
		this.tickets = tickets;
	}
	
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getDpm() {
		return dpm;
	}
	public void setDpm(String dpm) {
		this.dpm = dpm;
	}
	public int getTickets() {
		return tickets;
	}
	public void setTickets(int tickets) {
		this.tickets = tickets;
	}
	
}
