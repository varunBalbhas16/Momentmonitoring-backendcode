package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

	public Department findByDepartment(String department);

	public Department findByDepartmentid(Integer departmentid);
	
	public List<Department> findByDepartmentLike(String department);
	
	public List<Department> findByDepartmentContaining(String department);
	
}
