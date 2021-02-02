package com.irctn.vo;

import java.io.Serializable;
import java.util.Date;

public class SchoolStudentContributionVO implements Serializable {

	private Integer schoolStudentContributionId;
	private Integer schoolStudentId;
	private Integer programId;

	private Date contributionDate;

	// studentDetails
	private String name;
	private String grade;
	private Integer rollNumber;
	private String classLeader;
	private String house;
	
	private Double bagWeight;
	private Integer schoolProgrammappingId;
	private String barcode;

	public Integer getSchoolProgrammappingId() {
		return schoolProgrammappingId;
	}

	public void setSchoolProgrammappingId(Integer schoolProgrammappingId) {
		this.schoolProgrammappingId = schoolProgrammappingId;
	}

	public Double getBagWeight() {
		return bagWeight;
	}

	public void setBagWeight(Double bagWeight) {
		this.bagWeight = bagWeight;
	}

	public Integer getSchoolStudentContributionId() {
		return schoolStudentContributionId;
	}

	public void setSchoolStudentContributionId(Integer schoolStudentContributionId) {
		this.schoolStudentContributionId = schoolStudentContributionId;
	}

	public Integer getSchoolStudentId() {
		return schoolStudentId;
	}

	public void setSchoolStudentId(Integer schoolStudentId) {
		this.schoolStudentId = schoolStudentId;
	}

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public Date getContributionDate() {
		return contributionDate;
	}

	public void setContributionDate(Date contributionDate) {
		this.contributionDate = contributionDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}


	public Integer getRollNumber() {
		return rollNumber;
	}

	public void setRollNumber(Integer rollNumber) {
		this.rollNumber = rollNumber;
	}

	public String getClassLeader() {
		return classLeader;
	}

	public void setClassLeader(String classLeader) {
		this.classLeader = classLeader;
	}

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

}
