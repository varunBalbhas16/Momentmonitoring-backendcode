package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.CentreDepartment;

public interface CentreDepartmentRepository extends JpaRepository<CentreDepartment, Integer> {

	public List<CentreDepartment> findByCentreid(Integer centreid);

	public CentreDepartment findByCentredepartmentid(Integer centredepartmentid);
	
	public List<CentreDepartment> findByDepartmentid(Integer departmentid);
	
	public List<CentreDepartment> findByDepartmentheadid(Integer departmentheadid);
	
	public CentreDepartment findByCentreidAndDepartmentid(Integer centreid, Integer departmentid);
	
}
