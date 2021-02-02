package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.Supervisor;

public interface SupervisorRepository extends JpaRepository<Supervisor, Integer>{

	public Supervisor findByClothessortingid(Integer clothesSortingId);

	public Supervisor findByCollectionid(Integer collectionid);

	public Supervisor findBySupervisorid(Integer supervisorid);

	public List<Supervisor> countByStatus(Integer status);

	public List<Supervisor> findByStatus(Integer status);

	//public Supervisor findByClothessortingid(Integer clothessortingid);

}
