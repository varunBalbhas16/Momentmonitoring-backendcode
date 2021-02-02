package com.irctn.controller;

import java.io.IOException;
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

import com.irctn.model.District;
import com.irctn.model.Vehicle;
import com.irctn.service.CentreDepartmentService;
import com.irctn.service.DepartmentService;
import com.irctn.service.DriverVehicleService;
import com.irctn.service.MobileService;
import com.irctn.service.TicketService;
import com.irctn.service.TicketTripNavigationService;
import com.irctn.service.TicketTripService;
import com.irctn.service.UserService;
import com.irctn.util.AppConstants;
import com.irctn.vo.DriverAttendanceVO;
import com.irctn.vo.MessageVO;
import com.irctn.vo.UserVO;
import com.irctn.vo.VehicleDriverVO;
import com.irctn.vo.VehicleVO;
import com.irctn.vo.mobile.DistrictListVO;
import com.irctn.vo.mobile.DoNotDisturbVO;
import com.irctn.vo.mobile.DriverAttendanceListResponseVO;
import com.irctn.vo.mobile.MobileResponseVO;
import com.irctn.vo.mobile.ResetPasswordWrapper;
import com.irctn.vo.mobile.UserVOMobile;
import com.irctn.vo.mobile.VehicleDriverListResponseVO;
import com.irctn.vo.mobile.VehicleListVO;
import com.irctn.vo.mobile.request.DriverAttendanceListRequestVO;
import com.irctn.vo.mobile.request.ResetPasswordVO;
import com.irctn.vo.mobile.request.SearchDatesVO;
import com.irctn.vo.mobile.request.VehicleDriverRequestVO;

@RestController
@RequestMapping(value = "/api/home/")
public class MobileController {

	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.controller.MobileController");

	@Autowired
	UserService userService;
	
	@Autowired
	CentreDepartmentService centreDepartmentService;
	
	@Autowired
	DepartmentService departmentService;
	
	@Autowired
	TicketService ticketService;
	
	@Autowired
	TicketTripService ticketTripService;
	
	@Autowired
	TicketTripNavigationService ticketTripNavigationService;
	
	@Autowired
	MobileService mobileService;
	
	@Autowired
	DriverVehicleService driverVehicleService;
	
