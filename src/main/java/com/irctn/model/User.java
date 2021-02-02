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

@Entity
@Table(name = "tbl_user")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Integer userid;

	private String firstname;
	private String lastname;
	private String email;
	private String password;
	private String contact;

	private Integer status;
	private Integer contributorid;

	private Integer failedattempts;

	@ManyToOne
	@JoinColumn(name = "roleid", insertable = false, updatable = false)
	private Role role;

	@Column(name = "lastlogin", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;

	private Integer roleid;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getFailedattempts() {
		return failedattempts;
	}

	public void setFailedattempts(Integer failedattempts) {
		this.failedattempts = failedattempts;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Integer getRoleid() {
		return roleid;
	}

	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}

	public Integer getContributorid() {
		return contributorid;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setContributorid(Integer contributorid) {
		this.contributorid = contributorid;
	}

}
