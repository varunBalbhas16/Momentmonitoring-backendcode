package com.irctn.vo;

import java.io.Serializable;

public class DistrictVO implements Serializable {

	private Integer districtId;
	
	private String district;

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}
	
}
