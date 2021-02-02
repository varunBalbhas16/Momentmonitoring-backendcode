package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.VehicleDriver;

public interface VehicleDriverRepository extends JpaRepository<VehicleDriver, Integer> {

	public VehicleDriver findByDriverid(Integer driverid);
	
	public VehicleDriver findByDriverempidIgnoreCase(String driverempid);
	
	public List<VehicleDriver> findByDistrictid(Integer districtid);

	public List<VehicleDriver> findByOrderByDrivernameAsc();
	
	public List<VehicleDriver> findByDistrictidOrderByDrivernameAsc(Integer districtid);
}
