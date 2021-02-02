package com.irctn.vo;

import java.io.Serializable;

public class RetailerSortingVO implements Serializable {

	private Integer retailerSortingProcessId;
	private Integer clothesSortingId;
	private Integer sortingCategoryId;//delete this if yo udont want it. Check this later.
	private Integer sortingUserId;

	public Integer getSortingUserId() {
		return sortingUserId;
	}

	public void setSortingUserId(Integer sortingUserId) {
		this.sortingUserId = sortingUserId;
	}

	public Integer getRetailerSortingProcessId() {
		return retailerSortingProcessId;
	}

	public void setRetailerSortingProcessId(Integer retailerSortingProcessId) {
		this.retailerSortingProcessId = retailerSortingProcessId;
	}

	public Integer getClothesSortingId() {
		return clothesSortingId;
	}

	public void setClothesSortingId(Integer clothesSortingId) {
		this.clothesSortingId = clothesSortingId;
	}

	public Integer getSortingCategoryId() {
		return sortingCategoryId;
	}

	public void setSortingCategoryId(Integer sortingCategoryId) {
		this.sortingCategoryId = sortingCategoryId;
	}

}
