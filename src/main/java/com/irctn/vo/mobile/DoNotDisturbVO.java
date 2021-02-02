package com.irctn.vo.mobile;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DoNotDisturbVO implements Serializable {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private Date startDateTime;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private Date endDateTime;
	
	private Integer userId;
	
	private String emailId;
	
	private Integer doNotDisturbId;
	
	private String status;

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Integer getDoNotDisturbId() {
		return doNotDisturbId;
	}

	public void setDoNotDisturbId(Integer doNotDisturbId) {
		this.doNotDisturbId = doNotDisturbId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
