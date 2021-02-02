package com.irctn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_ticket")
public class Ticket implements Serializable {

	@Id
	@GeneratedValue
	private Integer ticketid;
	
	private Integer ticket;
	private Integer centreid;
	private Integer departmentid;
	private String description;
	private Integer coordinatorid;
	private String coordinatorcontact;
	private Date appointment;
	private String status;
	private Integer dpmid;
	private Date startdatetime;
	private Date enddatetime;
	private String photo;
	private String signature;
	
	private Integer selfticket;
	private Integer selfticketid;
	
	public Integer getTicketid() {
		return ticketid;
	}
	public void setTicketid(Integer ticketid) {
		this.ticketid = ticketid;
	}
	public Integer getTicket() {
		return ticket;
	}
	
	public void setTicket(Integer ticket) {
		this.ticket = ticket;
	}
	public Integer getCentreid() {
		return centreid;
	}
	public void setCentreid(Integer centreid) {
		this.centreid = centreid;
	}
	public Integer getDepartmentid() {
		return departmentid;
	}
	public void setDepartmentid(Integer departmentid) {
		this.departmentid = departmentid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getCoordinatorid() {
		return coordinatorid;
	}
	public void setCoordinatorid(Integer coordinatorid) {
		this.coordinatorid = coordinatorid;
	}
	public String getCoordinatorcontact() {
		return coordinatorcontact;
	}
	public void setCoordinatorcontact(String coordinatorcontact) {
		this.coordinatorcontact = coordinatorcontact;
	}
	public Date getAppointment() {
		return appointment;
	}
	public void setAppointment(Date appointment) {
		this.appointment = appointment;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getDpmid() {
		return dpmid;
	}
	public void setDpmid(Integer dpmid) {
		this.dpmid = dpmid;
	}
	public Date getStartdatetime() {
		return startdatetime;
	}
	public void setStartdatetime(Date startdatetime) {
		this.startdatetime = startdatetime;
	}
	public Date getEnddatetime() {
		return enddatetime;
	}
	public void setEnddatetime(Date enddatetime) {
		this.enddatetime = enddatetime;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public Integer getSelfticket() {
		return selfticket;
	}
	public void setSelfticket(Integer selfticket) {
		this.selfticket = selfticket;
	}
	public Integer getSelfticketid() {
		return selfticketid;
	}
	public void setSelfticketid(Integer selfticketid) {
		this.selfticketid = selfticketid;
	}
}
