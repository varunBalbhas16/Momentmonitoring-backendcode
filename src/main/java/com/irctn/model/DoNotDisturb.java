package com.irctn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_dnd")
public class DoNotDisturb implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Integer dndid;
	
	private Integer userid;
	
	private Date dndstart;
	
	private Date dndstop;
	
	private Integer status;

	public Integer getDndid() {
		return dndid;
	}

	public void setDndid(Integer dndid) {
		this.dndid = dndid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Date getDndstart() {
		return dndstart;
	}

	public void setDndstart(Date dndstart) {
		this.dndstart = dndstart;
	}

	public Date getDndstop() {
		return dndstop;
	}

	public void setDndstop(Date dndstop) {
		this.dndstop = dndstop;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
