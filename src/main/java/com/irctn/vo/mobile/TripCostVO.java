package com.irctn.vo.mobile;

import java.io.Serializable;
import java.math.BigDecimal;

public class TripCostVO implements Serializable {

	private Integer ticket;
	
	private Float kms;
	
	private BigDecimal cost;

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

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	
}
