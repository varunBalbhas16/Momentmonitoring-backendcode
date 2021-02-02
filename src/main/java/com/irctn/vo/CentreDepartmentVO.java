package com.irctn.vo;

import java.io.Serializable;

public class CentreDepartmentVO implements Serializable {

	private Integer centerDepartmentId;
	
	private Integer centreId;
	
	private Integer departmentId;
	
	private Integer departmentHeadId;
	
	private String centerName;
	
	private String departmentName;
	
	private String departmentHeadName;
	
	private String departmentHeadContact;
	
	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getCenterDepartmentId() {
		return centerDepartmentId;
	}

	public void setCenterDepartmentId(Integer centerDepartmentId) {
		this.centerDepartmentId = centerDepartmentId;
	}

	public Integer getCentreId() {
		return centreId;
	}

	public void setCentreId(Integer centreId) {
		this.centreId = centreId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getDepartmentHeadId() {
		return departmentHeadId;
	}

	public void setDepartmentHeadId(Integer departmentHeadId) {
		this.departmentHeadId = departmentHeadId;
	}

	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentHeadName() {
		return departmentHeadName;
	}

	public void setDepartmentHeadName(String departmentHeadName) {
		this.departmentHeadName = departmentHeadName;
	}

	public String getDepartmentHeadContact() {
		return departmentHeadContact;
	}

	public void setDepartmentHeadContact(String departmentHeadContact) {
		this.departmentHeadContact = departmentHeadContact;
	}
	
}
