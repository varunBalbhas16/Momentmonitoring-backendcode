package com.irctn.vo;

import java.io.Serializable;
import java.util.Date;

public class SupervisorVO implements Serializable {

	private Integer clothesSortingId;

	private Integer collectionId;
	private Integer supervisorId;
	private Integer numberOfBags;

	private Double totalWeight;
	private Double totalWaste;
	private Double totalReusable;
	
	private Integer batchNumber;
    
	
	private Date startDate;
	private Date endDate;

	private Integer programId;
	
	private String statusName;

	// Contributor Details
	private String name;
	private String type;
	private Integer status;
	
	// User Details
	private String firstName;
	private String email;
	private String roleName;
	private String lastName;
	private String contact;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getClothesSortingId() {
		return clothesSortingId;
	}

	public void setClothesSortingId(Integer clothesSortingId) {
		this.clothesSortingId = clothesSortingId;
	}

	public Integer getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(Integer collectionId) {
		this.collectionId = collectionId;
	}

	public Integer getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(Integer supervisorId) {
		this.supervisorId = supervisorId;
	}

	public Integer getNumberOfBags() {
		return numberOfBags;
	}

	public void setNumberOfBags(Integer numberOfBags) {
		this.numberOfBags = numberOfBags;
	}

	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public Double getTotalWaste() {
		return totalWaste;
	}

	public void setTotalWaste(Double totalWaste) {
		this.totalWaste = totalWaste;
	}

	public Double getTotalReusable() {
		return totalReusable;
	}

	public void setTotalReusable(Double totalReusable) {
		this.totalReusable = totalReusable;
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

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}
	public Integer getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
	}
	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

}
