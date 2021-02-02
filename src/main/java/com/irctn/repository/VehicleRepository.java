package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.irctn.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

	public Vehicle findByVehicleid(Integer vehicleid);
	
	public Vehicle findByRegistrationIgnoreCase(String registration);
	
	public List<Vehicle> findByDistrictid(Integer districtid);
	
	public List<Vehicle> findByOrderByRegistrationAsc();

	@Query("select distinct a.districtid from Vehicle a")
	public List<Integer> findDistinctDistricts();
	
}
