package com.irctn.controller;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.irctn.model.Program;
import com.irctn.service.AdminService;
import com.irctn.service.CentreService;
import com.irctn.service.ContributorService;
import com.irctn.service.DepartmentService;
import com.irctn.service.MapDigitalScaleService;
import com.irctn.service.ProgramService;
import com.irctn.service.SchoolProgramMappingService;
import com.irctn.service.UserService;
import com.irctn.service.ZoneService;
import com.irctn.util.AppConstants;
import com.irctn.vo.AdminDashboardVO;
import com.irctn.vo.CentreVO;
import com.irctn.vo.ContributorVO;
import com.irctn.vo.DepartmentVO;
import com.irctn.vo.MessageVO;
import com.irctn.vo.ProgramVO;
import com.irctn.vo.SchoolProgramMappingVO;
import com.irctn.vo.UserVO;
import com.irctn.vo.ZoneVO;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/home")
public class AdminController {

	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.controller.AdminController");

	@Autowired
	ContributorService contributorService;

	@Autowired
	UserService userService;
	
	@Autowired
	ZoneService zoneService;
	
	@Autowired
	DepartmentService departmentService;
	
	@Autowired
	CentreService centreService;

	@Autowired
	ProgramService programService;

	@Autowired
	AdminService adminService;

	@Autowired
	SchoolProgramMappingService schoolProgramMappingService;
	
	@Autowired
	MapDigitalScaleService mapDigitalScaleService;
	
	@CrossOrigin
	@RequestMapping(value = "/saveZone", method = RequestMethod.POST)
	public MessageVO saveZone(@RequestBody ZoneVO zoneVO) {

		String entity = zoneService.saveZone(zoneVO);
		if ("success".equals(entity)) {
			return new MessageVO("success", "Zone "+ zoneVO.getZone() +" is saved.", zoneVO.getZone());
		} else if ("updatesuccess".equals(entity)) {
			return new MessageVO("update success", "Zone "+ zoneVO.getZone() +" is updated.", zoneVO.getZone());
		} else {
			return new MessageVO("failure", "Zone not saved.",
					"Issues in saving Zone, contact Admin");
		}		
	}
	
	@CrossOrigin
	@RequestMapping(value = "/saveCentre", method = RequestMethod.POST)
	public MessageVO saveCentre(@RequestBody CentreVO centreVO) {

		String entity = centreService.saveCentre(centreVO);
		if ("success".equals(entity)) {
			return new MessageVO("success", "Centre "+ centreVO.getCentreName() +" is saved.", centreVO.getCentreName());
		} else if ("updatesuccess".equals(entity)) {
			return new MessageVO("update success", "Centre "+ centreVO.getCentreName() +" is updated.", centreVO.getCentreName());
		} else {
			return new MessageVO("failure", "Centre not saved.",
					"Issues in saving Centre, contact Admin");
		}		
	}
	
	@CrossOrigin
	@RequestMapping(value = "/saveDepartment", method = RequestMethod.POST)
	public MessageVO saveDepartment(@RequestBody DepartmentVO departmentVO) {

		String entity = departmentService.saveDepartment(departmentVO);
		if ("success".equals(entity)) {
			return new MessageVO("success", "Department "+ departmentVO.getName() +" is saved.", departmentVO.getName());
		} else if ("updatesuccess".equals(entity)) {
			return new MessageVO("update success", "Department "+ departmentVO.getName() +" is updated.", departmentVO.getName());
		} else {
			return new MessageVO("failure", "Department not saved.",
					"Issues in saving Department, contact Admin");
		}		
	}
	
	@CrossOrigin
	@RequestMapping(value = "/getAllZones", method = RequestMethod.GET)
	public List<ZoneVO> getAllZones() {
		return zoneService.getAllZones();
	}
	
	@CrossOrigin
	@RequestMapping(value = "/getAllDepartments", method = RequestMethod.GET)
	public List<DepartmentVO> getAllDepartments() {
		return departmentService.getAllDepartments();
	}
	
	@CrossOrigin
	@RequestMapping(value = "/isZoneUnique/{name}/{code}/{id}", method = RequestMethod.GET)
	public MessageVO isZoneUnique(@PathVariable String name, @PathVariable String code , @PathVariable Integer id) {
		System.out.println("The Zone name is : "+ name + " and code is " + code + " and the id is : " + id);
		return zoneService.isZoneUnique(name, code, id);
	}
	

	@CrossOrigin
	@RequestMapping(value = "/getAllCentres", method = RequestMethod.GET)
	public List<CentreVO> getAllCentres() {
		return centreService.getAllCentres();
	}
	
