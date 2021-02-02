package com.irctn.vo;

import java.io.Serializable;

public class TicketDetailInProgressVO extends TicketDetailVO implements Serializable {

	private String startTime;
	
	private String photo;
	
	private String coordinatorSignature;
	
	private boolean enabled;
	
	public TicketDetailInProgressVO() {
		super();
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getCoordinatorSignature() {
		return coordinatorSignature;
	}

	public void setCoordinatorSignature(String coordinatorSignature) {
		this.coordinatorSignature = coordinatorSignature;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}
