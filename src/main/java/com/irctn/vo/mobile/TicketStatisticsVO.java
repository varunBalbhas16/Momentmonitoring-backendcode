package com.irctn.vo.mobile;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TicketStatisticsVO implements Serializable {

	private Integer ticket;
	
	private String centre;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
	private Date ticketDate;	
	
	private String startLocation;
	
	private String endLocation;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
	private Date tripStart;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
	private Date tripEnd;
	
	private String type;
	
	private String dpm;
	
	private Float distance;
	
	private String cost;
	
}
