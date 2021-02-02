package com.irctn.vo;

import java.io.Serializable;

public class UserVO implements Serializable {

	private String firstName;
	private String email;
	private Integer status;
	private String roleName;
	private String lastName;
	private String contact;
	private String password;
	private String contributorName;

	private String result;
	private Integer userId;    
    private Integer centreId;    

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public UserVO(String result) {
		this.result = result;
	}

	public UserVO() {

	}

	public UserVO(String firstname, String email) {
		this.firstName = firstname;
		this.email = email;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Integer getUserid() {
		return userId;
	}

	public void setUserid(Integer userid) {
		this.userId = userid;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public UserVO(Integer status) {
		this.status = status;
	}

	public String getContributorName() {
		return contributorName;
	}

	public void setContributorName(String contributorName) {
		this.contributorName = contributorName;
	}

	public Integer getCentreId() {
		return centreId;
	}

	public void setCentreId(Integer centreId) {
		this.centreId = centreId;
	}
}
