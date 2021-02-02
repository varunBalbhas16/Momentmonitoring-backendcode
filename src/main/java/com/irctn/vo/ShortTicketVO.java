package com.irctn.vo;

import java.io.Serializable;

public class ShortTicketVO implements Serializable{
	
	private Integer id;
		
	private String centre;

	private String zone;
	
	private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCentre() {
		return centre;
	}

	public void setCentre(String centre) {
		this.centre = centre;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
