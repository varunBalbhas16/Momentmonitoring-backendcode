package com.irctn.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_driver")
public class VehicleDriver implements Serializable {

	@Id
	@GeneratedValue
	private Integer driverid;
	
	private String drivername;
	
	private Integer districtid;
	
	private String driverempid;

	public Integer getDriverid() {
		return driverid;
	}

	public void setDriverid(Integer driverid) {
		this.driverid = driverid;
	}

	public String getDrivername() {
		return drivername;
	}

	public void setDrivername(String drivername) {
		this.drivername = drivername;
	}

	public Integer getDistrictid() {
		return districtid;
	}

	public void setDistrictid(Integer districtid) {
		this.districtid = districtid;
	}

	public String getDriverempid() {
		return driverempid;
	}

	public void setDriverempid(String driverempid) {
		this.driverempid = driverempid;
	}
	
}
