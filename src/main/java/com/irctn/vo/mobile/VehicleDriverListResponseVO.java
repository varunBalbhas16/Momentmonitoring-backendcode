package com.irctn.vo.mobile;

import java.io.Serializable;
import java.util.List;

import com.irctn.vo.VehicleDriverVO;

public class VehicleDriverListResponseVO implements Serializable {

	private String status;
	
	private String errorMessage;
	
	private List<VehicleDriverVO> drivers;

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

	public List<VehicleDriverVO> getDrivers() {
		return drivers;
	}

	public void setDrivers(List<VehicleDriverVO> drivers) {
		this.drivers = drivers;
	}
}
