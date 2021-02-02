package com.irctn.vo.mobile.request;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SearchDatesVO implements Serializable {

	//2019-12-21T18:30:00.000Z
	@JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT")
	private Date startdate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT")
	private Date enddate;

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		this.enddate.setHours(23);
		this.enddate.setMinutes(59);
		this.enddate.setSeconds(59);
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	
}
