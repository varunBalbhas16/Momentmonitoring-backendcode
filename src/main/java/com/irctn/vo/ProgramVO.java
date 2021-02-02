package com.irctn.vo;

import java.io.Serializable;
import java.util.Date;

public class ProgramVO implements Serializable {

	private Integer programId;

	private String programName;
	private Date startDate;
	private Date endDate;
	private String purpose;

	private Integer status;
	
	private Date createDate;

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

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

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
