package com.irctn.vo;

import java.io.Serializable;
import java.util.Date;

public class SchoolProgramMappingVO implements Serializable {

	private Integer schoolProgrammappingId;

	private Date startDate;
	private Date endDate;

	// Program Details
	private Integer programId;
	private String programName;

	// Contributor Details
	private Integer contributorId;
	private String type;
	private String contributorName;

	private Double totalWeight;
	private Double totalRecyclable;
	private Double totalWaste;

	private Integer status;
	
	private String statusName;

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public Double getTotalRecyclable() {
		return totalRecyclable;
	}

	public void setTotalRecyclable(Double totalRecyclable) {
		this.totalRecyclable = totalRecyclable;
	}

	public Double getTotalWaste() {
		return totalWaste;
	}

	public void setTotalWaste(Double totalWaste) {
		this.totalWaste = totalWaste;
	}

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public Integer getContributorId() {
		return contributorId;
	}

	public void setContributorId(Integer contributorId) {
		this.contributorId = contributorId;
	}

	public Integer getSchoolProgrammappingId() {
		return schoolProgrammappingId;
	}

	public void setSchoolProgrammappingId(Integer schoolProgrammappingId) {
		this.schoolProgrammappingId = schoolProgrammappingId;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getContributorName() {
		return contributorName;
	}

	public void setContributorName(String contributorName) {
		this.contributorName = contributorName;
	}

}
