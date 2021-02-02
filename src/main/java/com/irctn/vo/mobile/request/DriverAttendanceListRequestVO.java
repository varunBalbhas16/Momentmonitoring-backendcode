package com.irctn.vo.mobile.request;

import java.io.Serializable;
import java.util.Date;

public class DriverAttendanceListRequestVO implements Serializable {

	private Integer userId;
	
	private Date dateSelected;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getDateSelected() {
		return dateSelected;
	}

	public void setDateSelected(Date dateSelected) {
		this.dateSelected = dateSelected;
	}
	
}
