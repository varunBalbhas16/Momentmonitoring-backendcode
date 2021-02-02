package com.irctn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "tbl_schoolstudentcontribution")
public class SchoolStudentContribution implements Serializable {

	@Id
	@GeneratedValue
	private Integer schoolstudentcontributionid;
	private Integer schoolprogrammappingid;
	private Integer schoolstudentid;
	
	private Double bagweight;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "contributiondate")
	private Date contributionDate;
	
	/*@ManyToOne
	@JoinColumn(name = "schoolstudentid", insertable = false, updatable = false)
	private Student student;
*/
	public Integer getSchoolstudentcontributionid() {
		return schoolstudentcontributionid;
	}

	public void setSchoolstudentcontributionid(Integer schoolstudentcontributionid) {
		this.schoolstudentcontributionid = schoolstudentcontributionid;
	}

	public Integer getSchoolstudentid() {
		return schoolstudentid;
	}

	public void setSchoolstudentid(Integer schoolstudentid) {
		this.schoolstudentid = schoolstudentid;
	}

	public Double getBagweight() {
		return bagweight;
	}

	public void setBagweight(Double bagweight) {
		this.bagweight = bagweight;
	}

	public Date getContributionDate() {
		return contributionDate;
	}

	public void setContributionDate(Date contributionDate) {
		this.contributionDate = contributionDate;
	}

	public Integer getSchoolprogrammappingid() {
		return schoolprogrammappingid;
	}

	public void setSchoolprogrammappingid(Integer schoolprogrammappingid) {
		this.schoolprogrammappingid = schoolprogrammappingid;
	}

}
