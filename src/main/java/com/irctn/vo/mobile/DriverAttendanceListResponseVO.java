package com.irctn.vo.mobile;

import java.io.Serializable;
import java.util.List;

import com.irctn.vo.DriverAttendanceVO;

public class DriverAttendanceListResponseVO implements Serializable {

	private String status;
	
	private String errorMessage;
	
	private List<DriverAttendanceVO> drivers;

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

	public List<DriverAttendanceVO> getDrivers() {
		return drivers;
	}

	public void setDrivers(List<DriverAttendanceVO> drivers) {
		this.drivers = drivers;
	}
	
}
