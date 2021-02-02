package com.irctn.vo.mobile;

import java.io.Serializable;

public class UserShortDetailVO implements Serializable {

	private String username;
	
	private String email;
	
	private String contact;
	
	private String attendance;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAttendance() {
		return attendance;
	}

	public void setAttendance(String attendance) {
		this.attendance = attendance;
	}
	
}
