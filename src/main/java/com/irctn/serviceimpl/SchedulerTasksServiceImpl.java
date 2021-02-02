package com.irctn.serviceimpl;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irctn.model.EscalationAudit;
import com.irctn.model.ReportAudit;
import com.irctn.repository.EscalationAuditRepository;
import com.irctn.repository.ReportAuditRepository;
import com.irctn.service.SchedulerTasksService;

@Service
public class SchedulerTasksServiceImpl implements SchedulerTasksService {

	@Autowired
	ReportAuditRepository reportAuditRepository;
	
	@Autowired
	EscalationAuditRepository escalationAuditRepository;
	
	@Override
	public boolean sendMonthlyReport(String month, int year, String summary, Integer dpmId) {
		ReportAudit reportAudit = new ReportAudit();
		reportAudit.setMonth(month);
		reportAudit.setYear(year);
		reportAudit.setDpmid(dpmId);
		reportAudit.setSummary(summary);
		reportAudit.setCreatedon(new Date());
		ReportAudit savedAudit = reportAuditRepository.save(reportAudit);
		return (null == savedAudit) ?  false :  true;
	}

	@Override
	public boolean isMonthlyReportSent(String month, int year, Integer dpmId) {
		if(null == reportAuditRepository.findByDpmidAndMonthIgnoreCaseAndYear(dpmId, month, year)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean isEscalationSentForTicket(Integer ticket) {
		if(null == escalationAuditRepository.findByTicket(ticket)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean sendEscationForTicket(Integer ticket, Integer dpmId, String emailId) {
		EscalationAudit audit = new EscalationAudit();
		audit.setTicket(ticket);
		audit.setDpmid(dpmId);
		audit.setCreatedat(new Date());
		EscalationAudit saved = escalationAuditRepository.save(audit);
		return (null==saved) ? false:true;
	}

}
