package com.irctn.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.irctn.model.CentreDepartment;
import com.irctn.repository.CentreDepartmentRepository;
import com.irctn.service.CentreDepartmentService;
import com.irctn.service.CentreService;
import com.irctn.service.DepartmentService;
import com.irctn.service.UserService;
import com.irctn.util.AppConstants;
import com.irctn.vo.CentreDepartmentVO;
import com.irctn.vo.CentreVO;
import com.irctn.vo.DepartmentVO;
import com.irctn.vo.UserVO;

@Service
public class CentreDepartmentServiceImpl implements CentreDepartmentService {

	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.serviceimpl.CentreDepartmentServiceImpl");

	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	CentreService centreService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	DepartmentService departmentService;
	
	@Autowired
	CentreDepartmentRepository centreDepartmentRepository;
	
	@Override
	public CentreDepartmentVO saveCentreDepartment(CentreDepartmentVO centreDepartmentVO) {
		System.out.println("Checking this .............");
		CentreDepartment centreDepartment = centreDepartmentRepository.findByCentredepartmentid(centreDepartmentVO.getCenterDepartmentId());
		String success = AppConstants.SUCCESS ;
		if(null == centreDepartment) {
			centreDepartment = new CentreDepartment();
		} else {
			redisTemplate.opsForHash().delete("CENTRE_DEPT_BY_ID", centreDepartmentVO.getCenterDepartmentId());
			success = AppConstants.UPDATE_SUCCESS;
		}
		
		System.out.println("------------>Saving saveCentreDepartment mapping with centre ID :"+ centreDepartmentVO.getCentreId() 
							+ " with department ID :" + centreDepartmentVO.getDepartmentId()
							+ " with department head ID :" + centreDepartmentVO.getDepartmentHeadId() );
		
		centreDepartment.setCentreid(centreDepartmentVO.getCentreId());
		centreDepartment.setDepartmentid(centreDepartmentVO.getDepartmentId());
		centreDepartment.setDepartmentheadid(centreDepartmentVO.getDepartmentHeadId());
		
		CentreDepartment savedCentreDept = centreDepartmentRepository.save(centreDepartment);
		LOGGER.debug("The CentreDepartment mapping is saved with id : " + savedCentreDept.getCentredepartmentid());
		redisTemplate.opsForHash().delete("CENTRE_DEPT_LIST", "CENTRE_DEPTS");
		redisTemplate.opsForHash().delete("DEPTS_BY_CENTRE", savedCentreDept.getCentreid());
		
		CentreVO centreVO = centreService.getCentreById(savedCentreDept.getCentreid());
		DepartmentVO departmentVO = departmentService.getDepartmentById(savedCentreDept.getDepartmentid());
		centreDepartmentVO.setCenterName(centreVO.getCentreName());
		centreDepartmentVO.setDepartmentName(departmentVO.getName());
		centreDepartmentVO.setResult(success);		
		return centreDepartmentVO;
				
	}

	@Override
	public List<CentreDepartmentVO> getDepartmentsByCentre(Integer centreId) {
		
		List<CentreDepartment> mappedDeptsInACentre = null;
		Object object = redisTemplate.opsForHash().get("DEPTS_BY_CENTRE", centreId);
		if(null == object) {
			mappedDeptsInACentre = centreDepartmentRepository.findByCentreid(centreId);
			List<CentreDepartmentVO> list = getVOListFromModelList(mappedDeptsInACentre);
			redisTemplate.opsForHash().put("DEPTS_BY_CENTRE", centreId, list);
			return list;
		} else {
			return (List<CentreDepartmentVO>)object;
		}
	}
	
	@Override
	public List<DepartmentVO> getUnmappedDepartmentsByCentre(Integer centreId) {
		List<CentreDepartmentVO> mapped = this.getDepartmentsByCentre(centreId);
		List<DepartmentVO> allDepts = departmentService.getAllDepartments();
		List<DepartmentVO> unmapped = new ArrayList<DepartmentVO>();
		if(null == mapped || mapped.isEmpty()) return allDepts;
		else {
			for(DepartmentVO deptVO : allDepts) {
				boolean isMapped = false;
				for(CentreDepartmentVO mappedVO: mapped) {
					if(deptVO.getDepartmentId().equals(mappedVO.getDepartmentId())) {						
						isMapped = true;
						break;
					}
				}
				if(!isMapped) {
					unmapped.add(deptVO);
				}
			}
			return unmapped;
		}
	}	
	
