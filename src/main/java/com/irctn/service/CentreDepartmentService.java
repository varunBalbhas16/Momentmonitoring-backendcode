package com.irctn.service;

import java.util.List;

import com.irctn.vo.CentreDepartmentVO;
import com.irctn.vo.DepartmentVO;

public interface CentreDepartmentService {
	
	public CentreDepartmentVO saveCentreDepartment(CentreDepartmentVO centreDepartmentVO);
	
	public List<CentreDepartmentVO> getDepartmentsByCentre(Integer centreId);
	
	public CentreDepartmentVO getCentreDepartmentById(Integer centreDepartmentId);
	
	public boolean isCentreMappedToDepartment(Integer centerId, Integer departmentId);

	List<DepartmentVO> getUnmappedDepartmentsByCentre(Integer centreId);

	List<DepartmentVO> searchUnmappedDepartmentsByCentre(Integer centreId, String search);
	
	public List<CentreDepartmentVO> getCentreDepartmentsByNameLike(Integer centreId, String search);
	
}
