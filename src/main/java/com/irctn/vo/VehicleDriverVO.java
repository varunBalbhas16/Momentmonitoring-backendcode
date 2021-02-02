package com.irctn.vo;

import java.io.Serializable;

public class VehicleDriverVO implements Serializable {

	private String driverName;
	
	private String driverEmpId;
	
	private String district;
	
	private String attendance = "";
	
	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverEmpId() {
		return driverEmpId;
	}

	public void setDriverEmpId(String driverEmpId) {
		this.driverEmpId = driverEmpId;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAttendance() {
		return attendance;
	}

	public void setAttendance(String attendance) {
		this.attendance = attendance;
	}
	
}
