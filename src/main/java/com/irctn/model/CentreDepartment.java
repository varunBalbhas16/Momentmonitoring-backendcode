package com.irctn.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_centredepartment")
public class CentreDepartment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Integer centredepartmentid;
	
	private Integer centreid;
	
	private Integer departmentid;
	
	private Integer departmentheadid;

	public Integer getCentredepartmentid() {
		return centredepartmentid;
	}

	public void setCentredepartmentid(Integer centredepartmentid) {
		this.centredepartmentid = centredepartmentid;
	}

	public Integer getCentreid() {
		return centreid;
	}

	public void setCentreid(Integer centreid) {
		this.centreid = centreid;
	}

	public Integer getDepartmentid() {
		return departmentid;
	}

	public void setDepartmentid(Integer departmentid) {
		this.departmentid = departmentid;
	}

	public Integer getDepartmentheadid() {
		return departmentheadid;
	}

	public void setDepartmentheadid(Integer departmentheadid) {
		this.departmentheadid = departmentheadid;
	}
	
}
