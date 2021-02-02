package com.irctn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.EscalationAudit;

public interface EscalationAuditRepository extends JpaRepository<EscalationAudit, Integer> {

	public EscalationAudit findById(Integer id);
	
	public EscalationAudit findByTicket(Integer ticket);
	
}
