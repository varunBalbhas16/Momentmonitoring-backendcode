package com.irctn.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_retailersubcategory")
public class RetailerSubCategory implements Serializable {
	
	@Id
	@GeneratedValue
	private Integer retailersubcategoryid;
	
	private String retailersubcategoryname;

	public Integer getRetailersubcategoryid() {
		return retailersubcategoryid;
	}

	public void setRetailersubcategoryid(Integer retailersubcategoryid) {
		this.retailersubcategoryid = retailersubcategoryid;
	}

	public String getRetailersubcategoryname() {
		return retailersubcategoryname;
	}

	public void setRetailersubcategoryname(String retailersubcategoryname) {
		this.retailersubcategoryname = retailersubcategoryname;
	}

}
