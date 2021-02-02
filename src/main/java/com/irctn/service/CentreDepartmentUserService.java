package com.irctn.service;

import java.util.List;

import com.irctn.vo.CentreDepartmentUserVO;
import com.irctn.vo.UserVO;

public interface CentreDepartmentUserService {

	public String saveCentreDepartmentUser(CentreDepartmentUserVO centreDepartmentVO);
	
	public CentreDepartmentUserVO getCentreDepartmentUserById(Integer centreDepartmentUserId);
	
	public CentreDepartmentUserVO getCentreDepartmentUserByUserId(Integer userId);
	
	public List<CentreDepartmentUserVO> getCentreDepartmentUserByRoleId(Integer roleId);
	
	public CentreDepartmentUserVO getCentreDepartmentUserByCentreId(Integer centreId);
	
	public List<CentreDepartmentUserVO> listByDpmIdList(List<UserVO> dpmUsers);
	
}
