package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.DoNotDisturb;

public interface DoNotDisturbRepository extends JpaRepository<DoNotDisturb, Integer> {

	public DoNotDisturb findByDndid(Integer dndid);
	
	public List<DoNotDisturb> findByUserid(Integer userid);
	
	public List<DoNotDisturb> findByUseridAndStatus(Integer userid, Integer status);
	
}
