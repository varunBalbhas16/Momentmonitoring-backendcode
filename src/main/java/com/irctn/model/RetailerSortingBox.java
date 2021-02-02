package com.irctn.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "tbl_retailersotingbox")
public class RetailerSortingBox implements Serializable {

	@Id
	@GeneratedValue
	private Integer retailersortingboxid;

	private Integer clothessortingprocessid;

	private Integer boxnumber;

	private Double boxweight;
	
	private Integer sortingagentid;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationdate;
	
	@ManyToMany
	@JoinColumn(name = "clothessortingprocessid")
	private List<Supervisor> supervisors;

	public Integer getSortingagentid() {
		return sortingagentid;
	}

	public void setSortingagentid(Integer sortingagentid) {
		this.sortingagentid = sortingagentid;
	}

	public Date getCreationdate() {
		return creationdate;
	}

	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}

	

	public Integer getRetailersortingboxid() {
		return retailersortingboxid;
	}

	public void setRetailersortingboxid(Integer retailersortingboxid) {
		this.retailersortingboxid = retailersortingboxid;
	}

	public Integer getClothessortingprocessid() {
		return clothessortingprocessid;
	}

	public void setClothessortingprocessid(Integer clothessortingprocessid) {
		this.clothessortingprocessid = clothessortingprocessid;
	}

	public Integer getBoxnumber() {
		return boxnumber;
	}

	public void setBoxnumber(Integer boxnumber) {
		this.boxnumber = boxnumber;
	}

	public Double getBoxweight() {
		return boxweight;
	}

	public void setBoxweight(Double boxweight) {
		this.boxweight = boxweight;
	}


}
