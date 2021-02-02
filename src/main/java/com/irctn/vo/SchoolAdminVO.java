package com.irctn.vo;

import java.io.Serializable;
import java.util.Date;

public class SchoolAdminVO implements Serializable {

	private Integer schoolAdminId;

	private Date date;
	private String programName;

	private Double totalWeight;
	private Double totalRecycle;
	private Double totalWaste;

	public Integer getSchoolAdminId() {
		return schoolAdminId;
	}

	public void setSchoolAdminId(Integer schoolAdminId) {
		this.schoolAdminId = schoolAdminId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public void setTotalRecycle(Double totalRecycle) {
		this.totalRecycle = totalRecycle;
	}

	public void setTotalWaste(Double totalWaste) {
		this.totalWaste = totalWaste;
	}

	public Double getTotalWeight() {
		return totalWeight;
	}

	public Double getTotalRecycle() {
		return totalRecycle;
	}

	public Double getTotalWaste() {
		return totalWaste;
	}
}
