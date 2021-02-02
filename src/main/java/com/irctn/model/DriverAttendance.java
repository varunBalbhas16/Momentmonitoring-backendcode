package com.irctn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_driverattendance")
public class DriverAttendance implements Serializable {

	@Id
	@GeneratedValue
	private Integer driverattendanceid;
	
	private Integer driverid;
	
	private Integer attendance;
	
	private Date date;
	
	private Integer createdby;
	
	private String vehicle;
	
	private Integer shift;
	
	private String others;

	public Integer getDriverattendanceid() {
		return driverattendanceid;
	}

	public void setDriverattendanceid(Integer driverattendanceid) {
		this.driverattendanceid = driverattendanceid;
	}

	public Integer getDriverid() {
		return driverid;
	}

	public void setDriverid(Integer driverid) {
		this.driverid = driverid;
	}

	public Integer getAttendance() {
		return attendance;
	}

	public void setAttendance(Integer attendance) {
		this.attendance = attendance;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getCreatedby() {
		return createdby;
	}

	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}

	public String getVehicle() {
		return vehicle;
	}

	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}

	public Integer getShift() {
		return shift;
	}

	public void setShift(Integer shift) {
		this.shift = shift;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}
	
}
