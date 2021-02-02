package com.irctn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_escalation_audit")
public class EscalationAudit implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	private Integer ticket;
	private Integer dpmid;
	private String emailid;
	private Date createdat;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTicket() {
		return ticket;
	}
	public void setTicket(Integer ticket) {
		this.ticket = ticket;
	}
	public Integer getDpmid() {
		return dpmid;
	}
	public void setDpmid(Integer dpmid) {
		this.dpmid = dpmid;
	}
	public String getEmailid() {
		return emailid;
	}
	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}
	public Date getCreatedat() {
		return createdat;
	}
	public void setCreatedat(Date createdat) {
		this.createdat = createdat;
	}
}