	@CrossOrigin
	@RequestMapping(value = "/mMainMenu/{userId}", method = RequestMethod.GET)
	public UserVOMobile getLandingPage(@PathVariable Integer userId) {
		System.out.println("-------------------> In the Mobile Controller method getLandingPage for userId ------------->" + userId);		
		if(null == userId) return null;
		UserVO vo = userService.getUserById(userId);
		if(null == vo) return null;
		return mobileService.getUser(vo);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mListAllDrivers", method = RequestMethod.GET)
	public VehicleDriverListResponseVO getAllDrivers() {
		System.out.println("-------------------> In the Mobile Controller method getAllDrivers------------->");		
		List<VehicleDriverVO> driverList = driverVehicleService.getAllDrivers();
		if(null == driverList) return null;
		
		VehicleDriverListResponseVO vo = new VehicleDriverListResponseVO();
		vo.setDrivers(driverList);
		vo.setStatus(AppConstants.SUCCESS);
		return vo;
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mListAllDrivers/{district}", method = RequestMethod.GET)
	public VehicleDriverListResponseVO getDriversByDistrict(@PathVariable String district) {
		System.out.println("-------------------> In the Mobile Controller method getDriversByDistrict ------------->" + district);		
		List<VehicleDriverVO> driverList = driverVehicleService.getDriversByDistrict(district);
		VehicleDriverListResponseVO vo = new VehicleDriverListResponseVO();
		if(null == driverList) {
			vo.setDrivers(null);
			vo.setStatus(AppConstants.FAILURE);			
		} else {
			vo.setDrivers(driverList);
			vo.setStatus(AppConstants.SUCCESS);			
		}
		return vo;
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mListDrivers", method = RequestMethod.POST)
	public DriverAttendanceListResponseVO getDriversLoggedByDpm(@RequestBody DriverAttendanceListRequestVO inputVO) {
		System.out.println("-------------------> In the Mobile Controller method getDriversLoggedByDpm------------->");		
		return driverVehicleService.getDriversLoggedByDpm(inputVO);
	}
	
	
	@CrossOrigin
	@RequestMapping(value = "/mMarkAttendance", method = RequestMethod.POST)
	public MobileResponseVO markAttendance(@RequestBody VehicleDriverRequestVO vo) {
		System.out.println("-------------------> In the Mobile Controller method markAttendance ------------->");
		return driverVehicleService.markAttendance(vo);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mListDriverAttendance", method = RequestMethod.GET)
	public DriverAttendanceListResponseVO getAllDriverAttendance() {
		System.out.println("-------------------> In the Mobile Controller method getAllDriverAttendance------------->");		
		return driverVehicleService.getAllDriverAttendance();
	}
	
	
	@CrossOrigin
	@RequestMapping(value = "/getAttendanceBwDates", method = RequestMethod.POST)
	public List<DriverAttendanceVO> getAttendanceBetweenDates(@RequestBody SearchDatesVO search) {
		System.out.println("-------------------> In the Mobile Controller method markAttendance ------------->");
		DriverAttendanceListResponseVO vo =  driverVehicleService.getAttendanceBetweenDates(search);
		if(AppConstants.SUCCESS.equalsIgnoreCase(vo.getStatus()) ){
			return vo.getDrivers();
		}
		return null;
	}
	
	@CrossOrigin
	@RequestMapping(value = "/getAllAttendance", method = RequestMethod.GET)
	public List<DriverAttendanceVO> getAllAttendance() {
		System.out.println("-------------------> In the Mobile Controller method getAllttendance------------->");		
		DriverAttendanceListResponseVO vo = driverVehicleService.getAllDriverAttendance();
		if(AppConstants.SUCCESS.equalsIgnoreCase(vo.getStatus()) ){
			return vo.getDrivers();
		}
		return null;
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mListVehicles", method = RequestMethod.GET)
	public VehicleListVO getAllVehicles() {
		System.out.println("-------------------> In the Mobile Controller method getAllVehicles------------->");		
		List<VehicleVO> list = driverVehicleService.getVehicles();
		VehicleListVO vo = new VehicleListVO();
		if(null == list || list.isEmpty()) {
			vo.setErrorMessage("No vehicles mapped in the system.");
			vo.setStatus(AppConstants.FAILURE);
			vo.setVehicles(null);
		} else {
			vo.setErrorMessage("");
			vo.setStatus(AppConstants.SUCCESS);
			vo.setVehicles(list);
		}
		return vo;
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mListVehicleDistricts", method = RequestMethod.GET)
	public DistrictListVO getAllVehicleDistricts() {
		System.out.println("-------------------> In the Mobile Controller method getAllVehicleDistricts------------->");		
		List<String> list = driverVehicleService.getAllVehicleDistricts();
		DistrictListVO vo = new DistrictListVO();
		if(null == list || list.isEmpty()) {
			vo.setErrorMessage("No district mapped in the system.");
			vo.setStatus(AppConstants.FAILURE);
			vo.setDistricts(null);
		} else {
			vo.setErrorMessage("");
			vo.setStatus(AppConstants.SUCCESS);
			vo.setDistricts(list);
		}
		return vo;
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mListDistricts", method = RequestMethod.GET)
	public DistrictListVO getAllDistricts() {
		System.out.println("-------------------> In the Mobile Controller method getAllDistricts------------->");		
		List<String> list = driverVehicleService.getAllVehicleDistricts();
		DistrictListVO vo = new DistrictListVO();
		if(null == list || list.isEmpty()) {
			vo.setErrorMessage("No district mapped in the system.");
			vo.setStatus(AppConstants.FAILURE);
			vo.setDistricts(null);
		} else {
			vo.setErrorMessage("");
			vo.setStatus(AppConstants.SUCCESS);
			vo.setDistricts(list);
		}
		return vo;
	}
	
	
	@CrossOrigin
	@RequestMapping(value = "/mListDistrictVehicles/{district}", method = RequestMethod.GET)
	public VehicleListVO getVehiclesByDistrict(@PathVariable String district) {
		System.out.println("-------------------> In the Mobile Controller method getVehiclesByDistrict------------->" + district);		
		List<VehicleVO> list = driverVehicleService.getVehiclesByDistrict(district);
		VehicleListVO vo = new VehicleListVO();
		if(null == list || list.isEmpty()) {
			vo.setErrorMessage("No vehicles mapped in the system.");
			vo.setStatus(AppConstants.FAILURE);
			vo.setVehicles(null);
		} else {
			vo.setErrorMessage("");
			vo.setStatus(AppConstants.SUCCESS);
			vo.setVehicles(list);
		}
		return vo;
	}
	
	
	@CrossOrigin
	@RequestMapping(value = "/mResetPassword", method = RequestMethod.POST)
	public ResetPasswordWrapper resetPassword(@RequestBody ResetPasswordVO vo) {
		System.out.println("-------------------> In the Mobile Controller method resetPassword ------------->");
		return mobileService.resetPassword(vo);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mDoNotDisturb", method = RequestMethod.POST)
	public MobileResponseVO saveDoNotDisturbDPM(@RequestBody DoNotDisturbVO vo) {
		
		System.out.println("-------------------> In the Mobile Controller method doNotDisturbDPM ------------->");
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("-------------------> In the Mobile Controller method doNotDisturbDPM ------------->");
		}
		MessageVO messageVO = mobileService.saveDoNotDisturb(vo);
		return new MobileResponseVO(messageVO.getResult(), messageVO.getMessage());
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mDeactivateDoNotDisturb", method = RequestMethod.POST)
	public MobileResponseVO deactivateDoNotDisturbDPM(@RequestBody DoNotDisturbVO vo) {
		
		System.out.println("-------------------> In the Mobile Controller method deactivateDoNotDisturbDPM ------------->");
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("-------------------> In the Mobile Controller method deactivateDoNotDisturbDPM ------------->");
		}
		if(null == vo.getUserId()) return null;
		MessageVO messageVO = mobileService.deactivateDND(vo.getUserId());
		return new MobileResponseVO(messageVO.getResult(), messageVO.getMessage());
	}
	
	@CrossOrigin
	@RequestMapping(value = "/importDistricts/20191122", method = RequestMethod.GET)
	public List<District> importDistricts() throws IOException {
		System.out.println("-------------------> In the Mobile Controller method importDistricts------------->");		
		return driverVehicleService.importDrivers();
	}
	
	@CrossOrigin
	@RequestMapping(value = "/importVehicles/20191122", method = RequestMethod.GET)
	public List<Vehicle> importVehicles() throws IOException {
		System.out.println("-------------------> In the Mobile Controller method importVehicles------------->");		
		return driverVehicleService.importVehicles();
	}
}
