package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.Zone;

public interface ZoneRepository extends JpaRepository<Zone, Integer> {

	public Zone findByZone(String zone);

	public Zone findByZoneid(Integer zoneid);
	
	public List<Zone> findByZoneContaining(String zone);	
	
	public Zone findByCode(String code);
	
}
