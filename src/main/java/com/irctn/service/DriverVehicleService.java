package com.irctn.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.irctn.model.District;
import com.irctn.model.Vehicle;
import com.irctn.vo.DriverAttendanceRankingVO;
import com.irctn.vo.UserVO;
import com.irctn.vo.VehicleDriverVO;
import com.irctn.vo.VehicleVO;
import com.irctn.vo.mobile.DriverAttendanceListResponseVO;
import com.irctn.vo.mobile.DriverListResponseVO;
import com.irctn.vo.mobile.MobileResponseVO;
import com.irctn.vo.mobile.request.DriverAttendanceListRequestVO;
import com.irctn.vo.mobile.request.SearchDatesVO;
import com.irctn.vo.mobile.request.VehicleDriverRequestVO;

public interface DriverVehicleService {

	public List<District> importDrivers() throws IOException;

	public List<VehicleDriverVO> getAllDrivers();

	public List<Vehicle> importVehicles() throws FileNotFoundException, IOException;

	public List<VehicleVO> getVehicles();

	public DriverAttendanceListResponseVO getDriversLoggedByDpm(DriverAttendanceListRequestVO inputVO);
	
	public DriverListResponseVO getMobileUserDetails(List<UserVO> users);

	public MobileResponseVO markAttendance(VehicleDriverRequestVO vo);

	public DriverAttendanceListResponseVO getAllDriverAttendance();

	public List<String> getAllVehicleDistricts();

	public List<VehicleVO> getVehiclesByDistrict(String district);

	public List<VehicleDriverVO> getDriversByDistrict(String district);

	public DriverAttendanceListResponseVO getAttendanceBetweenDates(SearchDatesVO vo);

	List<DriverAttendanceRankingVO> getDriversAttendanceRankingByDateRange();
	
}
