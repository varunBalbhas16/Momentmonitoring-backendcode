package com.irctn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.District;

public interface DistrictRepository extends JpaRepository<District, Integer> {

	public District findByDistrictid(Integer districtid);
	
	public District findByDistrictIgnoreCase(String district);
	
}
