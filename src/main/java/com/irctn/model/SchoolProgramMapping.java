package com.irctn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_schoolprogrammapping")
public class SchoolProgramMapping implements Serializable {

	@Id
	@GeneratedValue
	private Integer schoolprogrammappingid;

	private Integer contributorid;
	private Integer programid;

	private Integer status;
	
	private Date startdate;
	private Date enddate;

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Integer getSchoolprogrammappingid() {
		return schoolprogrammappingid;
	}

	public void setSchoolprogrammappingid(Integer schoolprogrammappingid) {
		this.schoolprogrammappingid = schoolprogrammappingid;
	}

	public Integer getContributorid() {
		return contributorid;
	}

	public void setContributorid(Integer contributorid) {
		this.contributorid = contributorid;
	}

	public Integer getProgramid() {
		return programid;
	}

	public void setProgramid(Integer programid) {
		this.programid = programid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
