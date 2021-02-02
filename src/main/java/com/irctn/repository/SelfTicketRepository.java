package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.SelfTicket;

public interface SelfTicketRepository extends JpaRepository<SelfTicket, Integer> {

	public SelfTicket findBySelfticketid(Integer selfticketid);
	
	public List<SelfTicket> findByCreatedby(Integer createdby);
	
	public List<SelfTicket> findByCentreContaining(String centre);
	
	public List<SelfTicket> findByCreatedbyAndCentreContaining(Integer createdby, String centre);
	
}
