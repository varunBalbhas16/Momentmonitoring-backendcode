package com.irctn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tbl_clothescollection")
public class ClothesCollection implements Serializable {

	@Id
	@GeneratedValue
	private Integer collectionid;

	private String type;

	@Column(name = "collection_userid")
	private Integer collectionUserId;

	private Integer contributorid;
	private Integer programid;
	private Integer noofbags;

	@Column(name = "collection_totalweight")
	private Double collectionTotalWeight;

	private Integer batchnumber;

	@Column(name = "collection_date", columnDefinition = "DATETIME", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date collectionDate;

	private Integer status;
	
	 @OneToOne(fetch = FetchType.LAZY,
	            cascade =  CascadeType.ALL,
	            mappedBy = "clothesCollection")
	private Supervisor supervisor;
	 
	 @ManyToOne
	 @JoinColumn(name = "programid",insertable = false,updatable = false)
	 private Program program;
	 
	 @ManyToOne
	 @JoinColumn(name = "contributorid",insertable = false,updatable = false)
	 private Contributor contributor;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCollectionid() {
		return collectionid;
	}

	public void setCollectionid(Integer collectionid) {
		this.collectionid = collectionid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getCollectionUserId() {
		return collectionUserId;
	}

	public void setCollectionUserId(Integer collectionUserId) {
		this.collectionUserId = collectionUserId;
	}

	public Integer getContributorid() {
		return contributorid;
	}

	public void setContributorid(Integer contributorid) {
		this.contributorid = contributorid;
	}

	public Integer getProgramid() {
		return programid;
	}

	public void setProgramid(Integer programid) {
		this.programid = programid;
	}

	public Integer getNoofbags() {
		return noofbags;
	}

	public void setNoofbags(Integer noofbags) {
		this.noofbags = noofbags;
	}

	public Double getCollectionTotalWeight() {
		return collectionTotalWeight;
	}

	public void setCollectionTotalWeight(Double collectionTotalWeight) {
		this.collectionTotalWeight = collectionTotalWeight;
	}

	public Integer getBatchnumber() {
		return batchnumber;
	}

	public void setBatchnumber(Integer batchnumber) {
		this.batchnumber = batchnumber;
	}

	public Date getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = collectionDate;
	}

}
