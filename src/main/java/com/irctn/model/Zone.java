package com.irctn.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_zone")
public class Zone implements Serializable {

	@Id
	@GeneratedValue
	private Integer zoneid;

	private String zone;
	private String code;
	private String description;
	
	public Integer getZoneid() {
		return zoneid;
	}
	public void setZoneid(Integer zoneid) {
		this.zoneid = zoneid;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
