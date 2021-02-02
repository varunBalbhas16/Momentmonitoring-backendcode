package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.irctn.model.TicketTrip;

public interface TicketTripRepository extends JpaRepository<TicketTrip, Integer> {

	public TicketTrip findByTickettripid(Integer tickettripid);
	
	public TicketTrip findByTrip(Integer trip);
	
	public List<TicketTrip> findByTicket(Integer ticket);
	
	public List<TicketTrip> findByDpmid(Integer dpmid);
	
	public TicketTrip findTopByOrderByTripDesc();
	
	public TicketTrip findTopByTicketOrderByTickettripidDesc(Integer ticket);
	
	public TicketTrip findByTicketAndDpmid(Integer ticket, Integer dpmid);

	@Query("select count(a.distance) from TicketTrip a")
	public Double sumTripDistance();
}
