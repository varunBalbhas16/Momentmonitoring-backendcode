package com.irctn.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.irctn.service.CentreService;
import com.irctn.service.DepartmentService;
import com.irctn.service.TicketService;
import com.irctn.service.TicketTripNavigationService;
import com.irctn.service.TicketTripService;
import com.irctn.service.UserService;
import com.irctn.service.ZoneService;
import com.irctn.util.AppConstants;
import com.irctn.vo.MessageVO;
import com.irctn.vo.RaiseTicketVO;
import com.irctn.vo.TicketDetailInProgressVO;
import com.irctn.vo.TicketTripStatsVO;
import com.irctn.vo.TicketVO;
import com.irctn.vo.UserVO;
import com.irctn.vo.mobile.MobileResponseVO;
import com.irctn.vo.mobile.TicketProgressListWrapper;
import com.irctn.vo.mobile.TicketProgressWrapper;
import com.irctn.vo.mobile.TicketTripCostWrapper;
import com.irctn.vo.mobile.TicketTripNavigationWrapper;
import com.irctn.vo.mobile.TicketTripVOWrapper;
import com.irctn.vo.mobile.TicketTripsReimbursementWrapper;
import com.irctn.vo.mobile.TicketVOWrapper;
import com.irctn.vo.mobile.request.SearchDatesVO;
import com.irctn.vo.mobile.request.SelfTicketRequestVO;
import com.irctn.vo.mobile.request.TicketTripNavigationRequestVO;
import com.irctn.vo.mobile.request.TicketTripStartRequestVO;
import com.irctn.vo.mobile.request.TicketUpdateRequestVO;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/home")
public class TicketController {

	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.controller.TicketController");

	@Autowired
	UserService userService;
	
	@Autowired
	ZoneService zoneService;
	
	@Autowired
	DepartmentService departmentService;
	
	@Autowired
	CentreService centreService;
	
	@Autowired
	TicketService ticketService;
	
	@Autowired
	TicketTripService ticketTripService;
	
	@Autowired
	TicketTripNavigationService ticketTripNavigationService;
	
	@CrossOrigin
	@RequestMapping(value = "/saveTicket", method = RequestMethod.POST)
	public MessageVO saveTicket(@RequestBody RaiseTicketVO raiseTicketVO) {
		return ticketService.raiseTicket(raiseTicketVO, false);		
	}
	
	@CrossOrigin
	@RequestMapping(value = "/listTickets", method = RequestMethod.GET)
	public @ResponseBody List<TicketVO> listAllTickets() {	
		return ticketService.getAllTickets();		
	}
	
