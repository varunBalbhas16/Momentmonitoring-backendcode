package com.irctn.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.irctn.config.ApplicationConfig;
import com.irctn.model.District;
import com.irctn.model.DriverAttendance;
import com.irctn.model.Vehicle;
import com.irctn.model.VehicleDriver;
import com.irctn.repository.DistrictRepository;
import com.irctn.repository.DriverAttendanceRepository;
import com.irctn.repository.VehicleDriverRepository;
import com.irctn.repository.VehicleRepository;
import com.irctn.service.DriverVehicleService;
import com.irctn.util.AppConstants;
import com.irctn.vo.DistrictVO;
import com.irctn.vo.DriverAttendanceRankingVO;
import com.irctn.vo.DriverAttendanceVO;
import com.irctn.vo.TicketDPMRankingVO;
import com.irctn.vo.UserVO;
import com.irctn.vo.VehicleDriverVO;
import com.irctn.vo.VehicleVO;
import com.irctn.vo.mobile.DriverAttendanceListResponseVO;
import com.irctn.vo.mobile.DriverListResponseVO;
import com.irctn.vo.mobile.MobileResponseVO;
import com.irctn.vo.mobile.UserShortDetailVO;
import com.irctn.vo.mobile.request.DriverAttendanceListRequestVO;
import com.irctn.vo.mobile.request.SearchDatesVO;
import com.irctn.vo.mobile.request.VehicleDriverRequestVO;

@Service
public class DriverVehicleServiceImpl implements DriverVehicleService {

	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.serviceimpl.DriverVehicleServiceImpl");
	
	@Autowired
	DistrictRepository districtRepository;	
	
	@Autowired
	VehicleDriverRepository vehicleDriverRepository;
	
	@Autowired
	VehicleRepository vehicleRepository;
	
	@Autowired
	DriverAttendanceRepository driverAttendanceRepository;
	
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	
	@Override
	public DriverListResponseVO getMobileUserDetails(List<UserVO> users) {
		DriverListResponseVO responseVO = new DriverListResponseVO();
		if(null == users || users.isEmpty()) {
			responseVO.setStatus(AppConstants.FAILURE);
			responseVO.setErrorMessage("Sorry. There are no Drivers available to be listed.");
			responseVO.setDrivers(null);
			return responseVO;
		}
		List<UserShortDetailVO> shortDetails = new ArrayList<UserShortDetailVO>();
		for(UserVO user : users) {
			UserShortDetailVO shortVO = new UserShortDetailVO();
			shortVO.setUsername(user.getFirstName() + " " + user.getLastName());
			shortVO.setContact(user.getContact());
			shortVO.setEmail(user.getEmail());
			shortDetails.add(shortVO);			
		}
		
		if(!shortDetails.isEmpty()) {			
			responseVO.setStatus(AppConstants.SUCCESS);
			responseVO.setErrorMessage("");
			responseVO.setDrivers(shortDetails);
			return responseVO;
		} else {
			responseVO.setStatus(AppConstants.FAILURE);
			responseVO.setErrorMessage("Sorry. There are no Drivers to be listed.");
			responseVO.setDrivers(null);
			return responseVO;
		}
		
	}

