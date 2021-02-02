package com.irctn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.MapDigitalScale;

public interface MapDigitalScaleRepository extends JpaRepository<MapDigitalScale, Integer> {

	public MapDigitalScale findByMapdigitalscaleid(Integer mapDigitalScaleId);
	
	public MapDigitalScale findByDigitalscale(Integer digitalscale);
	
	public MapDigitalScale findBySortinguserid(Integer sortinguserid);

}
