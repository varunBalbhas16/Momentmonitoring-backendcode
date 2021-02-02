package com.irctn.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_retailersortingboxcategory")
public class RetailerSortingBoxCategory implements Serializable {
	
	@Id
	@GeneratedValue
	private Integer retailersortingboxcategoryid;
	
	private Integer retailersortingboxid;
	private Integer retailercategoryid;
	private Integer retailersubcategoryid;
	
	private Double weight;
	
	private Integer items;
	
	@ManyToOne
	@JoinColumn(name = "retailersortingboxid",insertable = false,updatable = false)
	private RetailerSortingBox retailerSortingBox;
	
	@ManyToOne
	@JoinColumn(name = "retailersubcategoryid",insertable = false,updatable = false)
	private RetailerSubCategory retailerSubCategory;
	
	@ManyToOne
	@JoinColumn(name = "retailercategoryid",insertable = false,updatable = false)
	private RetailerCategory retailerCategory;

	public Integer getRetailersortingboxcategoryid() {
		return retailersortingboxcategoryid;
	}

	public void setRetailersortingboxcategoryid(Integer retailersortingboxcategoryid) {
		this.retailersortingboxcategoryid = retailersortingboxcategoryid;
	}

	public Integer getRetailersortingboxid() {
		return retailersortingboxid;
	}

	public void setRetailersortingboxid(Integer retailersortingboxid) {
		this.retailersortingboxid = retailersortingboxid;
	}

	public Integer getRetailercategoryid() {
		return retailercategoryid;
	}

	public void setRetailercategoryid(Integer retailercategoryid) {
		this.retailercategoryid = retailercategoryid;
	}

	public Integer getRetailersubcategoryid() {
		return retailersubcategoryid;
	}

	public void setRetailersubcategoryid(Integer retailersubcategoryid) {
		this.retailersubcategoryid = retailersubcategoryid;
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
	
	public String toSring(){
		StringBuilder builder = new StringBuilder();
		builder.append("retailersortingboxcategoryid-->").append(retailersortingboxcategoryid.intValue()).append(" \n ")
				.append("retailersortingboxid-->").append(retailersortingboxid.intValue()).append(" \n ")
				.append("retailercategoryid-->").append(retailercategoryid.intValue()).append(" \n ")
				.append("retailersubcategoryid-->").append(retailersubcategoryid.intValue()).append(" \n ")
				.append("weight-->").append(weight.doubleValue()).append(" \n ")
				.append("items-->").append(items.intValue()).append(" \n ")
				;
		return builder.toString();
	}

}