	@Override
	public MobileResponseVO markAttendance(VehicleDriverRequestVO vo) {
		if(null == vo || null == vo.getDriverEmpId() || null == vo.getDriverName() || null == vo.getUserId() || null == vo.getShift())	{
			return new MobileResponseVO(AppConstants.FAILURE, "Please Fill All Mandatory Fields to Mark Attendance.");
		}
		
		VehicleDriver user = vehicleDriverRepository.findByDriverempidIgnoreCase(vo.getDriverEmpId());
		if(null == user) {
			return new MobileResponseVO(AppConstants.FAILURE, "User is not available to mark attendance.");
		} else {
			Date today = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");			
			String todayString = sdf.format(today);
			boolean isPresent = AppConstants.PRESENT.equalsIgnoreCase(vo.getAttendance());
			
			SimpleDateFormat sdfDBFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String todayStart = todayString + " 00:00:01";
			String todayEnd = todayString + " 23:59:59";
			String registration = null;
			if(isPresent && null != vo.getVehicle() && !vo.getVehicle().isEmpty() && !vo.getVehicle().equalsIgnoreCase(AppConstants.VEHICLE_OTHERS)) {
				List<DriverAttendance> markedVehicles;
				String readableVehicle = vo.getVehicle();
				registration = readableVehicle.replaceAll("\\s", ""); 
				try {
					markedVehicles = driverAttendanceRepository.findByVehicleAndDateBetween(registration, sdfDBFormat.parse(todayStart), sdfDBFormat.parse(todayEnd));
				} catch (ParseException e) {
					return new MobileResponseVO(AppConstants.FAILURE, "System Error. Please contact admin.");
				}			
				
				if(null != markedVehicles && markedVehicles.size() == 2) {
					return new MobileResponseVO(AppConstants.FAILURE, registration + " Already taken by Another Driver.");
				} else if(null != markedVehicles && markedVehicles.size() == 1) {
					//check the time of the last attendance log, this should be minimum 7 hours earlier than now
					DriverAttendance attendance = null;
					if(null != (attendance = markedVehicles.get(0)) ){
						int markedShift = attendance.getShift();
						int currentShift = 0;
						if(AppConstants.FIRST_SHIFT.equalsIgnoreCase(vo.getShift())) {
							currentShift = AppConstants.FIRST_SHIFT_INT;
						} else {
							currentShift = AppConstants.SECOND_SHIFT_INT;
						}
						if(currentShift == markedShift && attendance.getDriverid() != user.getDriverid()) {
							return new MobileResponseVO(AppConstants.FAILURE, "Sorry, this Vehicle is already marked for Another Driver.");
						}
						Date lastMarkedAttendance = attendance.getDate();
						long diff = new Date().getTime() - lastMarkedAttendance.getTime();
						long diffMinutes = diff / (60 * 1000) % 60;
						long diffHours = diff / (60 * 60 * 1000) % 24;
						long diffDays = diff / (24 * 60 * 60 * 1000);
						LOGGER.info("-----> The difference in time to log attendance for " + registration + " is : " + diffDays + " days , " + diffHours + " hours, " + diffMinutes + " minutes.");
						if(diffHours < 4) {
							LOGGER.info(" The difference in time to log this attendance is : " + diffHours + " : " + diffMinutes);
							return new MobileResponseVO(AppConstants.FAILURE, "Attendance marked for current shift. Please mark attendance later.");
						}
					}					
				}
			}
			
			List<DriverAttendance> markedDriver;
			try {
				markedDriver = driverAttendanceRepository.findByDriveridAndDateBetween(user.getDriverid(), sdfDBFormat.parse(todayStart), sdfDBFormat.parse(todayEnd));
			} catch (ParseException e) {
				return new MobileResponseVO(AppConstants.FAILURE, "System Error. Please contact admin.");
			}			
			if(isPresent && null != markedDriver && markedDriver.size() == 2) {
				return new MobileResponseVO(AppConstants.FAILURE, "Attendance already marked for this driver.");
			} else if(isPresent && null != markedDriver && markedDriver.size() == 1) {
				//check the time of the last attendance log, this should be minimum 7 hours earlier than now
				DriverAttendance attendance = null;
				if(null != (attendance = markedDriver.get(0)) ){
					Date lastMarkedAttendance = attendance.getDate();
					long diff = new Date().getTime() - lastMarkedAttendance.getTime();
					long diffMinutes = diff / (60 * 1000) % 60;
					long diffHours = diff / (60 * 60 * 1000) % 24;
					long diffDays = diff / (24 * 60 * 60 * 1000);
					LOGGER.info("-----> The difference in time to log attendance for " + user.getDriverid() + " is : " + diffDays + " days , " + diffHours + " hours, " + diffMinutes + " minutes.");
					if(diffHours < 4) {
						LOGGER.info(" The difference in time to log this attendance is : " + diffHours + " : " + diffMinutes);
						return new MobileResponseVO(AppConstants.FAILURE, "Attendance marked for current shift. Please mark attendance later.");
					}
				}
			}
			
			DriverAttendance model = new DriverAttendance();
			model.setDate(new Date());
			if(AppConstants.PRESENT.equalsIgnoreCase(vo.getAttendance()) ) {
				model.setAttendance(AppConstants.STATUS_ACTIVE);
			} else {
				model.setAttendance(AppConstants.STATUS_INACTIVE);
			}			
			model.setCreatedby(vo.getUserId());
			model.setDriverid(user.getDriverid());
			if(null != registration) {
				model.setVehicle(registration);
			}
			if(AppConstants.FIRST_SHIFT.equalsIgnoreCase(vo.getShift())) {
				model.setShift(AppConstants.FIRST_SHIFT_INT);
			} else {
				model.setShift(AppConstants.SECOND_SHIFT_INT);
			}
			
			if(null != vo.getOthers()) {
				model.setOthers(vo.getOthers());
			}
			driverAttendanceRepository.save(model);
			return new MobileResponseVO(AppConstants.SUCCESS, "Attendance marked for "+ vo.getDriverEmpId());
			
		}
	}

