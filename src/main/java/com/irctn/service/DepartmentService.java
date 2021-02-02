package com.irctn.service;

import java.util.List;

import com.irctn.vo.DepartmentVO;
import com.irctn.vo.MessageVO;

public interface DepartmentService {
	
	public String saveDepartment(DepartmentVO departmentVO);
	
	public List<DepartmentVO> getAllDepartments();
	
	public String deleteDepartment(Integer departmentid);
	
	public List<DepartmentVO> getDepartmentNameLike(String search);

	public DepartmentVO getDepartmentByName(String search);	//Department name is unique

	public DepartmentVO getDepartmentById(Integer departmentid);

	public MessageVO isDepartmentUnique(String name, Integer id);

}
