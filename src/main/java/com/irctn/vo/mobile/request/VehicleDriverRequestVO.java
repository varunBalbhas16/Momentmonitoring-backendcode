package com.irctn.vo.mobile.request;

import java.io.Serializable;


public class VehicleDriverRequestVO implements Serializable {
	
	private String driverName;
	
	private String driverEmpId;
	
	private String district;
	
	private String attendance;
	
	private Integer userId;
	
	private String vehicle;
	
	private String shift;
	
	private String others;

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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getVehicle() {
		return vehicle;
	}

	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}

	public String getShift() {
		return shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}
	
}
