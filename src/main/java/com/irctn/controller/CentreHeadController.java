package com.irctn.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.irctn.service.CentreDepartmentService;
import com.irctn.service.CentreDepartmentUserService;
import com.irctn.service.CentreService;
import com.irctn.service.DepartmentService;
import com.irctn.service.UserService;
import com.irctn.service.ZoneService;
import com.irctn.util.AppConstants;
import com.irctn.vo.CentreDepartmentUserVO;
import com.irctn.vo.CentreDepartmentVO;
import com.irctn.vo.DepartmentVO;
import com.irctn.vo.DpmVO;
import com.irctn.vo.MessageVO;
import com.irctn.vo.UserVO;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/home")
public class CentreHeadController {

	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.controller.CentreHeadController");

	@Autowired
	UserService userService;
	
	
	@Autowired
	ZoneService zoneService;
	
	@Autowired
	DepartmentService departmentService;
	
	@Autowired
	CentreService centreService;
	
	@Autowired
	CentreDepartmentService centreDepartmentService;
	
	@Autowired
	CentreDepartmentUserService centreDepartmentUserService;
	
	@CrossOrigin
	@RequestMapping(value = "/getAllMappedDepartments/{id}", method = RequestMethod.GET)
	public List<CentreDepartmentVO> getDepartmentsByCentre(@PathVariable Integer id) {
		return centreDepartmentService.getDepartmentsByCentre(id);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/isDepartmentMappedToCentre/{centreid}/{departmentid}", method = RequestMethod.GET)
	public MessageVO isDepartmentMappedToCentre(@PathVariable Integer departmentid , @PathVariable Integer centreid) {
		System.out.println("Check Uniqueness... The Department id is : "+ departmentid + " and centre id is " + centreid );
		boolean result = centreDepartmentService.isCentreMappedToDepartment(centreid, departmentid);
		if(result) {
			return new MessageVO(AppConstants.FAILURE, "The Department and Centre are already mapped. Please check again.");
		} else {
			return new MessageVO(AppConstants.SUCCESS, "Please go ahed to map The Department and Centre.");
		}
	}
	
	@CrossOrigin
	@RequestMapping(value = "/saveCentreDepartment", method = RequestMethod.POST)
	public MessageVO saveCentreDepartment(@RequestBody CentreDepartmentVO centreDepartmentVO) {

		CentreDepartmentVO savedMapping = centreDepartmentService.saveCentreDepartment(centreDepartmentVO);
		if(null == savedMapping || null == savedMapping.getResult()) {
			return new MessageVO(AppConstants.FAILURE, "Centre Department mapping not saved.");
		}
		else if (AppConstants.SUCCESS.equalsIgnoreCase(savedMapping.getResult()) ) {
			return new MessageVO(AppConstants.SUCCESS, "Centre Department mapping for department "+ centreDepartmentVO.getDepartmentName() +" is done.");
		} else if (AppConstants.UPDATE_SUCCESS.equalsIgnoreCase(savedMapping.getResult()) ) {
			return new MessageVO(AppConstants.UPDATE_SUCCESS
									, "Centre Department mapping for department "+ centreDepartmentVO.getDepartmentName() +" is updated.");
		} else {
			return new MessageVO(AppConstants.FAILURE, "Centre Department mapping not saved.");
		}		
	}
	
	@CrossOrigin
	@RequestMapping(value = "/getUnMappedDepartments/{centreid}", method = RequestMethod.GET)
	public List<DepartmentVO> getUnMappedDepartmentsForCentre(@PathVariable Integer centreid) {
		return centreDepartmentService.getUnmappedDepartmentsByCentre(centreid);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/getUnMappedDepartmentsWithSearch/{centreid}/{search}", method = RequestMethod.GET)
	public List<DepartmentVO> getUnMappedDepartmentsForCentreWithSearch(@PathVariable Integer centreid, @PathVariable String search) {
		return centreDepartmentService.searchUnmappedDepartmentsByCentre(centreid, search);
	}
		
	@CrossOrigin
	@RequestMapping(value = "/searchDepartmentHeads/{search}", method = RequestMethod.GET)
	public List<UserVO> searchDeparmtentHeads(@PathVariable String search) {		
		List<UserVO> list =  userService.getUserWithNameAndRoleNameLike(search, AppConstants.ROLE_DEPARTMENT_HEAD);
		if(null != list && !list.isEmpty()) {
			System.out.println("The list of users with roles as departmetn heads are :" + list.size());
		}
		return list;
	 }

	
	@CrossOrigin
	@RequestMapping(value = "/searchDpmsByName/{search}", method = RequestMethod.GET)
	public List<DpmVO> searchDPMs(@PathVariable String search) {		
		System.out.println("--->CentreHeadController.searchDPMs.................");
		
		List<DpmVO> dpmList = null;
		List<UserVO> list =  userService.getUserWithNameAndRoleNameLike(search, AppConstants.ROLE_DPM);
		if(null != list && !list.isEmpty()) {
			System.out.println("--->The list of users with roles as dpms are :" + list.size());
			dpmList = new ArrayList<DpmVO>();
			for(UserVO vo : list) {
				DpmVO dpmVO = new DpmVO();
				dpmVO.setDpmId(vo.getUserId());
				dpmVO.setDpmName(vo.getFirstName() + " " + vo.getLastName());
				dpmVO.setDpmContact(vo.getContact());
				dpmList.add(dpmVO);
			}
			return dpmList;
		} else {
			return null;
		}
		/*
		List<CentreDepartmentUserVO> mappedDPMs = centreDepartmentUserService.listByDpmIdList(list);
		
		if(null == mappedDPMs || mappedDPMs.isEmpty()) {
			return null;
		}
		else {
			dpmList = new ArrayList<DpmVO>();
			for(CentreDepartmentUserVO vo : mappedDPMs) {
				DpmVO dpmVO = new DpmVO();
				dpmVO.setDpmId(vo.getUserId());
				dpmVO.setDpmName(vo.getUserName());
				dpmVO.setDpmContact(vo.getUserContactNumber());
				dpmVO.setDpmCentre(vo.getCentreName());
				dpmVO.setDpmDepartment(vo.getDepartmentName());
				dpmVO.setDpmZone(null); //TODO: Implement this
				dpmList.add(dpmVO);
			}
			return dpmList;
		}
		*/
		
	 }
	
	@CrossOrigin
	@RequestMapping(value = "/searchNonMappedDpmsByName/{search}", method = RequestMethod.GET)
	public List<UserVO> searchUnMappedDPMs(@PathVariable String search) {		
		System.out.println("--->CentreHeadController.searchUnMappedDPMs.................");
		
		List<UserVO> list =  userService.getUserWithNameAndRoleNameLike(search, AppConstants.ROLE_DPM);
		if(null != list && !list.isEmpty()) {
			System.out.println("--->The list of users with roles as dpms are :" + list.size());
		}
		List<CentreDepartmentUserVO> mappedDPMs = centreDepartmentUserService.listByDpmIdList(list);
		//List<DpmVO> dpmList = null;
		List<UserVO> unmappedDPMList = new ArrayList<UserVO>();
		if(null == mappedDPMs || mappedDPMs.isEmpty()) {
			unmappedDPMList.addAll(list);			
		}
		else {
			boolean isUserMappedAsDPM = false;
			for(UserVO userAsDPM : list) {				
				for(CentreDepartmentUserVO vo : mappedDPMs) {				
					if(userAsDPM.getUserId().equals(vo.getUserId())) {
						isUserMappedAsDPM = true;
						System.out.println("---------------------------> DPM User mapped is : " + userAsDPM.getFirstName() + userAsDPM.getLastName());
						break;
					}
				}
				if(!isUserMappedAsDPM) {
					System.out.println("-------------------------> This DPM User not mapped : " + userAsDPM.getFirstName() + userAsDPM.getLastName());
					unmappedDPMList.add(userAsDPM);
				}
				isUserMappedAsDPM = false;
			}			
		}
		return unmappedDPMList;
	}
	

	@CrossOrigin
	@RequestMapping(value = "/searchCentreDepartmentByName/{centreId}/{search}", method = RequestMethod.GET)
	public List<CentreDepartmentVO> searchCentreDepartmentByName(@PathVariable Integer centreId, @PathVariable String search) {
		return centreDepartmentService.getCentreDepartmentsByNameLike(centreId, search); 
	}
}