	@Override
	public DriverAttendanceListResponseVO getAllDriverAttendance() {
		List<DriverAttendance> attendances = driverAttendanceRepository.findAll();
		if(null == attendances || attendances.isEmpty())  {
			DriverAttendanceListResponseVO vo =  new DriverAttendanceListResponseVO();
			vo.setStatus(AppConstants.FAILURE);
			vo.setErrorMessage("Attendance not marked for Drivers.");
			return vo;
		
		} else {
			List<DriverAttendanceVO> list = new ArrayList<DriverAttendanceVO>();
			for(DriverAttendance attendance : attendances) {
				DriverAttendanceVO attendanceVO = new DriverAttendanceVO();
				if(AppConstants.STATUS_ACTIVE.equals(attendance.getAttendance())) {
					attendanceVO.setAttendance(AppConstants.PRESENT);
				} else {
					attendanceVO.setAttendance(AppConstants.ABSENT);
				}
				attendanceVO.setWorkDate(attendance.getDate());
				
				VehicleDriverVO vdVO = this.getDriverById(attendance.getDriverid());
				if(null != vdVO) {
					attendanceVO.setUsername(vdVO.getDriverName());
					attendanceVO.setDriverEmpId(vdVO.getDriverEmpId());
				}
				
				if(null != attendance.getVehicle() && !attendance.getVehicle().isEmpty()) {
					attendanceVO.setVehicle(attendance.getVehicle());
				}
				
				if(null != attendance.getShift()) {
					if(AppConstants.FIRST_SHIFT_INT.equals(attendance.getShift())) {
						attendanceVO.setShift(AppConstants.FIRST_SHIFT);
					} else if(AppConstants.SECOND_SHIFT_INT.equals(attendance.getShift())) {
						attendanceVO.setShift(AppConstants.SECOND_SHIFT);
					}
				}
				list.add(attendanceVO);
			}
			
			DriverAttendanceListResponseVO vo =  new DriverAttendanceListResponseVO();
			vo.setStatus(AppConstants.SUCCESS);
			vo.setErrorMessage("");
			vo.setDrivers(list);
			return vo;
		}
	}
	
