package com.irctn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "tbl_schoolsortingprocess")
public class SchoolSortingProcess implements Serializable {

	@Id
	@GeneratedValue
	private Integer schoolsortingprocessid;

	private Integer clothessortingid;
	private Integer schoolstudentcontributionid;
	private Integer sortingcategoryid;
	private Integer sortinguserid;

	private Double bagweight;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sortingdate")
	private Date sortingDate;

	public Date getSortingDate() {
		return sortingDate;
	}

	public void setSortingDate(Date sortingDate) {
		this.sortingDate = sortingDate;
	}

	public Integer getSchoolsortingprocessid() {
		return schoolsortingprocessid;
	}

	public void setSchoolsortingprocessid(Integer schoolsortingprocessid) {
		this.schoolsortingprocessid = schoolsortingprocessid;
	}

	public Integer getClothessortingid() {
		return clothessortingid;
	}

	public void setClothessortingid(Integer clothessortingid) {
		this.clothessortingid = clothessortingid;
	}

	public Integer getSchoolstudentcontributionid() {
		return schoolstudentcontributionid;
	}

	public void setSchoolstudentcontributionid(Integer schoolstudentcontributionid) {
		this.schoolstudentcontributionid = schoolstudentcontributionid;
	}

	public Integer getSortingcategoryid() {
		return sortingcategoryid;
	}

	public void setSortingcategoryid(Integer sortingcategoryid) {
		this.sortingcategoryid = sortingcategoryid;
	}

	public Integer getSortinguserid() {
		return sortinguserid;
	}

	public void setSortinguserid(Integer sortinguserid) {
		this.sortinguserid = sortinguserid;
	}

	public Double getBagweight() {
		return bagweight;
	}

	public void setBagweight(Double bagweight) {
		this.bagweight = bagweight;
	}

}
