package com.irctn.vo;

import java.io.Serializable;

public class SchoolSortingVO implements Serializable {

	private Integer schoolSortingProcessId;
	private Integer clothesSortingId;
	private Integer schoolStudentContributionId;
	private Double bagWeight;
	private Integer sortingDate;
	private Integer sortingCategoryId;
	private Integer schoolProgramMappingId;

	private Integer sortingUserId;
	
	private Integer contributorId;
	private Integer studentId;

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public Integer getContributorId() {
		return contributorId;
	}

	public void setContributorId(Integer contributorId) {
		this.contributorId = contributorId;
	}

	public Integer getSortingUserId() {
		return sortingUserId;
	}

	public void setSortingUserId(Integer sortingUserId) {
		this.sortingUserId = sortingUserId;
	}

	public Integer getSchoolSortingProcessId() {
		return schoolSortingProcessId;
	}

	public void setSchoolSortingProcessId(Integer schoolSortingProcessId) {
		this.schoolSortingProcessId = schoolSortingProcessId;
	}

	public Integer getClothesSortingId() {
		return clothesSortingId;
	}

	public void setClothesSortingId(Integer clothesSortingId) {
		this.clothesSortingId = clothesSortingId;
	}

	public Integer getSchoolStudentContributionId() {
		return schoolStudentContributionId;
	}

	public void setSchoolStudentContributionId(Integer schoolStudentContributionId) {
		this.schoolStudentContributionId = schoolStudentContributionId;
	}

	public Double getBagWeight() {
		return bagWeight;
	}

	public void setBagWeight(Double bagWeight) {
		this.bagWeight = bagWeight;
	}

	public Integer getSortingDate() {
		return sortingDate;
	}

	public void setSortingDate(Integer sortingDate) {
		this.sortingDate = sortingDate;
	}

	public Integer getSortingCategoryId() {
		return sortingCategoryId;
	}

	public void setSortingCategoryId(Integer sortingCategoryId) {
		this.sortingCategoryId = sortingCategoryId;
	}
	public Integer getSchoolProgramMappingId() {
		return schoolProgramMappingId;
	}

	public void setSchoolProgramMappingId(Integer schoolProgramMappingId) {
		this.schoolProgramMappingId = schoolProgramMappingId;
	}

	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n School Sorting VO ---------------------\n" )
				.append(" schoolSortingProcessId :").append(schoolSortingProcessId).append("\n")
				.append(" clothesSortingId :").append(clothesSortingId).append("\n")
				.append(" schoolStudentContributionId :").append(schoolStudentContributionId).append("\n")
				.append(" bagWeight :").append(bagWeight).append("\n")
				.append(" sortingCategoryId :").append(sortingCategoryId).append("\n")
				.append(" schoolProgramMappingId :").append(schoolProgramMappingId).append("\n")
				.append(" sortingUserId :").append(sortingUserId).append("\n")
				.append(" contributorId :").append(contributorId).append("\n")
				.append(" studentId :").append(studentId).append("\n")			
				;
				
		return builder.toString();
	}
}
