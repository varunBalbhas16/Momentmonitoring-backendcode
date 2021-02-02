package com.irctn.vo;

import java.io.Serializable;

public class ClothesCategoryVO implements Serializable {

	private Integer sortingCategoryId;

	private Double category1;
	private Double category2;
	private Double category3;
	private Double category4;
	private Double category5;
	private Double category6;
	private Double category7;
	private Double category8;
	private Double category9;
	private Double category10;
	private Double category11;
	private Double category12;
	private Double category13;
	private Double category14;
	private Double category15;
	private Double category16;
	private Double category17;
	private Double category18;
	private Double category19;

	private Double totalWaste;
	private Double totalReusable;

	private Integer batchNumber;
	private String batchType;
	private Integer schoolStudentContributionId;
	private Integer sortingUserId;
	private Integer numberOfItems;

	
	public Integer getNumberOfItems() {
		return numberOfItems;
	}

	public void setNumberOfItems(Integer numberOfItems) {
		this.numberOfItems = numberOfItems;
	}

	public Integer getSortingUserId() {
		return sortingUserId;
	}

	public void setSortingUserId(Integer sortingUserId) {
		this.sortingUserId = sortingUserId;
	}

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

	public Integer getSchoolStudentContributionId() {
		return schoolStudentContributionId;
	}

	public void setSchoolStudentContributionId(Integer schoolStudentContributionId) {
		this.schoolStudentContributionId = schoolStudentContributionId;
	}

	public Double getCategory11() {
		return category11;
	}

	public void setCategory11(Double category11) {
		this.category11 = category11;
	}

	public Double getCategory12() {
		return category12;
	}

	public void setCategory12(Double category12) {
		this.category12 = category12;
	}

	public Double getCategory13() {
		return category13;
	}

	public void setCategory13(Double category13) {
		this.category13 = category13;
	}

	public Double getCategory14() {
		return category14;
	}

	public void setCategory14(Double category14) {
		this.category14 = category14;
	}

	public Double getCategory15() {
		return category15;
	}

	public void setCategory15(Double category15) {
		this.category15 = category15;
	}

	public Double getCategory16() {
		return category16;
	}

	public void setCategory16(Double category16) {
		this.category16 = category16;
	}

	public Double getCategory17() {
		return category17;
	}

	public void setCategory17(Double category17) {
		this.category17 = category17;
	}

	public Double getCategory18() {
		return category18;
	}

	public void setCategory18(Double category18) {
		this.category18 = category18;
	}

	public Double getCategory19() {
		return category19;
	}

	public void setCategory19(Double category19) {
		this.category19 = category19;
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

	public Double getCategory1() {
		return category1;
	}

	public void setCategory1(Double category1) {
		this.category1 = category1;
	}

	public Double getCategory2() {
		return category2;
	}

	public void setCategory2(Double category2) {
		this.category2 = category2;
	}

	public Double getCategory3() {
		return category3;
	}

	public void setCategory3(Double category3) {
		this.category3 = category3;
	}

	public Double getCategory4() {
		return category4;
	}

	public void setCategory4(Double category4) {
		this.category4 = category4;
	}

	public Double getCategory5() {
		return category5;
	}

	public void setCategory5(Double category5) {
		this.category5 = category5;
	}

	public Double getCategory6() {
		return category6;
	}

	public void setCategory6(Double category6) {
		this.category6 = category6;
	}

	public Double getCategory7() {
		return category7;
	}

	public void setCategory7(Double category7) {
		this.category7 = category7;
	}

	public Double getCategory8() {
		return category8;
	}

	public void setCategory8(Double category8) {
		this.category8 = category8;
	}

	public Double getCategory9() {
		return category9;
	}

	public void setCategory9(Double category9) {
		this.category9 = category9;
	}

	public Double getCategory10() {
		return category10;
	}

	public void setCategory10(Double category10) {
		this.category10 = category10;
	}

	public Integer getSortingCategoryId() {
		return sortingCategoryId;
	}

	public void setSortingCategoryId(Integer sortingCategoryId) {
		this.sortingCategoryId = sortingCategoryId;
	}

	/*
	 * private Integer sortingCategoryId;

	private Double category1;
	private Double category2;
	private Double category3;
	private Double category4;
	private Double category5;
	private Double category6;
	private Double category7;
	private Double category8;
	private Double category9;
	private Double category10;
	private Double category11;
	private Double category12;
	private Double category13;
	private Double category14;
	private Double category15;
	private Double category16;
	private Double category17;
	private Double category18;
	private Double category19;

	private Double totalWaste;
	private Double totalReusable;

	private Integer batchNumber;
	private String batchType;
	private Integer schoolStudentContributionId;
	private Integer sortingUserId;
	private Integer numberOfItems;
	 */
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n Clothes Category VO   ---------------------\n" )
				.append(" sortingCategoryId :").append(sortingCategoryId).append("\n")
				.append(" batchNumber :").append(batchNumber).append("\n")
				.append(" batchType :").append(batchType).append("\n")
				.append(" schoolStudentContributionId :").append(schoolStudentContributionId).append("\n")
				.append(" sortingUserId :").append(sortingUserId).append("\n")
				.append(" totalReusable :").append(totalReusable).append("\n")
				.append(" sortingUserId :").append(sortingUserId).append("\n")
				.append(" category1 :").append(category1).append("\n")
				.append(" category2 :").append(category2).append("\n")			
				;
				
		return builder.toString();
	}
}