	@Override
	public List<DepartmentVO> searchUnmappedDepartmentsByCentre(Integer centreId, String search) {
		if(null == search) {
			return null;
		} 
		List<DepartmentVO> allDepts = departmentService.getDepartmentNameLike(search);		
		List<CentreDepartmentVO> mapped = this.getDepartmentsByCentre(centreId);
		List<DepartmentVO> unmapped = new ArrayList<DepartmentVO>();
		if(null == mapped || mapped.isEmpty()) return allDepts;
		else {
			for(DepartmentVO deptVO : allDepts) {
				boolean isMapped = false;
				for(CentreDepartmentVO mappedVO: mapped) {
					if(deptVO.getDepartmentId().equals(mappedVO.getDepartmentId())) {						
						isMapped = true;
						break;
					}
				}
				if(!isMapped) {
					unmapped.add(deptVO);
				}
			}
			return unmapped;
		}
	}	

	private List<CentreDepartmentVO> getVOListFromModelList(List<CentreDepartment> mappedDeptsInACentre) {
		if(null == mappedDeptsInACentre || mappedDeptsInACentre.isEmpty()) return null;
		List<CentreDepartmentVO> list = new ArrayList<CentreDepartmentVO>();
		for(CentreDepartment mappedDept : mappedDeptsInACentre) {
			list.add(getVOFromModel(mappedDept));
		}
		return list;
	}

	private CentreDepartmentVO getVOFromModel(CentreDepartment mappedDept) {
		if(null == mappedDept) return null;
		CentreDepartmentVO vo = new CentreDepartmentVO();
		vo.setCenterDepartmentId(mappedDept.getCentredepartmentid());
		vo.setCentreId(mappedDept.getCentreid());
		vo.setDepartmentId(mappedDept.getDepartmentid());
		vo.setDepartmentHeadId(mappedDept.getDepartmentheadid());
		
		vo.setCenterName(centreService.getCentreById(vo.getCentreId()).getCentreName());
		vo.setDepartmentName(departmentService.getDepartmentById(vo.getDepartmentId()).getName());
		if(null != vo.getDepartmentHeadId()) {
			UserVO userVO = userService.getUserById(vo.getDepartmentHeadId());
			vo.setDepartmentHeadName(userVO.getFirstName() + " " + userVO.getLastName());
			vo.setDepartmentHeadContact(userVO.getContact());
		}
		return vo;
	}

	@Override
	public CentreDepartmentVO getCentreDepartmentById(Integer centreDepartmentId) {
		
		Object object = redisTemplate.opsForHash().get("CENTRE_DEPT_BY_ID", centreDepartmentId);
		if(null == object) {
			CentreDepartment centreDeptMap = centreDepartmentRepository.findByCentredepartmentid(centreDepartmentId);
			CentreDepartmentVO vo = getVOFromModel(centreDeptMap);
			redisTemplate.opsForHash().put("CENTRE_DEPT_BY_ID", centreDepartmentId, vo);
			return vo;
		} else {
			return (CentreDepartmentVO)object;
		}
	}

	@Override
	public boolean isCentreMappedToDepartment(Integer centerId, Integer departmentId) {
		if(null == centerId || null == departmentId) 
			return true;
		
		if(null == centreDepartmentRepository.findByCentreidAndDepartmentid(centerId, departmentId)) 
			return false;
		else 
			return true;
		
	}

	@Override
	public List<CentreDepartmentVO> getCentreDepartmentsByNameLike(Integer centreId, String search) {
		List<CentreDepartmentVO> mappedDepartments = this.getDepartmentsByCentre(centreId);
		if(null == mappedDepartments || mappedDepartments.isEmpty()) {
			return null;
		} else {
			List<DepartmentVO> departments = departmentService.getDepartmentNameLike(search);
			if(null == departments || departments.isEmpty()) {
				return null;
			} else {
				List<CentreDepartmentVO> searchCentreDepartments = new ArrayList<CentreDepartmentVO>();
				boolean hasMatchingDepartments = false;
				for(CentreDepartmentVO centreDepartment: mappedDepartments) {
					for(DepartmentVO departmentVO : departments) {
						if(centreDepartment.getDepartmentId().equals(departmentVO.getDepartmentId()) ) {
							searchCentreDepartments.add(centreDepartment);
							hasMatchingDepartments = true;
							break;
						}
					}
				}
				if(hasMatchingDepartments) return searchCentreDepartments;
			}
		}
		return null;
	}
	

}
