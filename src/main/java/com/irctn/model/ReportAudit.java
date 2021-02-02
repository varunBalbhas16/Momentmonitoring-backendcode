package com.irctn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_report_audit")
public class ReportAudit implements Serializable{

	@Id
	@GeneratedValue
	private Integer reportid;
	
	private String month;
	
	private Integer year;
	
	private Integer dpmid;
	
	private String summary;
	
	private Date createdon;

	public Integer getReportid() {
		return reportid;
	}

	public void setReportid(Integer reportid) {
		this.reportid = reportid;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getDpmid() {
		return dpmid;
	}

	public void setDpmid(Integer dpmid) {
		this.dpmid = dpmid;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Date getCreatedon() {
		return createdon;
	}

	public void setCreatedon(Date createdon) {
		this.createdon = createdon;
	}
	
}