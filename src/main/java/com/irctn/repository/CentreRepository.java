package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.Centre;

public interface CentreRepository extends JpaRepository<Centre, Integer> {

	public Centre findByCentrename(String centrename);

	public Centre findByCentreid(Integer centreid);
	
	public List<Centre> findByCentrenameContaining(String centrename);	
	
	public Centre findByCentrecode(String centrecode);	
	
	public Centre findByHeadid(Integer headid);
	
}
