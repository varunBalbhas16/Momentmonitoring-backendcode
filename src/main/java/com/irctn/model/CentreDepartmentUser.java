package com.irctn.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_centredepartmentuser")
public class CentreDepartmentUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Integer centredepartmentuserid;
	
	private Integer centredepartmentid;
	
	private Integer userid;
	
	private Integer roleid;

	public Integer getCentredepartmentuserid() {
		return centredepartmentuserid;
	}

	public void setCentredepartmentuserid(Integer centredepartmentuserid) {
		this.centredepartmentuserid = centredepartmentuserid;
	}

	public Integer getCentredepartmentid() {
		return centredepartmentid;
	}

	public void setCentredepartmentid(Integer centredepartmentid) {
		this.centredepartmentid = centredepartmentid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getRoleid() {
		return roleid;
	}

	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}
	
}
