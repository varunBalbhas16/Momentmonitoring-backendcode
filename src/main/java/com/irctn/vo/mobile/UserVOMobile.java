package com.irctn.vo.mobile;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserVOMobile implements Serializable{
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String roleName;
	
	private String contact;
	
	private String result;
	private Integer userId;    
    private Integer centreId;    
    
    private Integer openTickets;
    
    private Integer currentTicket;
    
    private Integer fieldTrips;
    
    private String doNotDisturb;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private Date doNotdisturbStart;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private Date doNotDisturbEnd;
    
    private String status;
    

	public UserVOMobile(String result) {
		this.result = result;
	}

	public UserVOMobile() {
		// TODO Auto-generated constructor stub
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getCentreId() {
		return centreId;
	}

	public void setCentreId(Integer centreId) {
		this.centreId = centreId;
	}

	public Integer getOpenTickets() {
		return openTickets;
	}

	public void setOpenTickets(Integer openTickets) {
		this.openTickets = openTickets;
	}

	public Integer getCurrentTicket() {
		return currentTicket;
	}

	public void setCurrentTicket(Integer currentTicket) {
		this.currentTicket = currentTicket;
	}

	public Integer getFieldTrips() {
		return fieldTrips;
	}

	public void setFieldTrips(Integer fieldTrips) {
		this.fieldTrips = fieldTrips;
	}

	public String getDoNotDisturb() {
		return doNotDisturb;
	}

	public void setDoNotDisturb(String doNotDisturb) {
		this.doNotDisturb = doNotDisturb;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDoNotdisturbStart() {
		return doNotdisturbStart;
	}

	public void setDoNotdisturbStart(Date doNotdisturbStart) {
		this.doNotdisturbStart = doNotdisturbStart;
	}

	public Date getDoNotDisturbEnd() {
		return doNotDisturbEnd;
	}

	public void setDoNotDisturbEnd(Date doNotDisturbEnd) {
		this.doNotDisturbEnd = doNotDisturbEnd;
	}

}
