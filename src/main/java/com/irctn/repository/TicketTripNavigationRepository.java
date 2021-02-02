package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.TicketTripNavigation;

public interface TicketTripNavigationRepository extends JpaRepository<TicketTripNavigation, Integer> {

	public TicketTripNavigation findByTickettripnavigationid(Integer tickettripnavigationid);
	
	public TicketTripNavigation findTopByTickettripidOrderByTickettripnavigationidDesc(Integer tickettripid);
	
	public List<TicketTripNavigation> findByTickettripidOrderByTickettripnavigationid(Integer tickettripid);
	
}
