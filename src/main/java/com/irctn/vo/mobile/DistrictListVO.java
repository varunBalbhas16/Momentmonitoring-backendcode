package com.irctn.vo.mobile;

import java.io.Serializable;
import java.util.List;

public class DistrictListVO implements Serializable {

	private String status;
	
	private String errorMessage;
	
	private List<String> districts;

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

	public List<String> getDistricts() {
		return districts;
	}

	public void setDistricts(List<String> districts) {
		this.districts = districts;
	}
	
}
