package com.irctn.vo;

import java.util.List;

public class RetailerSortingBoxVO {
	
	private Integer batchNumber;
	private String batchType;
	
	private Integer retailerSortingBoxId;
	private Integer clothesSortingprocessId;
	private Integer boxNumber;	
	private Double boxWeight;
	
	private Double totalWeight;
	
	public Double getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}
	private Integer sortingUserId;

	private List<RetailerSortingBoxDetailVO> sortingCategoryDetailsList;
	
	public Integer getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
	}
	public String getBatchType() {
		return batchType;
	}
	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}
	
	public Integer getRetailerSortingBoxId() {
		return retailerSortingBoxId;
	}
	public void setRetailerSortingBoxId(Integer retailerSortingBoxId) {
		this.retailerSortingBoxId = retailerSortingBoxId;
	}
	public Integer getClothesSortingprocessId() {
		return clothesSortingprocessId;
	}
	public void setClothesSortingprocessId(Integer clothesSortingprocessId) {
		this.clothesSortingprocessId = clothesSortingprocessId;
	}
	public Integer getBoxNumber() {
		return boxNumber;
	}
	public void setBoxNumber(Integer boxNumber) {
		this.boxNumber = boxNumber;
	}	
	public Double getBoxWeight() {
		return boxWeight;
	}
	public void setBoxWeight(Double boxWeight) {
		this.boxWeight = boxWeight;
	}
	public List<RetailerSortingBoxDetailVO> getSortingCategoryDetailsList() {
		return sortingCategoryDetailsList;
	}
	public void setSortingCategoryDetailsList(List<RetailerSortingBoxDetailVO> sortingCategoryDetailsList) {
		this.sortingCategoryDetailsList = sortingCategoryDetailsList;
	}
	public Integer getSortingUserId() {
		return sortingUserId;
	}
	public void setSortingUserId(Integer sortingUserId) {
		this.sortingUserId = sortingUserId;
	}
}
