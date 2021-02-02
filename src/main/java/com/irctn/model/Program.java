package com.irctn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "tbl_programs")
public class Program implements Serializable {

	@Id
	@GeneratedValue
	private Integer programid;

	private String programname;
	private String purpose;
	
	private Date startdate;
	
	private Date enddate;

	private Integer status;
	
	@CreationTimestamp
	@Column(name = "createdate")
	private Date createDate;

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getProgramid() {
		return programid;
	}

	public void setProgramid(Integer programid) {
		this.programid = programid;
	}

	public String getProgramname() {
		return programname;
	}

	public void setProgramname(String programname) {
		this.programname = programname;
	}
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

}
