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
@Table(name = "tbl_mapdigitalscale")
public class MapDigitalScale implements Serializable {

	@Id
	@GeneratedValue
	private Integer mapdigitalscaleid;

	private Integer digitalscale;

	private Integer sortinguserid;
	
	private Double weight;

	public Integer getSortinguserid() {
		return sortinguserid;
	}

	public void setSortinguserid(Integer sortinguserid) {
		this.sortinguserid = sortinguserid;
	}

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "mappingdate")
	private Date mappingDate;

	public Date getMappingdate() {
		return mappingDate;
	}

	public void setMappingdate(Date mappingdate) {
		this.mappingDate = mappingdate;
	}

	public Integer getMapdigitalscaleid() {
		return mapdigitalscaleid;
	}

	public void setMapdigitalscaleid(Integer mapdigitalscaleid) {
		this.mapdigitalscaleid = mapdigitalscaleid;
	}

	public Integer getDigitalscale() {
		return digitalscale;
	}

	public void setDigitalscale(Integer digitalscale) {
		this.digitalscale = digitalscale;
	}
	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

}
