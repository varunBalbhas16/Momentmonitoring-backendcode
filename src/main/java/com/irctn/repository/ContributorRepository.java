package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.Contributor;

public interface ContributorRepository extends JpaRepository<Contributor, Integer> {

	// public Contributor findByEmail(Integer contributorid);

	public Contributor findByContributorid(Integer contributorid);

	public long countByStatusAndType(Integer status, String type);

	public List<Contributor> findDistinctByNameLike(String name);

	public List<Contributor> findByNameLike(String name);

	public List<Contributor> findByTypeAndNameLike(String type,  String name);
	
	public List<Contributor> findByType(String type);

	public Contributor findByName(String name);	
	
}
