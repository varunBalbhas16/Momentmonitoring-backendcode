package com.irctn.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DriverAttendanceVO implements Serializable {

	private String username;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm")
	private Date workDate;
	
	private String attendance;
	
	private String driverEmpId;
	
	private String vehicle = "";		//optional
	
	private String shift;
	
	private String others = "";			//conditionally optional when vehicle is 'others'
	
	private String district = "";

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public String getAttendance() {
		return attendance;
	}

	public void setAttendance(String attendance) {
		this.attendance = attendance;
	}

	public String getDriverEmpId() {
		return driverEmpId;
	}

	public void setDriverEmpId(String driverEmpId) {
		this.driverEmpId = driverEmpId;
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
	
}
