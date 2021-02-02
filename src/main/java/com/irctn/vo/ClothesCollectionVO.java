package com.irctn.vo;

import java.io.Serializable;
import java.time.Month;
import java.util.Date;

public class ClothesCollectionVO implements Serializable {

	private Integer collectionId;

	private String type;

	private Integer collectionUserId;
	private Integer schoolProgramMappingId;

	// Contributor Details
	private Integer contributorId;
	private String name;
	private String contactPerson;
	private String mobileNumber;
	private String address;
	private String landmark;
	private String email;

	// Program Details
	private Integer programId;
	private String programName;
	private String purpose;
	private Date startDate;
	private Date endDate;

	private Integer noOfBags;

	private Double collectionTotalWeight;
	private Double totalWaste;
	private Double totalRecyclable;

	private Integer batchNumber;

	private Date collectionDate;

	private Integer status;

	private Integer clothesSortingId;
	private Integer studentContributionId;
	private Integer sortingCategoryId;
	
	private Integer date;
	private String month;
	
	private String errorCode;
	private String errorMsg;
	
	
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Integer getSchoolProgramMappingId() {
		return schoolProgramMappingId;
	}

	public void setSchoolProgramMappingId(Integer schoolProgramMappingId) {
		this.schoolProgramMappingId = schoolProgramMappingId;
	}
	
	public Double getTotalWaste() {
		return totalWaste;
	}

	public void setTotalWaste(Double totalWaste) {
		this.totalWaste = totalWaste;
	}

	public Double getTotalRecyclable() {
		return totalRecyclable;
	}

	public void setTotalRecyclable(Double totalRecyclable) {
		this.totalRecyclable = totalRecyclable;
	}

	public Integer getClothesSortingId() {
		return clothesSortingId;
	}

	public void setClothesSortingId(Integer clothesSortingId) {
		this.clothesSortingId = clothesSortingId;
	}

	public Integer getStudentContributionId() {
		return studentContributionId;
	}

	public void setStudentContributionId(Integer studentContributionId) {
		this.studentContributionId = studentContributionId;
	}

	public Integer getSortingCategoryId() {
		return sortingCategoryId;
	}

	public void setSortingCategoryId(Integer sortingCategoryId) {
		this.sortingCategoryId = sortingCategoryId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = collectionDate;
	}

	public Integer getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(Integer collectionId) {
		this.collectionId = collectionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getCollectionUserId() {
		return collectionUserId;
	}

	public void setCollectionUserId(Integer collectionUserId) {
		this.collectionUserId = collectionUserId;
	}

	public Integer getContributorId() {
		return contributorId;
	}

	public void setContributorId(Integer contributorId) {
		this.contributorId = contributorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
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

	public Integer getNoOfBags() {
		return noOfBags;
	}

	public void setNoOfBags(Integer noOfBags) {
		this.noOfBags = noOfBags;
	}

	public Integer getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Double getCollectionTotalWeight() {
		return collectionTotalWeight;
	}

	public void setCollectionTotalWeight(Double collectionTotalWeight) {
		this.collectionTotalWeight = collectionTotalWeight;
	}
	public Integer getDate() {
		return date;
	}

	public void setDate(Integer date) {
		this.date = date;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	

}
