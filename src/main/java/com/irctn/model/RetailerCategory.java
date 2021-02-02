package com.irctn.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_retailercategory")
public class RetailerCategory implements Serializable {
	
	@Id
	@GeneratedValue
	private Integer retailercategoryid;
	
	private String categoryname;

	public Integer getRetailercategoryid() {
		return retailercategoryid;
	}

	public void setRetailercategoryid(Integer retailercategoryid) {
		this.retailercategoryid = retailercategoryid;
	}

	public String getCategoryname() {
		return categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

}
