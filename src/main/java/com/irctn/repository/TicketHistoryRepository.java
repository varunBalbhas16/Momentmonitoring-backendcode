package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.TicketHistory;

public interface TicketHistoryRepository extends JpaRepository<TicketHistory, Integer> {

	public TicketHistory findByTickethistoryid(Integer tickethistoryid);
	
	public List<TicketHistory> findByTicket(Integer ticket);
	
}