	@CrossOrigin
	@RequestMapping(value = "/deleteTicket/{ticket}", method = RequestMethod.DELETE)
	public MessageVO deleteCentre(@PathVariable Integer ticket) {
		if ("success".equals(ticketService.deleteTicket(ticket))) {
			System.out.println("Ticket deleted successfully " + ticket);
			return new MessageVO("Ticket deleted Successfully", "Ticket Deleted");
		} else {
			System.out.println("Ticket does not exist" + ticket);
			return new MessageVO("Delete Operation Failed", "Ticket does Not Exist");
		}
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mTicketList/{userId}", method = RequestMethod.GET)
	public TicketProgressListWrapper getTicketsForDPM(@PathVariable Integer userId) {
		System.out.println("---------------------> In the Ticket Controller mobile call method getTicketsForDPM------------->");
		return ticketService.getTicketsForDPMWrapped(userId);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mTicketCurrent/{userId}", method = RequestMethod.GET)
	public TicketProgressWrapper getCurrentTicket(@PathVariable Integer userId) {
		System.out.println("-------------------> In the Ticket Controller mobile call method getCurrentTicket------------->");
		return ticketService.getCurrentTicketForDPM(userId);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mTicketDetail/{ticket}", method = RequestMethod.GET)
	public TicketVOWrapper getTicket(@PathVariable Integer ticket) {
		System.out.println("-------------------> In the Ticket Controller mobile call method getTicket ------------->" + ticket);
		return ticketService.getTicket(ticket);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mTicketAccept", method = RequestMethod.POST)
	public TicketVOWrapper acceptTicket(@RequestBody TicketUpdateRequestVO ticketUpdateRequestVO) {
		System.out.println("-------------------> In the Ticket Controller mobile call method acceptTicket ------------->");
		return ticketService.acceptTicket(ticketUpdateRequestVO, true);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mTicketReject", method = RequestMethod.POST)
	public TicketVOWrapper rejectTicket(@RequestBody TicketUpdateRequestVO ticketUpdateRequestVO) {
		System.out.println("-------------------> In the Ticket Controller mobile call method rejectTicket ------------->");
		return ticketService.acceptTicket(ticketUpdateRequestVO, false);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mTicketTripStart", method = RequestMethod.POST)	
	public TicketTripVOWrapper setTicketTripStart(@RequestBody TicketTripStartRequestVO ticketTripStartRequestVO) {
		System.out.println("-------------------> In the Ticket Controller mobile call method setTicketTripStart ------------->");
		return ticketTripService.saveTrip(ticketTripStartRequestVO);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mTicketTripContinue", method = RequestMethod.POST)	
	public TicketTripVOWrapper setTicketTripContinue(@RequestBody TicketTripStartRequestVO ticketTripStartRequestVO) {
		System.out.println("-------------------> In the Ticket Controller mobile call method setTicketTripContinue ------------->");
		return ticketTripService.continueTrip(ticketTripStartRequestVO);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mTrackTrip", method = RequestMethod.POST)
	public TicketTripNavigationWrapper trackTrip(@RequestBody TicketTripNavigationRequestVO ticketTripNavigationRequestVO) {
		System.out.println("-------------------> In the Ticket Controller mobile call method trackTrip ------------->");
		return ticketTripNavigationService.saveTripNavigation(ticketTripNavigationRequestVO);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mTicketEndTrip", method = RequestMethod.POST)
	public TicketProgressWrapper stopTrip(@RequestBody TicketTripNavigationRequestVO ticketTripNavigationRequestVO) {
		System.out.println("-------------------> In the Ticket Controller mobile call method stopTrip ------------->");
		return ticketTripNavigationService.endTripNavigation(ticketTripNavigationRequestVO);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mTicketUploadPhoto", method = RequestMethod.POST)
	public TicketProgressWrapper uploadPhoto(@RequestParam("uploadingFile") MultipartFile uploadingFile, @RequestParam("ticket") Integer ticket, 
			 @RequestParam("userId") Integer userId, Double latitude, Double longitude) {
		System.out.println("-------------------> In the Ticket Controller mobile call method uploadPhoto ------------->");
		return this.uploadingFiles(uploadingFile, ticket, true, userId, latitude, longitude);		
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mTicketUploadSign", method = RequestMethod.POST)
	public TicketProgressWrapper uploadSignature(@RequestParam("uploadingFile") MultipartFile uploadingFile, @RequestParam("ticket") Integer ticket, 
			 @RequestParam("userId") Integer userId, Double latitude, Double longitude) {
		System.out.println("-------------------> In the Ticket Controller mobile call method uploadSignature ------------->");
		return this.uploadingFiles(uploadingFile, ticket, false, userId,  latitude, longitude);		
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mTicketCompleted", method = RequestMethod.POST)
	public TicketProgressWrapper completeTask(@RequestBody TicketUpdateRequestVO ticketUpdateRequestVO) {
		System.out.println("-------------------> In the Ticket Controller mobile call method completeTask ------------->");
		return ticketService.completeTask(ticketUpdateRequestVO);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mTicketClose", method = RequestMethod.POST)
	public TicketProgressWrapper closeTask(@RequestBody TicketUpdateRequestVO ticketUpdateRequestVO) {
		System.out.println("-------------------> In the Ticket Controller mobile call method closeTask ------------->");
		return ticketService.closeTask(ticketUpdateRequestVO);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mTicketCloseList/{userId}", method = RequestMethod.GET)
	public List<TicketDetailInProgressVO> getTicketsForDeptHead(@PathVariable Integer userId) {
		System.out.println("-------------------> In the Ticket Controller mobile call method getTicketsForDeptHead------------->");
		return ticketService.getCompletedTicketListForCoordinator(userId);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mTripsCostList/{dpmId}", method = RequestMethod.GET)
	public TicketTripsReimbursementWrapper getReimbursementForDPM(@PathVariable Integer dpmId) {
		System.out.println("-------------------> In the Ticket Controller mobile call method getReimbursementForDPM ------------->");
		return ticketService.getTripCostsForDPM(dpmId);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/tripCosts/{dpmId}", method = RequestMethod.GET)
	public TicketTripCostWrapper getTripCosts(@PathVariable Integer dpmId) {
		System.out.println("-------------------> In the Ticket Controller method getTripCosts ------------->");
		return ticketService.getReimbursementForDPM(dpmId);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/getAllTrips/20191129", method = RequestMethod.GET)
	public List<TicketTripStatsVO> getAllTripStatistics() {
		System.out.println("-------------------> In the Ticket Controller method getAllTripStatistics ------------->");
		return ticketService.getTicketTripStatistics();
	}
	
	@CrossOrigin
	@RequestMapping(value = "/getTripsBwDates", method = RequestMethod.POST)
	public List<TicketTripStatsVO> getTripsBetweenDates(@RequestBody SearchDatesVO search) {
		System.out.println("-------------------> In the Ticket Controller method getTripsBetweenDates ------------->");
		return ticketService.getTripsByDateRange(search);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mSelfTicketRequest", method = RequestMethod.POST)
	public MobileResponseVO raiseSelfTicket(@RequestBody SelfTicketRequestVO selfTicketRequestVO) {
		System.out.println("-------------------> In the Ticket Controller mobile call method closeTask ------------->");
		return ticketService.createSelfTicket(selfTicketRequestVO);
	}
	
	@CrossOrigin
	@GetMapping("/generateReports/{month}/{year}/{dpmId}")
	public List<TicketTripStatsVO> generateMonthlyReports(@PathVariable String month, @PathVariable String year, @PathVariable Integer dpmId) {
		return ticketService.getTripsForDPMWithDateRange(month, year, dpmId, false);
		
	}
	
	@CrossOrigin
	@GetMapping("/generateReports/{month}/{year}")
	public String generatePDFReports(@PathVariable String month, @PathVariable String year) {
		return ticketService.generateTripReportsForAllDPMsForMonth(month, year, false);		
	}
	
	
	private TicketProgressWrapper uploadingFiles(MultipartFile uploadingFile,  Integer ticket, Boolean isPhoto, Integer userId, Double latitude, Double longitude) {

		TicketDetailInProgressVO vo = ticketService.getInProgressTicketDetails(ticket);
		if(null ==  vo || null == vo.getTicket()) {
			LOGGER.warn("Ticket is an invalid ticket : "+ ticket);
			return this.getErrorTicketProgressWrapper("Invalid Ticket.", ticket, userId);
			
		} else {
			String folderName = "";
			String url = com.irctn.config.ApplicationConfig.SERVER;
			
			if(uploadingFile.getOriginalFilename().endsWith(".jpg")) {
				folderName = folderName +  File.separatorChar + "images" + File.separatorChar + ticket; 
				url = url + "/" + "images" + "/" + ticket;
			} 
			else {
				return this.getErrorTicketProgressWrapper("Invalid File Format. Only JPG files are allowed.", ticket, userId);
			}
			
			//String uploadfileName = uploadingFile.getOriginalFilename().replace(".jpg", ".jpeg");
			String latitudeString = ("" + latitude).replace(".", "p");
			String longitudeString = ("" + longitude).replace(".", "p");
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MMM-dd_HH-mm-ss");			
			String uploadfileName = latitudeString+"_"+longitudeString +"_"+ sdf.format(new Date())+".jpeg"; 
			System.out.println("-------------> Upload File name is --->" + uploadfileName);			
			
			File uploadingdirs = new File(com.irctn.config.ApplicationConfig.UPLOAD_FOLDER + folderName);
			uploadingdirs.mkdirs();
			
			File file = new File(uploadingdirs + "" + File.separatorChar + uploadfileName);
			url = url + "/" +  uploadfileName;
			
			boolean isUploadSuccess = true;
			try {
				uploadingFile.transferTo(file);
			} catch (IllegalStateException | IOException e) {
				isUploadSuccess = false;
				e.printStackTrace();
				return this.getErrorTicketProgressWrapper("Server Error. Cannot upload file. Contact Admin.", ticket, userId);
			}
			
			if(isUploadSuccess) {
				//ticketService call
				return ticketService.uploadFile(ticket, file, url, isPhoto);
			} else {
				return this.getErrorTicketProgressWrapper("Server Error. Contact Admin.", ticket, userId);
			}
		}
	}	
	
	@CrossOrigin
	@RequestMapping(value = "/images/{ticket}/{fileName:.+}", method = RequestMethod.GET)
	//public ResponseEntity<byte[]> getFile(@PathVariable String email, @PathVariable String section, @PathVariable String fileName) {
	public ResponseEntity<byte[]> getFile(@PathVariable String ticket, @PathVariable String fileName) {
//		LOGGER.debug("In FileServicesController.getFile..........................");
		System.out.println(fileName);
		//email = email.replace("@", "_");
		//StringBuilder builder = new StringBuilder().append(com.irctn.config.ApplicationConfig.UPLOAD_FOLDER).append(email).append(File.separatorChar)
		//		.append(section).append(File.separatorChar).append("images").append(File.separatorChar).append(fileName);

		StringBuilder builder = new StringBuilder().append(com.irctn.config.ApplicationConfig.UPLOAD_FOLDER)
									.append(File.separatorChar).append("images")
									.append(File.separatorChar).append(ticket)
									.append(File.separatorChar).append(fileName);
				
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile(builder.toString(), "r");

		} catch (FileNotFoundException e1) {
			LOGGER.error(builder.toString() + " File not Found Exception  " + e1.getMessage());
		}

		byte[] b = null;
		try {
			b = new byte[(int) file.length()];
			file.readFully(b);
		} catch (final IOException e) {
			LOGGER.error(builder.toString() + " File cant be read Exception  " + e.getMessage());
		}

		if (null != file)
			try { 
				file.close();
			} catch (IOException e) {
				LOGGER.warn(builder.toString() + " File cant be closed Exception  " + e.getMessage());
			}

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		headers.setAccessControlAllowOrigin("*");

		return new ResponseEntity<byte[]>(b, headers, HttpStatus.CREATED);

	}
	
	private TicketProgressWrapper getErrorTicketProgressWrapper(String errorMessage, Integer ticket, Integer userId) {
		if(null == errorMessage || null == ticket || null == userId) {
			return null;
		}
		TicketProgressWrapper wrapper = new TicketProgressWrapper();
		String name = userService.getNameById(userId);
		
		if(null != name) {
			wrapper.setFirstName(name);
		}
		UserVO userVO = userService.getUserById(userId);
		if(null != userVO ) {
			wrapper.setEmail(userVO.getEmail());
		}
		wrapper.setErrorMessage(errorMessage);		
		wrapper.setStatus(AppConstants.FAILURE);
		wrapper.setTicketDetail(null);
		return wrapper;
	}
	
	
}
