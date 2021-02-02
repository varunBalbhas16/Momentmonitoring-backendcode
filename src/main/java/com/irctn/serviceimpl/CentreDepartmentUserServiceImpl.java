package com.irctn.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.irctn.model.CentreDepartmentUser;
import com.irctn.repository.CentreDepartmentUserRepository;
import com.irctn.service.CentreDepartmentService;
import com.irctn.service.CentreDepartmentUserService;
import com.irctn.service.CentreService;
import com.irctn.service.DepartmentService;
import com.irctn.service.UserService;
import com.irctn.util.AppConstants;
import com.irctn.vo.CentreDepartmentUserVO;
import com.irctn.vo.CentreDepartmentVO;
import com.irctn.vo.UserVO;

@Service
public class CentreDepartmentUserServiceImpl implements CentreDepartmentUserService {

	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.serviceimpl.CentreDepartmentUserServiceImpl");
	
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	CentreService centreService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	DepartmentService departmentService;
	
	@Autowired
	CentreDepartmentService centreDepartmentService;
	
	@Autowired
	CentreDepartmentUserRepository centreDepartmentUserRepository;
	
	@Override
	public String saveCentreDepartmentUser(CentreDepartmentUserVO centreDepartmentUserVO) {
		System.out.println("--------->this is the new kid in the block---");
		CentreDepartmentUser centreDepartmentUser = centreDepartmentUserRepository.findByCentredepartmentuserid(centreDepartmentUserVO.getCenterDepartmentUserId());
		String success = AppConstants.SUCCESS ;
		if(null == centreDepartmentUser) {
			centreDepartmentUser = new CentreDepartmentUser();
		} else {
			redisTemplate.opsForHash().delete("CENTRE_DEPT_USER_BY_ID", centreDepartmentUserVO.getCenterDepartmentUserId());
			success = AppConstants.UPDATE_SUCCESS;
		}
		
		System.out.println("Saving mapping with centre ID :"+ centreDepartmentUserVO.getCentreDepartmentId() 
							+ " with user ID :" + centreDepartmentUserVO.getUserId()
							+ " with role ID :" + centreDepartmentUserVO.getRoleId() );
		
		centreDepartmentUser.setCentredepartmentid(centreDepartmentUserVO.getCentreDepartmentId());
		centreDepartmentUser.setRoleid(centreDepartmentUserVO.getRoleId());
		centreDepartmentUser.setUserid(centreDepartmentUserVO.getUserId());
		
		CentreDepartmentUser savedCentreDept = centreDepartmentUserRepository.save(centreDepartmentUser);
		LOGGER.debug("The CentreDepartmentUser mapping is saved with id : " + savedCentreDept.getCentredepartmentuserid());
		redisTemplate.opsForHash().delete("CENTRE_DEPT_USER_LIST", "CENTRE_DEPTS_USERS");
		
		return success;
	}

	@Override
	public CentreDepartmentUserVO getCentreDepartmentUserById(Integer centreDepartmentUserId) {
		return getVOFromModel(centreDepartmentUserRepository.findByCentredepartmentuserid(centreDepartmentUserId));
	}

	@Override
	public CentreDepartmentUserVO getCentreDepartmentUserByUserId(Integer userId) {
		return this.getVOFromModel(centreDepartmentUserRepository.findByUserid(userId));
	}

	@Override
	public List<CentreDepartmentUserVO> getCentreDepartmentUserByRoleId(Integer roleId) {
		return this.getVOListFromModelList(centreDepartmentUserRepository.findByRoleid(roleId));
	}

	@Override
	public CentreDepartmentUserVO getCentreDepartmentUserByCentreId(Integer centreId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private List<CentreDepartmentUserVO> getVOListFromModelList(List<CentreDepartmentUser> centreDeptUsers) {
		if(null == centreDeptUsers || centreDeptUsers.isEmpty()) return null;
		List<CentreDepartmentUserVO> list = new ArrayList<CentreDepartmentUserVO>();
		for(CentreDepartmentUser mappedDept : centreDeptUsers) {
			list.add(getVOFromModel(mappedDept));
		}
		return list;
	}

	private CentreDepartmentUserVO getVOFromModel(CentreDepartmentUser centreDepartmentUser) {
		if(null == centreDepartmentUser) return null;
		CentreDepartmentUserVO vo = new CentreDepartmentUserVO();
		vo.setCenterDepartmentUserId(centreDepartmentUser.getCentredepartmentuserid());
		vo.setCentreDepartmentId(centreDepartmentUser.getCentredepartmentid());
		vo.setRoleId(centreDepartmentUser.getRoleid());
		vo.setUserId(centreDepartmentUser.getUserid());
		
		CentreDepartmentVO centreDepartmentVO = centreDepartmentService.getCentreDepartmentById(centreDepartmentUser.getCentredepartmentid());
		if(null != centreDepartmentVO) {
			vo.setCentreName(centreService.getCentreById(centreDepartmentVO.getCentreId()).getCentreName());
			vo.setDepartmentName(departmentService.getDepartmentById(centreDepartmentVO.getDepartmentId()).getName());
		}
		if(null != vo.getUserId()) {
			UserVO userVO = userService.getUserById(vo.getUserId());
			vo.setUserName(userVO.getFirstName() + " " + userVO.getLastName());
			vo.setUserContactNumber(userVO.getContact());
		}
		
		return vo;
	}

	@Override
	public List<CentreDepartmentUserVO> listByDpmIdList(List<UserVO> dpmUsers) {
		if(null == dpmUsers || dpmUsers.isEmpty()) {
			return null;
		}
		List<Integer> dpmIds = new ArrayList<Integer>(); 
		for(UserVO user: dpmUsers) {
			dpmIds.add(user.getUserId());
		}
		
		return getVOListFromModelList(centreDepartmentUserRepository.findByUseridIn(dpmIds) );
	}

}
