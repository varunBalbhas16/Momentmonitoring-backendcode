package com.irctn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "tbl_clothessorting")
public class Supervisor implements Serializable {

	@Id
	@GeneratedValue
	private Integer clothessortingid;

	private Integer collectionid;
	private Integer supervisorid;
	private Integer numberofbags;
	private Integer status;

	private Double totalweight;
	private Double totalwaste;
	private Double totalreusable;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date startdate;
	private Date enddate;
	
	@OneToOne
	@JoinColumn(name = "collectionid",insertable = false, updatable = false)
	private ClothesCollection clothesCollection;

	public Integer getClothessortingid() {
		return clothessortingid;
	}

	public void setClothessortingid(Integer clothessortingid) {
		this.clothessortingid = clothessortingid;
	}

	public Integer getCollectionid() {
		return collectionid;
	}

	public void setCollectionid(Integer collectionid) {
		this.collectionid = collectionid;
	}

	public Integer getSupervisorid() {
		return supervisorid;
	}

	public void setSupervisorid(Integer supervisorid) {
		this.supervisorid = supervisorid;
	}

	public Integer getNumberofbags() {
		return numberofbags;
	}

	public void setNumberofbags(Integer numberofbags) {
		this.numberofbags = numberofbags;
	}

	public Double getTotalweight() {
		return totalweight;
	}

	public void setTotalweight(Double totalweight) {
		this.totalweight = totalweight;
	}

	public Double getTotalwaste() {
		return totalwaste;
	}

	public void setTotalwaste(Double totalwaste) {
		this.totalwaste = totalwaste;
	}

	public Double getTotalreusable() {
		return totalreusable;
	}

	public void setTotalreusable(Double totalreusable) {
		this.totalreusable = totalreusable;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
