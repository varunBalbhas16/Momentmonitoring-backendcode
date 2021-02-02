package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.ReportAudit;

public interface ReportAuditRepository extends JpaRepository<ReportAudit, Integer> {
	
	public ReportAudit findByReportid(Integer reportid);
	
	public List<ReportAudit> findByDpmid(Integer dpmid);
	
	public ReportAudit findByDpmidAndMonthIgnoreCaseAndYear(Integer dpmid, String month, Integer year);
	
	public List<ReportAudit> findByMonthIgnoreCaseAndYear(String month, Integer year);

}