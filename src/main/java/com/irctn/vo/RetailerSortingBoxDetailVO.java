package com.irctn.vo;

import java.io.Serializable;

public class RetailerSortingBoxDetailVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//these are request variables for posting retailer categories weights and items
	private Double weight;	
	private Integer items;	
	private String categoryName;
	private String retailerSubCategoryName;
	
	//These should be taken from DB and filled in
	private Integer retailerSortingBoxCategoryId;
	private Integer retailerCategoryId;
	private Integer retailerSubcategoryId;
	private Integer retailerSortingBoxId;
	
	public Integer getRetailerSortingBoxId() {
		return retailerSortingBoxId;
	}
	public void setRetailerSortingBoxId(Integer retailerSortingBoxId) {
		this.retailerSortingBoxId = retailerSortingBoxId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Integer getItems() {
		return items;
	}
	public void setItems(Integer items) {
		this.items = items;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getRetailerSubCategoryName() {
		return retailerSubCategoryName;
	}
	public void setRetailerSubCategoryName(String retailerSubCategoryName) {
		this.retailerSubCategoryName = retailerSubCategoryName;
	}
	public Integer getRetailerSortingBoxCategoryId() {
		return retailerSortingBoxCategoryId;
	}
	public void setRetailerSortingBoxCategoryId(Integer retailerSortingBoxCategoryId) {
		this.retailerSortingBoxCategoryId = retailerSortingBoxCategoryId;
	}
	public Integer getRetailerCategoryId() {
		return retailerCategoryId;
	}
	public void setRetailerCategoryId(Integer retailerCategoryId) {
		this.retailerCategoryId = retailerCategoryId;
	}
	public Integer getRetailerSubcategoryId() {
		return retailerSubcategoryId;
	}
	public void setRetailerSubcategoryId(Integer retailerSubcategoryId) {
		this.retailerSubcategoryId = retailerSubcategoryId;
	} 
}
