package com.irctn.serviceimpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.irctn.model.Department;
import com.irctn.repository.DepartmentRepository;
import com.irctn.service.DepartmentService;
import com.irctn.util.AppConstants;
import com.irctn.vo.DepartmentVO;
import com.irctn.vo.MessageVO;

@Service
public class DepartmentServiceImpl implements DepartmentService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.serviceimpl.DepartmentServiceImpl");
	
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	DepartmentRepository departmentRepository;

	@Override
	public String saveDepartment(DepartmentVO departmentVO) {
		
		Department department = departmentRepository.findByDepartmentid(departmentVO.getDepartmentId());
		String success = "success";
		if(null == department) {
			department = new Department();
		} else {
			redisTemplate.opsForHash().delete("DEPARTMENT_BY_ID", departmentVO.getDepartmentId());			
			success = "updatesuccess";
		}
		department.setDepartment(departmentVO.getName());
		department.setDescription(departmentVO.getDescription());
		
		Department savedDepartment = departmentRepository.save(department);
		LOGGER.debug("The Department is saved with id : " + departmentVO.getName());
		
		redisTemplate.opsForHash().put("DEPARTMENT_BY_ID", savedDepartment.getDepartmentid(), savedDepartment);
		redisTemplate.opsForHash().delete("DEPARTMENT_LIST", "DEPARTMENTS");
		
		return success;		
	}

	@Override
	public List<DepartmentVO> getAllDepartments() {
		Object object = redisTemplate.opsForHash().get("DEPARTMENT_LIST", "DEPARTMENTS");
		List<Department> departments = new ArrayList<Department>();
		if(null == object) {
			departments = departmentRepository.findAll();
			System.out.println("Getting list from DB " + departments);
		} else {
			System.out.println("Getting list from object " + departments);
			return (List<DepartmentVO>)object;
		}
		
		if(null != departments) {
			List<DepartmentVO> voList = new ArrayList<DepartmentVO>();
			for(Department department : departments) {
				DepartmentVO vo = new DepartmentVO();
				vo.setDepartmentId(department.getDepartmentid());
				vo.setName(department.getDepartment());
				vo.setDescription(department.getDescription());
				voList.add(vo);
			}
			
			List<DepartmentVO> sortedDepartments = voList.stream().sorted(Comparator.comparing(DepartmentVO::getDepartmentId).reversed())
																.collect(Collectors.toList());
			redisTemplate.opsForHash().put("DEPARTMENT_LIST", "DEPARTMENTS", sortedDepartments);
			
			return sortedDepartments;
		}
		
		return null;
	}

	@Override
	public String deleteDepartment(Integer departmentid) {
		departmentRepository.delete(departmentid);
		redisTemplate.opsForHash().delete("DEPARTMENT_BY_ID", departmentid);
		redisTemplate.opsForHash().delete("DEPARTMENT_LIST", "DEPARTMENTS");
		return AppConstants.SUCCESS;
	}

	@Override
	public List<DepartmentVO> getDepartmentNameLike(String search) {
		List<Department> departments = departmentRepository.findByDepartmentContaining(search);
		return getVOListFromModelList(departments);
	}

	@Override
	public DepartmentVO getDepartmentByName(String department) {
		return getVOFromModel(departmentRepository.findByDepartment(department));
	}

	@Override
	public DepartmentVO getDepartmentById(Integer departmentid) {
		Object object = redisTemplate.opsForHash().get("DEPARTMENT_BY_ID", departmentid);
		Department department = null;
		if(null == object) {
			department = departmentRepository.findByDepartmentid(departmentid);
		} else {
			return (DepartmentVO)object;
		}
				
		DepartmentVO vo = getVOFromModel(department);
		redisTemplate.opsForHash().put("DEPARTMENT_BY_ID", departmentid, vo);
		return vo;
	}

	@Override
	public MessageVO isDepartmentUnique(String name, Integer id) {
		if(null == name || null == id) return new MessageVO(AppConstants.FAILURE, "Server Error. Please refresh and retry.");
		//DepartmentVO departmentToBeUpdated = getDepartmentById(id);
		DepartmentVO departmentByName = getDepartmentByName(name);
		System.out.println(" Incoming Name and id " + name + " : " + id.intValue());
		
		if(null != departmentByName && id != departmentByName.getDepartmentId() ) {
			System.out.println(" departmentByName Name and id " + departmentByName.getDepartmentId() + " : " + id.intValue());
			return new MessageVO(AppConstants.FAILURE, "Department already exists with the same name. Please check.");
		} else {
			return new MessageVO(AppConstants.SUCCESS, "Please add the Department.");
		}
	}
	
	private DepartmentVO getVOFromModel(Department department) {
		if(null == department) return null;
		DepartmentVO vo = new DepartmentVO();
		vo.setDepartmentId(department.getDepartmentid());
		vo.setName(department.getDepartment());
		vo.setDescription(department.getDescription());
		return vo;
	}
	
	private List<DepartmentVO> getVOListFromModelList(List<Department> departments) {
		if(null == departments) return null;
		List<DepartmentVO> list = new ArrayList<DepartmentVO>();
		for(Department department : departments) {
			list.add(getVOFromModel(department));
		}
		return list;
	}

}
