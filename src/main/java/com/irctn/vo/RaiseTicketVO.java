/**
 * 
 */
package com.irctn.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Admin
 *
 */
public class RaiseTicketVO implements Serializable {

	//private String centre;
	
	//private String department;
	
	private Integer ticket;
	
	private String contact;
	
	private String email;
	
	private String coordinator;
	
	private Integer coordinatorId;
	
	private Date dateofappointment;
	
	private String hourofappointment;
	
	private Integer departmentId;
	
	private Integer centreId;
	
	private String description;
	
	private Integer dpmId;
	
	private String ticketStatus;
	
	private Integer selfTicket;
	
	private Integer selfTicketId;
	

	/*
	public String getCentre() {
		return centre;
	}

	public void setCentre(String centre) {
		this.centre = centre;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
*/
	
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCoordinator() {
		return coordinator;
	}

	public void setCoordinator(String coordinator) {
		this.coordinator = coordinator;
	}

	public Date getDateofappointment() {
		return dateofappointment;
	}

	public void setDateofappointment(Date dateofappointment) {
		this.dateofappointment = dateofappointment;
	}

	public String getHourofappointment() {
		return hourofappointment;
	}

	public void setHourofappointment(String hourofappointment) {
		this.hourofappointment = hourofappointment;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getCentreId() {
		return centreId;
	}

	public void setCentreId(Integer centreId) {
		this.centreId = centreId;
	}

	public Integer getCoordinatorId() {
		return coordinatorId;
	}

	public void setCoordinatorId(Integer coordinatorId) {
		this.coordinatorId = coordinatorId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDpmId() {
		return dpmId;
	}

	public void setDpmId(Integer dpmId) {
		this.dpmId = dpmId;
	}

	public Integer getTicket() {
		return ticket;
	}

	public void setTicket(Integer ticket) {
		this.ticket = ticket;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public Integer getSelfTicket() {
		return selfTicket;
	}

	public void setSelfTicket(Integer selfTicket) {
		this.selfTicket = selfTicket;
	}

	public Integer getSelfTicketId() {
		return selfTicketId;
	}

	public void setSelfTicketId(Integer selfTicketId) {
		this.selfTicketId = selfTicketId;
	}
	
}
