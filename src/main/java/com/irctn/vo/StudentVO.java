package com.irctn.vo;

import java.io.Serializable;

public class StudentVO implements Serializable {

	private Integer studentId;
	private Integer contributorId;

	private String studentName;
	private String grade;
	private String rollNumber;

	private String studentClassLeader;

	private Double totalWeight;
	private Double recyclable;
	
	private String house;
	private String barcode;


	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getStudentClassLeader() {
		return studentClassLeader;
	}

	public void setStudentClassLeader(String studentClassLeader) {
		this.studentClassLeader = studentClassLeader;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public Double getRecyclable() {
		return recyclable;
	}

	public void setRecyclable(Double recyclable) {
		this.recyclable = recyclable;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Integer getContributorId() {
		return contributorId;
	}

	public void setContributorId(Integer contributorId) {
		this.contributorId = contributorId;
	}

	public String getRollNumber() {
		return rollNumber;
	}

	public void setRollNumber(String rollNumber) {
		this.rollNumber = rollNumber;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	

}