	@Override
	public List<District> importDrivers() throws IOException {

		List<District> districts = new ArrayList<District>();
		List<VehicleDriver> driversList = new ArrayList<VehicleDriver>();
		
		FileInputStream fis = new FileInputStream(new File(ApplicationConfig.CSV_UPLOAD_FOLDER+"//Driver_list.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet worksheet = workbook.getSheetAt(0);
		SortedSet<String> districtNames = new TreeSet<String>();
		/*
        System.out.println("Before Iterating Excel Datas -------------------" + worksheet.getPhysicalNumberOfRows());
		for (int i = 1; i < worksheet.getPhysicalNumberOfRows()-1; i++) {
			System.out.println("--->After the Loop");
			XSSFRow row = worksheet.getRow(i);
			if(null != row.getCell(1).getStringCellValue() && !row.getCell(1).getStringCellValue().isEmpty()) {
				districtNames.add(row.getCell(1).getStringCellValue());			
			}
		}
		
		for(String model : districtNames) {
			if(null == districtRepository.findByDistrictIgnoreCase(model)) {
				System.out.println("------->Adding District to DB -------->" + model);
				District district = new District();				
				district.setDistrict(model);
				district.setCreatedon(new Date());
				//districtRepository.save(district);
				districts.add(district);
				if(null != model) {
					redisTemplate.opsForHash().delete("DISTRICT_VEHICLES", model.toUpperCase());
				}
			} else {
				System.out.println("------->District already there in the DB -------->" + model);
			}
		}
		
		if(!districts.isEmpty()) {
			System.out.println("------->Adding Districts in the DB -------->" + districts.size());
			districtRepository.save(districts);			
			redisTemplate.opsForHash().delete("DISTRICT", "ALL_DISTRICTS");
		}
		*/
		System.out.println("------>Iterating for Drivers .......");
		for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
			System.out.println("------>Row ......." + i);
			XSSFRow row = worksheet.getRow(i);
			if(null != row.getCell(1).getStringCellValue() && !row.getCell(1).getStringCellValue().isEmpty()) {	//just to ensure that we are not picking empty rows
				
				Integer districtId = null;
				DistrictVO districtVO = this.getDistrictByName(row.getCell(1).getStringCellValue());
				//if(null == district) {
				if(null == districtVO) {
					System.out.println("------>District not inthe DB to add driver  ......." + row.getCell(1).getStringCellValue());
					
					District district = new District();				
					district.setDistrict(row.getCell(1).getStringCellValue());
					district.setCreatedon(new Date());
					District savedDistrict = districtRepository.save(district);
					if(null != district.getDistrict()) {
						redisTemplate.opsForHash().delete("DISTRICT_VEHICLES", district.getDistrict().toUpperCase());
					}
					redisTemplate.opsForHash().delete("DISTRICT", "ALL_DISTRICTS");	
					districtId = savedDistrict.getDistrictid();
					
				} else {
					districtId = districtVO.getDistrictId();
				}
				
				VehicleDriverVO vehicleDriverVO = this.getDriverByEmpId(row.getCell(2).getStringCellValue());
				if(null != vehicleDriverVO && null != districtVO) {
					if(!vehicleDriverVO.getDistrict().equalsIgnoreCase(districtVO.getDistrict()) 
							|| !vehicleDriverVO.getDriverName().equalsIgnoreCase(row.getCell(3).getStringCellValue())
							) {
						//update the Driver record
						System.out.println("------>Need to update this driver   ......." + row.getCell(3).getStringCellValue());
						System.out.println("------>From DB details are district is " + districtVO.getDistrict() + " : " + vehicleDriverVO.getDistrict());
						System.out.println("------>The Driver name is " + row.getCell(3).getStringCellValue() + " : " + vehicleDriverVO.getDriverName());
						VehicleDriver vehicleDriver = vehicleDriverRepository.findByDriverempidIgnoreCase(row.getCell(2).getStringCellValue());
						vehicleDriver.setDistrictid(districtId);
						vehicleDriver.setDrivername(row.getCell(3).getStringCellValue());
						VehicleDriver savedDriver = vehicleDriverRepository.save(vehicleDriver);
						VehicleDriverVO driverVO = this.getVOFromModel(savedDriver);
						if(null != savedDriver && null != driverVO) {
							redisTemplate.opsForHash().put("DRIVER_BY_ID", savedDriver.getDriverid(), driverVO);
							redisTemplate.opsForHash().put("DRIVER_BY_EMP_ID", savedDriver.getDriverempid(), driverVO);
							redisTemplate.opsForHash().delete("DRIVERS", "DRIVERS_LIST");
						}
					}
					
				} else if(null != districtId && null == vehicleDriverVO ) {
					System.out.println("------>saving driver to DB   ......." + row.getCell(3).getStringCellValue());
					
					VehicleDriver vehicleDriver = new VehicleDriver();
					vehicleDriver.setDistrictid(districtId);
					vehicleDriver.setDriverempid(row.getCell(2).getStringCellValue());
					vehicleDriver.setDrivername(row.getCell(3).getStringCellValue());
					System.out.println("------>saving driver to DB   ......." + vehicleDriver.getDrivername());
					VehicleDriver savedDriver = vehicleDriverRepository.save(vehicleDriver);
					VehicleDriverVO driverVO = this.getVOFromModel(savedDriver);
					if(null != savedDriver && null != driverVO) {
						redisTemplate.opsForHash().put("DRIVER_BY_ID", savedDriver.getDriverid(), driverVO);
						redisTemplate.opsForHash().put("DRIVER_BY_EMP_ID", savedDriver.getDriverempid(), driverVO);
						redisTemplate.opsForHash().delete("DRIVERS", "DRIVERS_LIST");
					}
					
				} else {
					System.out.println("------>Nothing done on Row ......." + i);
				}
			}
		}
		
		return districts;
	}
	
	
	private VehicleDriverVO getDriverById(Integer vehicleDriverId) {
		if(null == vehicleDriverId) return null;
		Object object = redisTemplate.opsForHash().get("DRIVER_BY_ID", vehicleDriverId);
		if(null != object) {
			//System.out.println("--------->Returning Driver By Id  from REDIS  ......." + vehicleDriverId );
			return (VehicleDriverVO)object;
		}
		VehicleDriverVO vo = getVOFromModel(vehicleDriverRepository.findByDriverid(vehicleDriverId));
		if(null != vo) {
			redisTemplate.opsForHash().put("DRIVER_BY_ID", vehicleDriverId, vo);
			redisTemplate.opsForHash().delete("DRIVERS", "DRIVERS_LIST");
			//System.out.println("--------->Returning Driver By Id  from REDIS  ......." + vehicleDriverId );
			return vo;
		} else {
			return null;
		}
	}
	
	private VehicleDriverVO getDriverByEmpId(String empId) {
		if(null == empId) return null;
		Object object = redisTemplate.opsForHash().get("DRIVER_BY_EMP_ID", empId);
		if(null != object) {
			System.out.println("--------->Returning Driver By Emp Id  from REDIS  ......." + empId );
			return (VehicleDriverVO)object;
		}
		VehicleDriverVO vo = getVOFromModel(vehicleDriverRepository.findByDriverempidIgnoreCase(empId));
		if(null != vo) {
			redisTemplate.opsForHash().put("DRIVER_BY_EMP_ID", empId, vo);
			redisTemplate.opsForHash().delete("DRIVERS", "DRIVERS_LIST");
			System.out.println("--------->Returning Driver By Emp Id  from DB  ......." + empId );
			return vo;
		} else {
			return null;
		}
	}

	@Override
	public List<VehicleDriverVO> getAllDrivers() {
		Object object = redisTemplate.opsForHash().get("DRIVERS", "DRIVERS_LIST");
		if(null != object) {
			System.out.println("--------->Returning Drivers List from REDIS  ......." );
			return (List<VehicleDriverVO>)object;
		}
		
		List<VehicleDriver> drivers = vehicleDriverRepository.findByOrderByDrivernameAsc();
		List<VehicleDriverVO> driverVOs =  getVOListFromModelList(drivers);
		if(null != driverVOs && !driverVOs.isEmpty()) {
			System.out.println("------>Returning Drivers List from Database  ......." );
			redisTemplate.opsForHash().put("DRIVERS", "DRIVERS_LIST", driverVOs);
		}
		return driverVOs;
	}

	private List<VehicleDriverVO> getVOListFromModelList(List<VehicleDriver> drivers) {
		List<VehicleDriverVO> list = new ArrayList<VehicleDriverVO>();
		if(null == drivers || drivers.isEmpty()) return null;
		for(VehicleDriver vehicleDriver : drivers) {
			VehicleDriverVO vo = getVOFromModel(vehicleDriver);
			list.add(vo);
		}
		return list;
	}

	private VehicleDriverVO getVOFromModel(VehicleDriver vehicleDriver) {
		if(null == vehicleDriver)		return null;
		VehicleDriverVO vo = new VehicleDriverVO();
		vo.setDriverEmpId(vehicleDriver.getDriverempid());
		vo.setDriverName(vehicleDriver.getDrivername());
		if(null != vehicleDriver.getDistrictid() ) {
			District district = districtRepository.findByDistrictid(vehicleDriver.getDistrictid());
			if(null != district ) {
				vo.setDistrict(district.getDistrict());
			}
		}
		return vo;
	}

	@Override
	public List<Vehicle> importVehicles() throws IOException {
		FileInputStream fis = new FileInputStream(new File(ApplicationConfig.CSV_UPLOAD_FOLDER+"//Vehicle_list.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
        System.out.println("---------Nuimber of worksheets are : " +  workbook.getNumberOfSheets());
       
        for(int sheets = 0; sheets < workbook.getNumberOfSheets(); sheets++) {
        	System.out.println("------>Iterating for sheet ..............." + workbook.getSheetAt(sheets).getSheetName());
        	System.out.println("Before Iterating Excel Datas -------------------" + workbook.getSheetAt(sheets).getPhysicalNumberOfRows());
        	
        	for (int i = 2; i < workbook.getSheetAt(sheets).getPhysicalNumberOfRows(); i++) {
    			
    			XSSFRow row = workbook.getSheetAt(sheets).getRow(i);
    			if(null != row.getCell(1).getStringCellValue() && !row.getCell(1).getStringCellValue().trim().isEmpty()) {	//just to ensure that we are not picking empty rows
    				District district = districtRepository.findByDistrictIgnoreCase(row.getCell(1).getStringCellValue().trim());
        			if(null == district) {
        				System.out.println("------>District not inthe DB to add driver  ......." + row.getCell(1).getStringCellValue());
        			} else {
        				System.out.println("------>add vehicle  ......." + row.getCell(4).getStringCellValue());
        				VehicleVO vehicleVO = getVehicleByRegistration(row.getCell(4).getStringCellValue().trim());
        				
        				if(null == vehicleVO) {
        					Vehicle vehicle = new Vehicle();
        					vehicle.setCreatedon(new Date());
        					vehicle.setDistrictid(district.getDistrictid());
        					vehicle.setService(row.getCell(2).getStringCellValue().trim());
        					vehicle.setLocation(row.getCell(3).getStringCellValue().trim());					
        					vehicle.setRegistration(row.getCell(4).getStringCellValue().trim());
        					
        					System.out.println("------>SAving vehicle into DB  .........>" + row.getCell(4).getStringCellValue());
        					Vehicle savedVehicle = vehicleRepository.save(vehicle);
        					VehicleVO vo = getVehicleVOFromModel(savedVehicle);
        					redisTemplate.opsForHash().put("VEHICLE", savedVehicle.getRegistration(), vo);
        					redisTemplate.opsForHash().delete("VEHICLE", "VEHICLE_LIST");
        					
        				} else if(!vehicleVO.getDistrict().equalsIgnoreCase(row.getCell(1).getStringCellValue().trim())) {	//update change in district
        					Vehicle updateVechicle = vehicleRepository.findByRegistrationIgnoreCase(row.getCell(4).getStringCellValue().trim());
        					updateVechicle.setDistrictid(district.getDistrictid());
        					updateVechicle.setService(row.getCell(2).getStringCellValue().trim());
        					updateVechicle.setLocation(row.getCell(3).getStringCellValue().trim());
        					Vehicle savedVehicle = vehicleRepository.save(updateVechicle);
        					VehicleVO vo = getVehicleVOFromModel(savedVehicle);
        					redisTemplate.opsForHash().put("VEHICLE", savedVehicle.getRegistration(), vo);
        					redisTemplate.opsForHash().delete("VEHICLE", "VEHICLE_LIST");
        				}
        				else {
        					System.out.println("------>Vehicle found in DB  ......." + row.getCell(4).getStringCellValue());
        				}
        			}
    			}    			
    		}
        }
		
		return null;
	}
	
	@Override
	public List<VehicleVO> getVehicles() {
		Object object = redisTemplate.opsForHash().get("VEHICLE", "VEHICLE_LIST");
		if(null != object) {
			System.out.println("------>Returning Vehicle List from REDIS  ......." );
			return (List<VehicleVO>)object;
		}
		
		List<Vehicle> vehicles = vehicleRepository.findByOrderByRegistrationAsc();
		List<VehicleVO> voList = this.getVehicleVOListFromModelList(vehicles);
		if(null != voList && !voList.isEmpty()) {
			System.out.println("------>Returning Vehicle List from Database  ......." );
			redisTemplate.opsForHash().put("VEHICLE", "VEHICLE_LIST", voList);
		}
		return voList;
	}

	private List<VehicleVO> getVehicleVOListFromModelList(List<Vehicle> vehicles) {
		if(null == vehicles || vehicles.isEmpty()) {
			return null;
		} 
		List<VehicleVO> list = new ArrayList<VehicleVO>();
		for(Vehicle vehicle : vehicles) {
			list.add(this.getVehicleVOFromModel(vehicle));
		}
		return list;
	}

	private VehicleVO getVehicleByRegistration(String registration) {
		if(null == registration || registration.isEmpty() ) 		return null;
		Object object = redisTemplate.opsForHash().get("VEHICLE", registration);
		if(null != object) {
			return (VehicleVO)object;
		}
		VehicleVO vehicleVO = getVehicleVOFromModel(vehicleRepository.findByRegistrationIgnoreCase(registration));
		if(null != vehicleVO) {
			redisTemplate.opsForHash().put("VEHICLE", registration, vehicleVO);
		}
		return vehicleVO;
	}

	private VehicleVO getVehicleVOFromModel(Vehicle vehicle) {
		if(null == vehicle) return null;
		VehicleVO vo = new VehicleVO();
		vo.setLocation(vehicle.getLocation());
		vo.setService(vehicle.getService());
		String registration = vehicle.getRegistration();
		if(null != registration) {
			if(AppConstants.VEHICLE_OTHERS.equalsIgnoreCase(registration)) {
				vo.setRegistration(registration);
			} else {
				String readableRegistration = getRegistrationWithSpaces(registration);
				vo.setRegistration(readableRegistration);
			}			
		} 
		
		DistrictVO districtVO = getDistrictById(vehicle.getDistrictid());
		if(null != districtVO) {
			vo.setDistrict(districtVO.getDistrict());
		}
		return vo;
	}

	private String getRegistrationWithSpaces(String registration) {
		if(null == registration) return null;
		StringBuffer buffer = new StringBuffer().append(registration.substring(0, 2))
				.append(" ").append(registration.substring(2, 4))
				.append(" ").append(registration.substring(4, registration.length()-4))
				.append(" ").append(registration.substring(registration.length()-4));
		return buffer.toString();
	}

	@Override
	public DriverAttendanceListResponseVO getDriversLoggedByDpm(DriverAttendanceListRequestVO driverAttendanceReqVO) {
		if(null == driverAttendanceReqVO) return null;
		DriverAttendanceListResponseVO vo =  new DriverAttendanceListResponseVO();
		
		if( null == driverAttendanceReqVO.getUserId() || null == driverAttendanceReqVO.getDateSelected()) {
			vo.setStatus(AppConstants.FAILURE);
			vo.setErrorMessage("Invalid Inputs To Get Attendance List.");
			return vo;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");			
		String dayString = sdf.format(driverAttendanceReqVO.getDateSelected());
		
		SimpleDateFormat sdfDBFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String dayStart = dayString + " 00:00:01";
		String dayEnd = dayString + " 23:59:59";
		
		List<DriverAttendance> loggedDrivers;
		try {
			loggedDrivers = driverAttendanceRepository.findByCreatedbyAndDateBetween(driverAttendanceReqVO.getUserId(), sdfDBFormat.parse(dayStart), sdfDBFormat.parse(dayEnd));
		} catch (ParseException e) {
			vo.setStatus(AppConstants.FAILURE);
			vo.setErrorMessage("SystemError. Please Contact Admin.");
			return vo;
		}
		
		if(null == loggedDrivers || loggedDrivers.isEmpty())  {
			vo.setStatus(AppConstants.FAILURE);
			vo.setErrorMessage("Attendance not marked for Drivers.");
			return vo;
		} else {
			List<DriverAttendanceVO> list = new ArrayList<DriverAttendanceVO>();
			for(DriverAttendance attendance : loggedDrivers) {
				DriverAttendanceVO attendanceVO = new DriverAttendanceVO();
				if(AppConstants.STATUS_ACTIVE.equals(attendance.getAttendance())) {
					attendanceVO.setAttendance(AppConstants.PRESENT);					
				} else {
					attendanceVO.setAttendance(AppConstants.ABSENT);
				}
				attendanceVO.setWorkDate(attendance.getDate());
				
				VehicleDriverVO vehicleDriverVO = this.getDriverById(attendance.getDriverid());
				if(null != vehicleDriverVO) {
					attendanceVO.setUsername(vehicleDriverVO.getDriverName());
					attendanceVO.setDriverEmpId(vehicleDriverVO.getDriverEmpId());
				}
				if(null != attendance.getVehicle() && !attendance.getVehicle().isEmpty()) {					
					attendanceVO.setVehicle(attendance.getVehicle());
				} 
				
				if(null != attendance.getOthers() && !attendance.getOthers().isEmpty()) {
					attendanceVO.setOthers(attendance.getOthers());
				}
				
				if(AppConstants.FIRST_SHIFT_INT.equals(attendance.getShift()) ) {
					attendanceVO.setShift(AppConstants.FIRST_SHIFT);
				} else {
					attendanceVO.setShift(AppConstants.SECOND_SHIFT);
				}
				
				list.add(attendanceVO);
			}
			
			vo.setStatus(AppConstants.SUCCESS);
			vo.setErrorMessage("");
			vo.setDrivers(list);
			return vo;
		}
	}

	@Override
	public List<String> getAllVehicleDistricts() {
		Object object = redisTemplate.opsForHash().get("DISTRICT", "ALL_DISTRICTS");
		if(null != object) {
			System.out.println("------>Returning All District Vehicles from REDIS  .......");
			return (List<String>)object;
		}
		
		List<Integer> ids = this.vehicleRepository.findDistinctDistricts();
		
		SortedSet<String> districts = null;
		List<String> sortedDistricts = null;
		if(null != ids && !ids.isEmpty()) {			
			districts = new TreeSet<String>();
			for(Integer districtId: ids) {
				DistrictVO vo = this.getDistrictById(districtId);
				if(null != vo) {
					districts.add(vo.getDistrict());
				}
			}
		}
		
		if(null != districts && !districts.isEmpty()) {
			sortedDistricts = new ArrayList<String>(districts);
			redisTemplate.opsForHash().put("DISTRICT", "ALL_DISTRICTS", sortedDistricts);
			System.out.println("------>Returning All Vehicle Districts  from Database  .......");
		}
		return sortedDistricts;
	}

	public DistrictVO getDistrictById(Integer districtId) {
		if(null == districtId) return null;
		Object object = redisTemplate.opsForHash().get("DISTRICT_BY_ID", districtId);
		if(null != object) {
			System.out.println("------>Returning District By Id from REDIS  ......." + districtId);
			return (DistrictVO) object;
		}		
		DistrictVO vo = this.getDistrictVOFromModel(this.districtRepository.findByDistrictid(districtId));		
		redisTemplate.opsForHash().put("DISTRICT_BY_ID", vo.getDistrictId(), vo);
		System.out.println("------>Returning District By Id from Database  ......." + districtId);
		return vo;
	}

	private DistrictVO getDistrictVOFromModel(District model) {
		if(null == model) return null;
		DistrictVO vo = new DistrictVO();
		vo.setDistrictId(model.getDistrictid());
		vo.setDistrict(model.getDistrict());
		return vo;
	}

	@Override
	public List<VehicleVO> getVehiclesByDistrict(String district) {
		if(null == district || district.isEmpty()) return null;		
		String key = district.toUpperCase();
		Object object = redisTemplate.opsForHash().get("DISTRICT_VEHICLES", key);
		if(null != object) {
			System.out.println("------>Returning District Vehicles By District Name from REDIS  ......." + district);
			return (List<VehicleVO>)object;
		}
		
		DistrictVO vo = getDistrictByName(district);
		List<Vehicle> list = this.vehicleRepository.findByDistrictid(vo.getDistrictId());		
		if(null != list) {		
			list.add(0, new Vehicle(AppConstants.VEHICLE_OTHERS));
			List<VehicleVO> vehicleVOList = this.getVehicleVOListFromModelList(list);
			redisTemplate.opsForHash().put("DISTRICT_VEHICLES", key, vehicleVOList);
			System.out.println("------>Returning District Vehicles By District Name from Database  ......." + district);
			return vehicleVOList;
		} else {
			System.out.println("------> No District Vehicles By District Name   ......." + district);
			return null;
		}
	}

	private DistrictVO getDistrictByName(String district) {
		if(null == district) return null;
		String key = district.toUpperCase();
		Object object = redisTemplate.opsForHash().get("DISTRICT_BY_NAME", key);
		if(null != object) {
			System.out.println("------>Returning District By Name from REDIS  ......." + district);
			return (DistrictVO) object;
		}	
		DistrictVO vo = this.getDistrictVOFromModel(this.districtRepository.findByDistrictIgnoreCase(district));		
		redisTemplate.opsForHash().put("DISTRICT_BY_NAME", key, vo);
		System.out.println("------>Returning District By Name from Database  ......." + district);
		return vo;
	}

	@Override
	public List<VehicleDriverVO> getDriversByDistrict(String district) {
		
		DistrictVO districtVO = this.getDistrictByName(district);
		if(null == districtVO) return null;
		
		Object object = redisTemplate.opsForHash().get("DRIVERS_BY_DISTRICT", districtVO.getDistrictId());
		if(null != object) {
			System.out.println("--------->Returning Drivers List from REDIS  for district ......." + district);
			return (List<VehicleDriverVO>)object;
		}
		
		List<VehicleDriver> drivers = vehicleDriverRepository.findByDistrictidOrderByDrivernameAsc(districtVO.getDistrictId());
		List<VehicleDriverVO> driverVOs =  getVOListFromModelList(drivers);
		if(null != driverVOs && !driverVOs.isEmpty()) {
			System.out.println("------>Returning Drivers List from Database for district ......." + district + " with driver strength as : " + driverVOs.size());
			redisTemplate.opsForHash().put("DRIVERS", "DRIVERS_LIST", driverVOs);
		} else {
			System.out.println("------> No Drivers found in District  ......." + district);			
		}
		return driverVOs;
	}

	@Override
	public DriverAttendanceListResponseVO getAttendanceBetweenDates(SearchDatesVO search) {
		/*
		System.out.println("Getting attendance between dates --------- start date :" + search.getStartdate().toGMTString());
		System.out.println("Getting attendance between dates --------- end date :" + search.getEnddate().toGMTString());
		
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		sdf.setTimeZone(TimeZone.getTimeZone("IST"));
	    String date = sdf.format(search.getStartdate());		
	    
	    System.out.println(date);
	    */
	    
		List<DriverAttendance> attendances = driverAttendanceRepository.findByDateBetween(search.getStartdate(), search.getEnddate());
		if(null == attendances || attendances.isEmpty())  {
			DriverAttendanceListResponseVO vo =  new DriverAttendanceListResponseVO();
			vo.setStatus(AppConstants.FAILURE);
			vo.setErrorMessage("Attendance not marked for Drivers.");
			return vo;
		
		} else {
			List<DriverAttendanceVO> list = new ArrayList<DriverAttendanceVO>();
			for(DriverAttendance attendance : attendances) {
				DriverAttendanceVO attendanceVO = new DriverAttendanceVO();
				if(AppConstants.STATUS_ACTIVE.equals(attendance.getAttendance())) {
					attendanceVO.setAttendance(AppConstants.PRESENT);
				} else {
					attendanceVO.setAttendance(AppConstants.ABSENT);
				}
				attendanceVO.setWorkDate(attendance.getDate());
				
				VehicleDriverVO vdVO = this.getDriverById(attendance.getDriverid());
				if(null != vdVO) {
					attendanceVO.setUsername(vdVO.getDriverName());
					attendanceVO.setDriverEmpId(vdVO.getDriverEmpId());
					attendanceVO.setDistrict(vdVO.getDistrict());
				}
				
				if(null != attendance.getVehicle() && !attendance.getVehicle().isEmpty()) {
					attendanceVO.setVehicle(attendance.getVehicle());
				}
				
				if(null != attendance.getShift()) {
					if(AppConstants.FIRST_SHIFT_INT.equals(attendance.getShift())) {
						attendanceVO.setShift(AppConstants.FIRST_SHIFT);
					} else if(AppConstants.SECOND_SHIFT_INT.equals(attendance.getShift())) {
						attendanceVO.setShift(AppConstants.SECOND_SHIFT);
					}
				}
				if(null != attendance.getOthers() && !attendance.getOthers().isEmpty()) {
					attendanceVO.setOthers(attendance.getOthers());
				}
				
				
				list.add(attendanceVO);
			}
			
			DriverAttendanceListResponseVO vo =  new DriverAttendanceListResponseVO();
			vo.setStatus(AppConstants.SUCCESS);
			vo.setErrorMessage("");
			vo.setDrivers(list);
			return vo;
		}
	}
	
	@Override
	public List<DriverAttendanceRankingVO> getDriversAttendanceRankingByDateRange() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");		
		String startString = "01/01/2020 00:00:01";
		String endString = "31/01/2020 23:59:59";
		Date start = new Date();
		Date end = new Date();
		try {
			start = formatter.parse(startString);
			end = formatter.parse(endString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<DriverAttendanceRankingVO> presentList = null;
		List<DriverAttendanceRankingVO> absentList = null;
		List<DriverAttendance> attendances = driverAttendanceRepository.findByDateBetweenOrderByDriverid(start, end);
		if(null == attendances || attendances.isEmpty())  {			
			return null;
		
		} else {
			presentList = new ArrayList<DriverAttendanceRankingVO>();
			String driver = null;
			int presents = 0;
			int absents = 0;
			Integer tempDriverId = null;
			
			for(DriverAttendance attendance : attendances) {
				if(null == tempDriverId) {
					tempDriverId = attendance.getDriverid();
				} else if(!tempDriverId.equals(attendance.getDriverid()) ) {
					VehicleDriverVO vdVO = this.getDriverById(tempDriverId);
					if(null != vdVO) {
						presentList.add(new DriverAttendanceRankingVO(0, vdVO.getDriverName(), presents) );
					}
					presents = 0;
					absents = 0;
					tempDriverId = attendance.getDriverid();
				} 
				
				if(AppConstants.STATUS_ACTIVE.equals(attendance.getAttendance())) {
					presents++;
				} else {
					absents++;
				}						
				
			}
			VehicleDriverVO vdVO = this.getDriverById(tempDriverId);
			if(null != presentList && null != vdVO) {
				presentList.add(new DriverAttendanceRankingVO(0, vdVO.getDriverName(), presents) );
			}
			
			if(null != presentList && !presentList.isEmpty()) {
				Collections.sort(presentList, Comparator.comparingInt(DriverAttendanceRankingVO :: getDays).reversed());
				for(DriverAttendanceRankingVO vo : presentList) {
					System.out.println("----------------->list -->" + vo.getDriverName() + " : " + vo.getDays());				
				}				
			}
			return presentList;
		}
		
	}
}
