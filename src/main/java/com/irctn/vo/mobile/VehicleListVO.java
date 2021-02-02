package com.irctn.vo.mobile;

import java.io.Serializable;
import java.util.List;

import com.irctn.vo.VehicleVO;

public class VehicleListVO implements Serializable {

	private String status;
	
	private String errorMessage;
	
	private List<VehicleVO> vehicles;

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

	public List<VehicleVO> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<VehicleVO> vehicles) {
		this.vehicles = vehicles;
	}
	
	
}