	@CrossOrigin
	@RequestMapping(value = "/searchCentresByName/{search}", method = RequestMethod.GET)
	public List<CentreVO> searchCentresByName(@PathVariable String search) {
		return centreService.getCentreNameLike(search);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/isCentreUnique/{name}/{code}/{id}", method = RequestMethod.GET)
	public MessageVO isCentreUnique(@PathVariable String name, @PathVariable String code , @PathVariable Integer id) {
		System.out.println("The Centre name is : "+ name + " and code is " + code + " and the id is : " + id);
		LOGGER.debug("The Centre name is : "+ name + " and code is " + code + " and the id is : " + id);
		return centreService.isCentreUnique(name, code, id);
	}

	@CrossOrigin
	@RequestMapping(value = "/isCentreParamsUnique/{name}/{code}", method = RequestMethod.GET)
	public MessageVO isCentreUnique(@PathVariable String name, @PathVariable String code) {
		System.out.println("The Centre name is : "+ name + " and code is " + code);
		LOGGER.debug("The Centre name is : "+ name + " and code is " + code );
		return centreService.isCentreNameCodeUnique(name, code);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/isDepartmentUnique/{name}/{id}", method = RequestMethod.GET)
	public MessageVO isDepartmentUnique(@PathVariable String name, @PathVariable Integer id) {
		System.out.println("The name is : "+ name + " and the id is : " + id);
		return departmentService.isDepartmentUnique(name, id);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/getZoneByName/{search}", method = RequestMethod.GET)
	public ZoneVO getZoneByName(@PathVariable String search) {
		return zoneService.getZonesByNames(search);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/getZoneByNameLike/{search}", method = RequestMethod.GET)
	public List<ZoneVO> getZoneByNameLike(@PathVariable String search) {
		return zoneService.getZoneNameLike(search);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/getDepartmentByName/{search}", method = RequestMethod.GET)
	public DepartmentVO getDepartmentByName(@PathVariable String search) {
		return departmentService.getDepartmentByName(search);
	}
	
	

	@CrossOrigin
	@RequestMapping(value = "/getZoneById/{id}", method = RequestMethod.GET)
	public ZoneVO getZoneById(@PathVariable Integer id) {
		return zoneService.getZoneById(id);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/getDepartmentById/{id}", method = RequestMethod.GET)
	public DepartmentVO getDepartmentById(@PathVariable Integer id) {
		return departmentService.getDepartmentById(id);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/deleteZone/{zoneid}", method = RequestMethod.DELETE)
	public MessageVO deleteZone(@PathVariable Integer zoneid) {
		if ("success".equals(zoneService.deleteZone(zoneid))) {
			System.out.println("zone deleted successfully " + zoneid);
			return new MessageVO("Zone deleted Successfully", "Zone Deleted");
		} else {
			System.out.println("Zone does not exist");
			return new MessageVO("Delete Operation Failed", "Zone does Not Exist");
		}
	}
	
	@CrossOrigin
	@RequestMapping(value = "/deleteDepartment/{departmentid}", method = RequestMethod.DELETE)
	public MessageVO deleteDepartment(@PathVariable Integer departmentid) {
		if ("success".equals(departmentService.deleteDepartment(departmentid))) {
			System.out.println("Department deleted successfully " + departmentid);
			return new MessageVO("Department deleted Successfully", "Zone Deleted");
		} else {
			System.out.println("Department does not exist");
			return new MessageVO("Delete Operation Failed", "Department does Not Exist");
		}
	}
	
	@CrossOrigin
	@RequestMapping(value = "/deleteCentre/{centreid}", method = RequestMethod.DELETE)
	public MessageVO deleteCentre(@PathVariable Integer centreid) {
		if ("success".equals(centreService.deleteCentre(centreid))) {
			System.out.println("Centre deleted successfully " + centreid);
			return new MessageVO("Centre deleted Successfully", "Centre Deleted");
		} else {
			System.out.println("Centre does not exist");
			return new MessageVO("Delete Operation Failed", "Centre does Not Exist");
		}
	}
	
	@CrossOrigin
	@RequestMapping(value = "/saveEntity", method = RequestMethod.POST)
	public MessageVO saveEntity(@RequestBody ContributorVO contributorVO) {

		String entity = contributorService.saveSchoolAndRetailerEntity(contributorVO);
		if ("success".equals(entity)) {
			return new MessageVO("success", "SchoolAndRetailerEntity is saved.", contributorVO.getType(),
					contributorVO.getName());
		} else if ("updatesuccess".equals(entity)) {
			return new MessageVO("update success", "SchoolAndRetailerEntity is updated.", contributorVO.getType(),
					contributorVO.getName());
		} else {
			return new MessageVO("failure", "SchoolAndRetailerEntity not saved.",
					"Issues in saving SchoolAndRetailerEntity, contact Admin");
		}
	}

	@CrossOrigin
	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
	public MessageVO saveUser(@RequestBody UserVO userVO) {
		MessageVO messageVO = userService.validateUser(userVO);
		if(null != messageVO && null != messageVO.getResult() && AppConstants.SUCCESS.equalsIgnoreCase(messageVO.getResult())) {
			String user = userService.saveUser(userVO, null);
			if ("success".equals(user)) {
				System.out.println("User saved Successfully");
				return new MessageVO("User is saved.", "success",  userVO.getFirstName());
			} else if ("updatesuccess".equals(user)) {
				System.out.println("updated successfully");
				return new MessageVO("User is updated.", "update success", userVO.getFirstName());
			} else {
				System.out.println("failed to save User ");
				return new MessageVO("User not saved.", "failure", "Issues in saving User");
			}
		} else if(null != messageVO) {
			return messageVO;
		} else {
			System.out.println("failed to validate and save the User ");
			return new MessageVO("User not saved.", "failure", "Issues in saving User");
		}
		
		
	}

	@CrossOrigin
	@RequestMapping(value = "/savePrograms", method = RequestMethod.POST)
	public MessageVO saveProgram(@RequestBody ProgramVO programVO) {
		String program = programService.savePrograms(programVO);
		System.out.println("Program Saved Successfully" +"Start Date is" +programVO.getStartDate());
		LOGGER.debug(programVO.getStartDate() +"End Date" +programVO.getEndDate());
		if ("success".equals(program)) {
			return new MessageVO("success", "Programs are saved.", programVO.getProgramName());
		} else if ("updatesuccess".equals(program)) {
			LOGGER.debug(programVO.getStartDate() +"End Date" +programVO.getEndDate());
			return new MessageVO("update success", "Programs are updated.", programVO.getProgramName());
		} else {
			return new MessageVO("failure", "Programs not saved.", "Issues in saving Programs, contact Admin");
		}
	}

	
	@CrossOrigin
	@RequestMapping(value = "/isUserUnique", method = RequestMethod.POST)
	public MessageVO validateUniqueUser(@RequestBody UserVO userVO) {
		return userService.validateUser(userVO);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/getAllPrograms", method = RequestMethod.GET)
	public List<ProgramVO> getAllPrograms() {
		return programService.getAllPrograms();

	}

	@CrossOrigin
	@RequestMapping(value = "/getAllActivePrograms", method = RequestMethod.GET)
	public List<ProgramVO> getAllActivePrograms() {
		return programService.getAllActivePrograms();

	}

	@CrossOrigin
	@RequestMapping(value = "/getAllProgramsById/{programid}", method = RequestMethod.GET)
	public ProgramVO getAllProgramsById(@PathVariable Integer programid) {
		return programService.getProgramById(programid);

	}

	@CrossOrigin
	@RequestMapping(value = "/getAllEntity", method = RequestMethod.GET)
	public List<ContributorVO> getAllEntity() {
		return contributorService.getAllEntity();
	}

	@CrossOrigin
	@RequestMapping(value = "/getAllEntityById/{contributorid}", method = RequestMethod.GET)
	public ContributorVO getAllEntity(@PathVariable Integer contributorid) {
		return contributorService.getEntityById(contributorid);
	}

	@CrossOrigin
	@RequestMapping(value = "/getAllUsersById/{userid}", method = RequestMethod.GET)
	public UserVO getAllUserById(@PathVariable Integer userid) {
		return userService.getUserById(userid);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/getUsersByNameLike/{roleid}/{search}", method = RequestMethod.GET)
	public List<UserVO> getAllUserById( @PathVariable Integer roleid, @PathVariable String search) {
		return userService.getUserWithNameLike(search, roleid);
	}

	@CrossOrigin
	@RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
	public List<UserVO> getAllUser() {
		return userService.getAllUser();
	}

	@CrossOrigin
	@RequestMapping(value = "/saveSchoolsByPrograms", method = RequestMethod.POST)
	public MessageVO saveSchoolsByPrograms(@RequestBody SchoolProgramMappingVO schoolProgramVO) {
		String schoolByPrograms = schoolProgramMappingService.saveContributorProgramMapping(schoolProgramVO);
		if ("success".equals(schoolByPrograms)) {
			System.out.println("SchoolAndPrograms saved Successfully");
			return new MessageVO("success", "SchoolAndPrograms is saved.", schoolProgramVO.getProgramName());
		} else if ("updatesuccess".equals(schoolByPrograms)) {
			return new MessageVO("update success", "SchoolAndPrograms is updated.", schoolProgramVO.getProgramName());
		} else {
			System.out.println("failed to save SchoolAndPrograms ");
			return new MessageVO("failure", "SchoolAndPrograms not saved.", "Issues in saving SchoolAndPrograms");
		}
	}
	
	@CrossOrigin
	@RequestMapping(value = "/deleteUser/{userid}", method = RequestMethod.DELETE)
	public MessageVO deleteUser(@PathVariable Integer userid) {
		if ("success".equals(userService.deleteUser(userid))) {
			System.out.println("deleted successfully");
			return new MessageVO("deleted Successfully", "User Deleted");
		} else {
			return new MessageVO("Delete Operation Failed", "User is Not Exist");
		}
	}

	@CrossOrigin
	@RequestMapping(value = "/deleteEntity/{contributorid}", method = RequestMethod.DELETE)
	public MessageVO deleteEntity(@PathVariable Integer contributorid) {
		if ("success".equals(contributorService.deleteEntity(contributorid))) {
			System.out.println("deleted successfully");
			return new MessageVO("deleted Successfully", "Contributor Deleted");
		} else {
			System.out.println("Contributor not exist");
			return new MessageVO("Delete Operation Failed", "Contributor is Not Exist");
		}
	}

	@CrossOrigin
	@RequestMapping(value = "/deleteProgram/{programid}", method = RequestMethod.DELETE)
	public MessageVO deleteProgram(@PathVariable Integer programid) {
		if ("success".equals(programService.deleteProgram(programid))) {
			System.out.println("deleted successfully");
			return new MessageVO("deleted Successfully", "Program Deleted");
		} else {
			return new MessageVO("Delete Operation Failed", "Program is Not Exist");
		}
	}

	@CrossOrigin
	@RequestMapping(value = "/getAdminDashboard", method = RequestMethod.GET)
	public AdminDashboardVO getAdminDashInfo() {
		LOGGER.debug("Getting info from ADMIN DAshBoard...............");
		return adminService.getAdminDashboard();

	}

	@CrossOrigin
	@RequestMapping(value = "/getSchoolByPrograms", method = RequestMethod.GET)
	public List<SchoolProgramMappingVO> getAllSchoolByPrograms() {
		return schoolProgramMappingService.getAllSchoolsByPrograms();

	}

	@CrossOrigin
	@GetMapping("/getProgramByNames/{search}")
	public List<Program> getAllProgramByNames(@PathVariable String search) {
		return programService.getAllProgramsByNames(search);

	}

	@CrossOrigin
	@GetMapping("/getContributorByNames/{search}")
	public List<ContributorVO> getAllContributorByNames(@PathVariable String search) {
		return contributorService.getAllContributorByNames(search);
	}
	
	
	
	
	/*	
	@CrossOrigin
	@GetMapping("/getSchoolProgramMappingByContributorId/{contributorId}")
	public List<SchoolProgramMappingVO> getSchoolProgramMappingByContributorId(@PathVariable Integer contributorId){
		return schoolProgramMappingService.getSchoolProgramMappingByContributorId(contributorId);
	}
	
	
	@CrossOrigin
	@PostMapping("/logweight/{scalenumber}")
	public String logWeight(@PathVariable("scalenumber") Long scalenumber, HttpServletRequest httpServletRequest) {
    	System.out.println("The scale number is --->" + scalenumber + "<---------");
    	Map<String,String[]> map = httpServletRequest.getParameterMap();
    	if(null != map) {
    		Set<String> keys = map.keySet();
    		for(String key: keys) {
    			System.out.println("----->" + key + "<----------");
    			String[] values = map.get(key);
    			for(String value : values) {
    				System.out.println("\t ------>" + value + "<-----------");
    			}
    		}
    	}
    	Enumeration<String> headerKeys  = httpServletRequest.getHeaderNames();
    	while (headerKeys.hasMoreElements()) {
    		String element = headerKeys.nextElement();
    		System.out.println("element name -->" + element + "<---element value -->" + httpServletRequest.getHeader(element));
    	}
    	return "success";
    }
	
	@CrossOrigin
	@GetMapping("/getlogweight/{scalenumber}")
	public String getLogWeight(@PathVariable("scalenumber") Integer scalenumber, HttpServletRequest httpServletRequest) {
    	System.out.println("The scale number is --->" + scalenumber + "<---------");
    	String data = httpServletRequest.getParameter(AppConstants.DATA);
    	if(null != scalenumber && null != data) {
    		Double weight = null;
    		try {
    			weight = Double.parseDouble(data);
    		} catch(NumberFormatException nfe) {
    			System.out.println("ERROR : Number format Exception in converting weight to double value -->" + nfe.getStackTrace());
    		}
    		if(null != weight) {
    			mapDigitalScaleService.saveWeight(scalenumber, weight);
    			return "success";
    		}
    	}
    	return AppConstants.FAILURE;
    }
	*/

}
