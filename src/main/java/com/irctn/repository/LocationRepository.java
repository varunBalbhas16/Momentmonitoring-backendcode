package com.irctn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {
	
	

}
