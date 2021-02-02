package com.irctn.service;

public interface SchedulerTasksService {

	public boolean sendMonthlyReport(String month, int year, String summary, Integer dpmId);
	
	public boolean isMonthlyReportSent(String month, int year, Integer dpmId);
	
	public boolean isEscalationSentForTicket(Integer ticket);
	
	public boolean sendEscationForTicket(Integer ticket, Integer dpmId, String emailId);
	
}
