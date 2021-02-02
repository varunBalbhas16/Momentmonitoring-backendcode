package com.irctn.vo.mobile;

import java.io.Serializable;
import java.util.List;

public class DriverListResponseVO implements Serializable {

	private String status;
	
	private String errorMessage;
	
	private List<UserShortDetailVO> drivers;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<UserShortDetailVO> getDrivers() {
		return drivers;
	}

	public void setDrivers(List<UserShortDetailVO> drivers) {
		this.drivers = drivers;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
