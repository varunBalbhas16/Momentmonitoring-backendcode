package com.irctn.vo;

import java.io.Serializable;

public class CentreDepartmentUserVO implements Serializable {

	private Integer centerDepartmentUserId;
	
	private Integer centreDepartmentId;
	
	private Integer userId;
	
	private Integer roleId;
	
	private String userName;
	
	private String userContactNumber;
	
	private String roleName;
	
	private String departmentName;
	
	private String centreName;

	public Integer getCenterDepartmentUserId() {
		return centerDepartmentUserId;
	}

	public void setCenterDepartmentUserId(Integer centerDepartmentUserId) {
		this.centerDepartmentUserId = centerDepartmentUserId;
	}

	public Integer getCentreDepartmentId() {
		return centreDepartmentId;
	}

	public void setCentreDepartmentId(Integer centreDepartmentId) {
		this.centreDepartmentId = centreDepartmentId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getCentreName() {
		return centreName;
	}

	public void setCentreName(String centreName) {
		this.centreName = centreName;
	}

	public String getUserContactNumber() {
		return userContactNumber;
	}

	public void setUserContactNumber(String userContactNumber) {
		this.userContactNumber = userContactNumber;
	}
	
}
