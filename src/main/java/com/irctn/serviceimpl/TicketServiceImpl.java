package com.irctn.serviceimpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.irctn.model.SelfTicket;
import com.irctn.model.Ticket;
import com.irctn.model.TicketHistory;
import com.irctn.repository.CentreDepartmentRepository;
import com.irctn.repository.SelfTicketRepository;
import com.irctn.repository.TicketRepository;
import com.irctn.service.CentreService;
import com.irctn.service.DepartmentService;
import com.irctn.service.MobileService;
import com.irctn.service.SchedulerTasksService;
import com.irctn.service.TicketHistoryService;
import com.irctn.service.TicketService;
import com.irctn.service.TicketTripNavigationService;
import com.irctn.service.TicketTripService;
import com.irctn.service.UserService;
import com.irctn.service.ZoneService;
import com.irctn.util.AppConstants;
import com.irctn.vo.CentreVO;
import com.irctn.vo.DepartmentVO;
import com.irctn.vo.MessageVO;
import com.irctn.vo.RaiseTicketVO;
import com.irctn.vo.TicketDPMRankingVO;
import com.irctn.vo.TicketDetailInProgressVO;
import com.irctn.vo.TicketDetailVO;
import com.irctn.vo.TicketHistogramVO;
import com.irctn.vo.TicketTripStatsVO;
import com.irctn.vo.TicketTripVO;
import com.irctn.vo.TicketVO;
import com.irctn.vo.TripHistogramVO;
import com.irctn.vo.UserVO;
import com.irctn.vo.mobile.DoNotDisturbVO;
import com.irctn.vo.mobile.MobileResponseVO;
import com.irctn.vo.mobile.TicketProgressListWrapper;
import com.irctn.vo.mobile.TicketProgressWrapper;
import com.irctn.vo.mobile.TicketTripCostWrapper;
import com.irctn.vo.mobile.TicketTripNavigationWrapper;
import com.irctn.vo.mobile.TicketTripsReimbursementWrapper;
import com.irctn.vo.mobile.TicketVOWrapper;
import com.irctn.vo.mobile.TripCostVO;
import com.irctn.vo.mobile.TripDateCostVO;
import com.irctn.vo.mobile.TripDateVO;
import com.irctn.vo.mobile.request.SearchDatesVO;
import com.irctn.vo.mobile.request.SelfTicketRequestVO;
import com.irctn.vo.mobile.request.TicketUpdateRequestVO;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class TicketServiceImpl implements TicketService {

	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.serviceimpl.TicketServiceImpl");
			
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	CentreService centreService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	DepartmentService departmentService;
	
	@Autowired
	TicketRepository ticketRepository;
	
	@Autowired
	TicketTripNavigationService ticketTripNavigationService;
	
	@Autowired
	ZoneService zoneService;
	
	@Autowired
	TicketHistoryService ticketHistoryService;
	
	@Autowired
	TicketTripService ticketTripService;
	
	@Autowired
	MobileService mobileService;	
	
	@Autowired
	SelfTicketRepository selfTicketRepository;
	
	@Autowired
	CentreDepartmentRepository centreDepartmentRepository;
	
	@Autowired
	JavaMailSender javaMailService;
	
	@Autowired
	SchedulerTasksService schedulerTasksService;
	
	
	@Override
	public List<TicketVO> getTicketsForDPM(Integer dpmId) {
		if(null == dpmId) return null;
		List<Ticket> tickets = ticketRepository.findByDpmid(dpmId);
		return getVOListFromModelList(tickets);
	}

	private List<TicketVO> getVOListFromModelList(List<Ticket> tickets) {
		if(null == tickets || tickets.isEmpty()) return null;
		List<TicketVO> ticketList = new ArrayList<TicketVO>();
		TicketVO vo = null;
		for(Ticket ticket : tickets) {
			if(null != (vo = getVOFromModel(ticket))) ticketList.add(vo);
		}
		return ticketList;
	}

	private TicketVO getVOFromModel(Ticket ticket) {
		if(null == ticket) return null;
		TicketVO vo = new TicketVO();
		vo.setTicketId(ticket.getTicketid());
		vo.setTicket(ticket.getTicket());
		if(AppConstants.STATUS_ACTIVE.equals( ticket.getSelfticket()) ) {
			vo.setSelfTicket(true);
			vo.setSelfTicketId(ticket.getSelfticketid());
			if(null != ticket.getSelfticketid()) {
				SelfTicket self = this.getSelfTicket(ticket.getSelfticketid());
				vo.setCentreName(self.getCentre());
			}
		} else {
			vo.setSelfTicket(false);
			vo.setCentreId(ticket.getCentreid());
			vo.setDepartmentId(ticket.getDepartmentid());			
			vo.setCoordinatorId(ticket.getCoordinatorid());
			vo.setCoordinatorcontact(ticket.getCoordinatorcontact());
			
			vo.setSignature(ticket.getSignature());
			CentreVO centreVO = centreService.getCentreById(ticket.getCentreid());
			if(null != centreVO) {
				vo.setCentreName(centreVO.getCentreName());
				vo.setZoneId(centreVO.getZoneId());
				vo.setZoneName(centreVO.getZone());
			}
			
			DepartmentVO departmentVO = departmentService.getDepartmentById(ticket.getDepartmentid());
			if(null != departmentVO) {
				vo.setDepartmentName(departmentVO.getName());
			}
			
			UserVO userVO = userService.getUserById(ticket.getCoordinatorid());
			if(null != userVO) {
				vo.setCoordinator(userVO.getFirstName() + " " + userVO.getLastName());
			}
		}
		
		if(null != ticket.getPhoto()) {
			vo.setPhoto(ticket.getPhoto());
		} else {
			vo.setPhoto("");
		}
		vo.setDescription(ticket.getDescription());
		vo.setStatus(ticket.getStatus());
		vo.setDpmId(ticket.getDpmid());
		vo.setStartdatetime(ticket.getStartdatetime());
		vo.setEnddatetime(ticket.getEnddatetime());
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");  
		Date appointment = ticket.getAppointment();
		vo.setAppointment(appointment);
		
		String hours = formatter.format(appointment);
		//LOGGER.debug("------------> appointment is :" + hours);
		//System.out.println("------------> appointment is :" + hours);
		
		hours = hours.substring(10,13);
		
		//System.out.println("------------> after substring appointment is :" + hours);
		vo.setTimeSlot(hours);
		
		if(null != ticket.getDpmid()) {
			UserVO userVO = userService.getUserById(ticket.getDpmid());
			if(null != userVO) { 
				vo.setDpm(userVO.getFirstName() + " " + userVO.getLastName());
			}
		}
		
		return vo;
	}

	@Override
	public TicketProgressListWrapper getTicketsForDPMWrapped(Integer dpmId) {
		if(null == dpmId) return null;
		//List<TicketVO> list = this.getTicketListForDPM(dpmId);
		List<String> filterStatus = new ArrayList<String>();
		filterStatus.add(AppConstants.TICKET_STATUS_ASSIGNED);
		filterStatus.add(AppConstants.TICKET_STATUS_ACCEPTED);
		filterStatus.add(AppConstants.TICKET_STATUS_INPROGRESS);
		filterStatus.add(AppConstants.TICKET_STATUS_TRIPSTARTED);
		filterStatus.add(AppConstants.TICKET_STATUS_TRIPCOMPLETED);
		
		//List<TicketVO> list = this.getVOListFromModelList( ticketRepository.findByDpmidAndStatusInOrderByTicketidDesc(dpmId, filterStatus) );
		//List<Ticket> tickets = ticketRepository.findByDpmid(dpmId);
		List<Ticket> tickets =  ticketRepository.findByDpmidAndStatusInOrderByTicketidDesc(dpmId, filterStatus);
		//if(null == list) {
		if(null == tickets || tickets.isEmpty()) {
			return null;
		}
		
		boolean hasTickets = false;
		/*
		List<ShortTicketVO> shortTickets = new ArrayList<ShortTicketVO>();
		for(TicketVO ticketVO : list) {
			ShortTicketVO shortTicket = new ShortTicketVO();
			shortTicket.setId(ticketVO.getTicket());
			shortTicket.setCentre(ticketVO.getCentreName());
			shortTicket.setZone(ticketVO.getZoneName());
			shortTicket.setStatus(ticketVO.getStatus());
			shortTickets.add(shortTicket);
			hasTickets = true;
		}
		*/
		
		TicketVO currentTicket = this.getCurrentTicket(dpmId);
		List<TicketDetailInProgressVO> ticketInProgressList = new ArrayList<TicketDetailInProgressVO>();
		for(Ticket ticket : tickets) {
			if(null != ticket.getTicket()) {
				if(null == currentTicket || (null != currentTicket.getTicket() && currentTicket.getTicket().equals(ticket.getTicket()))	) {				
					ticketInProgressList.add( this.getInProgressTicketDetails(ticket.getTicket(), true));				
				}else {
					ticketInProgressList.add(this.getInProgressTicketDetails(ticket.getTicket(), false));
				}				
				hasTickets = true;				
			}			
		}
		
		//TicketVOListWrapper ticketWrapper = new TicketVOListWrapper();
		TicketProgressListWrapper wrapper = new TicketProgressListWrapper();
		if(hasTickets) {
			wrapper.setStatus(AppConstants.SUCCESS);
			wrapper.setTickets(ticketInProgressList);
			//ticketWrapper.setTickets(shortTickets);
		} else {
			wrapper.setStatus(AppConstants.FAILURE);
		}
		
		if(null != dpmId) {
			UserVO userVO = userService.getUserById(dpmId);
			if(null != userVO) {
				wrapper.setFirstName(userVO.getFirstName() + " " + userVO.getLastName());
				wrapper.setEmail(userVO.getEmail());
			}
		}
		return wrapper;
	}

	@Override
	public TicketVOWrapper getTicket(Integer ticket) {
		TicketVO vo = getVOFromModel(ticketRepository.findByTicket(ticket));
		TicketDetailVO detailVO = getTicketDetailFromVO(vo);
		TicketVOWrapper wrapper = new TicketVOWrapper();
		if(null == vo) {
			wrapper.setStatus(AppConstants.FAILURE);
			wrapper.setTicketDetail(null);
		} else {
			wrapper.setStatus(AppConstants.SUCCESS);
			wrapper.setTicketDetail(detailVO);
			if(null != vo.getDpmId()) {
				UserVO userVO = userService.getUserById(vo.getDpmId());
				if(null != userVO) {
					wrapper.setFirstName(userVO.getFirstName() + " " + userVO.getLastName());
					wrapper.setEmail(userVO.getEmail());
				}
			}	
		}
			
		return wrapper;
	}

	private TicketDetailVO getTicketDetailFromVO(TicketVO vo) {
		if(null == vo) return null;
		TicketDetailVO detailVO = new TicketDetailVO();
		detailVO.setTicket(vo.getTicket());
		detailVO.setCentre(vo.getCentreName());
		detailVO.setZone(vo.getZoneName());
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		detailVO.setDate(formatter.format(vo.getAppointment()));
		
		formatter = new SimpleDateFormat("hh:mm a");
		detailVO.setTime(formatter.format(vo.getAppointment()));
		
		if(vo.getSelfTicket()) {
			if(null != vo.getSelfTicketId()) {
				SelfTicket self =this.getSelfTicket(vo.getSelfTicketId());
				detailVO.setAddress(self.getAddress());
				detailVO.setLatitude(""+self.getLatitude());
				detailVO.setLongitude(""+self.getLongitude());
			}
			detailVO.setCoordinator("");
			detailVO.setContact("");
			
		} else {
			CentreVO centreVO = centreService.getCentreById(vo.getCentreId());
			if(null != centreVO) {
				detailVO.setAddress(centreVO.getAddress());
				detailVO.setLatitude(""+centreVO.getLatitude());
				detailVO.setLongitude(""+centreVO.getLongitude());
			}
			
			detailVO.setCoordinator(vo.getCoordinator());
			detailVO.setContact(vo.getCoordinatorcontact());
		}
		
		detailVO.setDescription(vo.getDescription());		
		detailVO.setStatus(vo.getStatus());
		return detailVO;
	}

	@Override
	public TicketVOWrapper acceptTicket(TicketUpdateRequestVO ticketUpdateRequestVO, boolean isAccepted) {
		if(null == ticketUpdateRequestVO) return null;
		Integer ticketNumber = ticketUpdateRequestVO.getTicket();
		Integer requestUserId = ticketUpdateRequestVO.getUserId();
		if(null == ticketNumber || null == requestUserId) {
			return null;
		} 
		
		String errorMessage = null;
		//validations		
		Ticket ticket = ticketRepository.findByTicket(ticketNumber);
		if(!requestUserId.equals(ticket.getDpmid())) {
			//This not the dpm, not allowed
			errorMessage = "Access Denied.";
		}
		else if(!AppConstants.TICKET_STATUS_ASSIGNED.equalsIgnoreCase(ticket.getStatus())) { //TODO: This should be Assigned
			errorMessage = "Cannot update the ticket status.";
		}
		else {
			if(isAccepted) {
				ticket.setStatus("Accepted");
			} else {
				ticket.setStatus("Rejected");
			}
			
			Ticket savedTicket = ticketRepository.save(ticket);
			updateRedisTicket(savedTicket);
			
			//save the history manually
			logHistory(savedTicket, ticketUpdateRequestVO.getComments());		
			
		}
		if(null != errorMessage) {
			return getErrorTicket(errorMessage, ticketUpdateRequestVO);
		}
		TicketVOWrapper wrapper = this.getTicket(ticketNumber);
		return wrapper;
	}
	
	private void updateRedisTicket(Ticket savedTicket) {
		redisTemplate.opsForHash().delete("TICKET", savedTicket.getTicket());
		redisTemplate.opsForHash().put("TICKET", savedTicket.getTicket(), this.getVOFromModel(savedTicket)); //TODO: This causes problem when ticket is accepted and trip started
		redisTemplate.opsForHash().delete("TICKET", "ALL_TICKETS");
		
	}

	@Override
	public String updateTicketStatus(Integer ticket, String status, String comments, Integer userId) {
		if(null == ticket ||null == status) {
			return AppConstants.FAILURE;
		}
		Ticket model = ticketRepository.findByTicket(ticket);
		if(AppConstants.TICKET_STATUS_INPROGRESS.equalsIgnoreCase(status)) {
			ticketTripService.stopTrip(ticket);
			/*
			if(null != model && AppConstants.STATUS_ACTIVE.equals(model.getSelfticket()) ) {
				status = AppConstants.TICKET_STATUS_COMPLETED;				
				model.setStartdatetime(new Date());
				model.setEnddatetime(new Date());
			} else {
				model.setStartdatetime(new Date());
			}*/
			model.setStartdatetime(new Date());
		} else if(AppConstants.TICKET_STATUS_COMPLETED.equalsIgnoreCase(status)) {
			if( !AppConstants.TICKET_STATUS_INPROGRESS.equalsIgnoreCase(model.getStatus()) ) {
				return "Could not mark the ticket as completed. Please contact Admin";
			} else if(null == model.getSignature() && !AppConstants.STATUS_ACTIVE.equals(model.getSelfticket())) {
				return "Update Failure. Please get the task completion acknowledgement signature from Coordinator.";
			} else if(null == model.getPhoto()) {
				return "Update Failure. Please upload a photo of the task you have completed.";
			}
			model.setEnddatetime(new Date());
		} else if(AppConstants.TICKET_STATUS_CLOSED.equalsIgnoreCase(status) ) {
			if( !AppConstants.TICKET_STATUS_COMPLETED.equalsIgnoreCase(model.getStatus())) {
				return "Could not mark the ticket as Closed. Please contact Admin";
			} else if(! model.getCoordinatorid().equals(userId)) {
				return "Could not mark the ticket as Closed. Only the corordinator can close this ticket.";
			}
		}
		
		model.setStatus(status);
		Ticket savedTicket = ticketRepository.save(model);
		this.updateRedisTicket(savedTicket);
		//save the history manually
		logHistory(savedTicket, comments);
		return AppConstants.SUCCESS;
		
	}


	private void logHistory(Ticket savedTicket, String comments) {
		TicketHistory history = new TicketHistory();
		history.setTicket(savedTicket.getTicket());
		history.setStatus(savedTicket.getStatus());
		history.setDpm(savedTicket.getDpmid());
		history.setLogdate(new Date());
		if(null != comments) { 
			history.setComments(comments);
		}
		ticketHistoryService.saveTicketHistory(history);		
	}

	private TicketVOWrapper getErrorTicket(String errorMessage, TicketUpdateRequestVO ticketUpdateRequestVO) {
		if(null == errorMessage || null == ticketUpdateRequestVO) {
			return null;
		}
		TicketVOWrapper wrapper = new TicketVOWrapper();
		String name = userService.getNameById(ticketUpdateRequestVO.getUserId());
		if(null != name) {
			wrapper.setFirstName(name);
		}
		wrapper.setErrorMessage(errorMessage);
		wrapper.setEmail(ticketUpdateRequestVO.getEmail());
		wrapper.setStatus(AppConstants.FAILURE);
		wrapper.setTicketDetail(null);
		return wrapper;
	}

	@Override
	public TicketVO getTicketByTicketNumber(Integer ticketNumber) {
		if(null == ticketNumber) return null;
		
		Object object = redisTemplate.opsForHash().get("TICKET", ticketNumber);
		if(null != object) {
			System.out.println("-------------->got the ticket from Redis "+ ticketNumber + " <-----------------------");
			return (TicketVO) object;
		}
		
		TicketVO ticketVO = getVOFromModel(ticketRepository.findByTicket(ticketNumber));
		if(null != ticketVO) {
			System.out.println("-------------->got the ticket from DAtabase and now it is in Redis "+ ticketNumber + " <-----------------------");
			redisTemplate.opsForHash().put("TICKET", ticketNumber, ticketVO);
		}
		return ticketVO;
	}


	@Override
	public TicketDetailInProgressVO getInProgressTicketDetails(Integer ticketNumber, boolean isCurrentTicket) {
		
		TicketVO ticket = getTicketByTicketNumber(ticketNumber);
		if(null == ticket) return null;
		
		TicketDetailInProgressVO vo = new TicketDetailInProgressVO();
		vo.setTicket(ticket.getTicket());
		vo.setDescription(ticket.getDescription());
		vo.setStatus(ticket.getStatus());
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");  
		Date appointment = ticket.getAppointment();
		vo.setDate(formatter.format(appointment) );
		
		String hours = formatter.format(appointment);
		hours = hours.substring(10,13);		
		vo.setTime(hours);		
		vo.setCentre(ticket.getCentreName());
		vo.setSelfTicket(ticket.getSelfTicket());
		
		Ticket model = ticketRepository.findByTicket(ticketNumber);
		if(null != model.getStartdatetime()) {
			vo.setStartTime(formatter.format(model.getStartdatetime()));
		}
		
		if(vo.getSelfTicket()) {
			vo.setZone("");
			vo.setCoordinator("");
			vo.setContact("");			
			vo.setCoordinatorSignature("");
			if(null != model.getSelfticketid()) {
				SelfTicket self = this.getSelfTicket(model.getSelfticketid());
				if(null != self) {
					vo.setLatitude(""+self.getLatitude());
					vo.setLongitude(""+self.getLongitude());
					vo.setAddress(self.getAddress());
				}
			}
		} else {
			vo.setZone(ticket.getZoneName());
			vo.setCoordinator(ticket.getCoordinator());
			vo.setContact(ticket.getCoordinatorcontact());
			if(null == ticket.getSignature()) {
				vo.setCoordinatorSignature("");
			} else {
				vo.setCoordinatorSignature(ticket.getSignature());
			}
			Integer centreId = ticket.getCentreId();
			if(null!= centreId) {
				CentreVO centreVO = centreService.getCentreById(centreId);			
				if(null != centreVO) {
					vo.setLatitude(""+centreVO.getLatitude());
					vo.setLongitude(""+centreVO.getLongitude());
					vo.setAddress(centreVO.getAddress());
				}
			}
		}
		
		if(null == ticket.getPhoto()) {
			vo.setPhoto("");
		} else {
			vo.setPhoto(ticket.getPhoto());
		}
		vo.setEnabled(isCurrentTicket); 	//This is to enable a ticket from the List of tickets
		return vo;
		
	}

	@Override
	public TicketProgressWrapper uploadFile(Integer ticket, File file, String url, Boolean isPhoto) {
		if(null == ticket || null == file || null == url || null == isPhoto) return null;
		Ticket model = ticketRepository.findByTicket(ticket);
		if(isPhoto) {
			model.setPhoto(url);
		} else {
			model.setSignature(url);
		}
		Ticket savedTicket = ticketRepository.save(model);
		this.updateRedisTicket(savedTicket);
		
		TicketDetailInProgressVO updatedTicket = this.getInProgressTicketDetails(ticket);
		TicketProgressWrapper wrapper = new TicketProgressWrapper();
		UserVO dpm = userService.getUserById(model.getDpmid());
		wrapper.setEmail(dpm.getEmail());
		wrapper.setFirstName(dpm.getFirstName() + " "  +  dpm.getLastName());
		wrapper.setStatus(AppConstants.SUCCESS);
		wrapper.setTicketDetail(updatedTicket);
		return wrapper;
	}

	@Override
	public TicketProgressWrapper completeTask(TicketUpdateRequestVO ticketUpdateRequestVO) {
		return this.makeTaskComplete(ticketUpdateRequestVO,AppConstants.TICKET_STATUS_COMPLETED, "Ticket Task Completed by DPM.");
	}

	@Override
	public TicketProgressWrapper closeTask(TicketUpdateRequestVO ticketUpdateRequestVO) {
		return this.makeTaskComplete(ticketUpdateRequestVO,AppConstants.TICKET_STATUS_CLOSED , "Ticket Task Closed.");
	}
	
	private TicketProgressWrapper makeTaskComplete(TicketUpdateRequestVO ticketUpdateRequestVO , String statusToBeUpdated, String comments) {
		if(null == ticketUpdateRequestVO || null == ticketUpdateRequestVO.getTicket() || null == ticketUpdateRequestVO.getUserId())	return null;
		Ticket model = ticketRepository.findByTicket(ticketUpdateRequestVO.getTicket());
		if(null == model) return null;
		
		String updateStatus = this.updateTicketStatus(ticketUpdateRequestVO.getTicket(), statusToBeUpdated, comments,ticketUpdateRequestVO.getUserId() );
		
		TicketProgressWrapper wrapper = new TicketProgressWrapper();
		wrapper.setTicketDetail(this.getInProgressTicketDetails(model.getTicket()));
		wrapper.setStatus(updateStatus);
		wrapper.setEmail(ticketUpdateRequestVO.getComments());
		UserVO dpm = userService.getUserById(model.getDpmid());
		wrapper.setFirstName(dpm.getFirstName() + " "  +  dpm.getLastName());
		return wrapper;
		
	}

	@Override
	public TicketProgressWrapper getCurrentTicketForDPM(Integer dpmId) {
		LOGGER.debug("In TicketServiceImpl getCurrentTicketForDPM for " + dpmId);
		if(null == dpmId) return null;
		TicketVO ticketVO = getCurrentTicket(dpmId);
		if(null == ticketVO) {
			return null;
		}
		//TicketDetailInProgressVO updatedTicket = this.getInProgressTicketDetails(ticketVO.getTicket()); //TODO: true
		TicketDetailInProgressVO updatedTicket = this.getInProgressTicketDetails(ticketVO.getTicket(),true); //TODO: true
		TicketProgressWrapper wrapper = new TicketProgressWrapper();
		UserVO dpm = userService.getUserById(dpmId);
		wrapper.setEmail(dpm.getEmail());
		wrapper.setFirstName(dpm.getFirstName() + " "  +  dpm.getLastName());
		wrapper.setStatus(AppConstants.SUCCESS);
		wrapper.setTicketDetail(updatedTicket);
		return wrapper;
	}

	private TicketVO getCurrentTicket(Integer dpmId) {
		List<TicketVO> list = this.getTicketsForDPM(dpmId);
		if(null!= list) {
			for(TicketVO ticketVO : list) {
				if(null != ticketVO.getStatus() && ( AppConstants.TICKET_STATUS_INPROGRESS.equalsIgnoreCase(ticketVO.getStatus()) 
													|| AppConstants.TICKET_STATUS_TRIPSTARTED.equalsIgnoreCase(ticketVO.getStatus())
													|| AppConstants.TICKET_STATUS_TRIPCOMPLETED.equalsIgnoreCase(ticketVO.getStatus())
													|| AppConstants.TICKET_STATUS_ACCEPTED.equalsIgnoreCase(ticketVO.getStatus())
													)
													
						) {
					
					return ticketVO;
				}
			}
		}
		return null;		
	}

	@Override
	public List<TicketVO> getTicketListForDPM(Integer dpmId) {
		if(null == dpmId) return null;
		List<TicketVO> list = this.getTicketsForDPM(dpmId);
		if(null != list) {
			List<TicketVO> result = list.stream()
					.filter(ticketVO -> AppConstants.TICKET_STATUS_ACCEPTED.equalsIgnoreCase(ticketVO.getStatus())
										|| AppConstants.TICKET_STATUS_ASSIGNED.equalsIgnoreCase(ticketVO.getStatus())
										|| AppConstants.TICKET_STATUS_INPROGRESS.equalsIgnoreCase(ticketVO.getStatus()) 
										|| AppConstants.TICKET_STATUS_TRIPSTARTED.equalsIgnoreCase(ticketVO.getStatus()) 
										|| AppConstants.TICKET_STATUS_TRIPCOMPLETED.equalsIgnoreCase(ticketVO.getStatus()) )
					.collect(Collectors.toList());
			return result;
		} else {
			return null;
		}
	}

	@Override
	public List<TicketVO> getClosedTicketListForDPM(Integer dpmId) {
		if(null == dpmId) return null;
		List<TicketVO> list = this.getTicketsForDPM(dpmId);
		if(null != list) {
			List<TicketVO> result = list.stream()
					.filter(ticketVO -> AppConstants.TICKET_STATUS_CLOSED.equalsIgnoreCase(ticketVO.getStatus()) )
					.collect(Collectors.toList());
			return result;
		}		
		return null;
	}

	@Override
	public List<TicketVO> getCompletedTicketListForDPM(Integer dpmId) {
		if(null == dpmId) return null;
		List<TicketVO> list = this.getTicketsForDPM(dpmId);
		if(null!= list) {
			List<TicketVO> result = list.stream()
					.filter(ticketVO -> AppConstants.TICKET_STATUS_COMPLETED.equalsIgnoreCase(ticketVO.getStatus()) )
					.collect(Collectors.toList());
			return result;
		}
		return null;
	}

	@Override
	public List<TicketDetailInProgressVO> getCompletedTicketListForCoordinator(Integer coordinatorId) {
		List<Ticket> tickets = ticketRepository.findByCoordinatoridAndStatus(coordinatorId, AppConstants.TICKET_STATUS_COMPLETED);
		if(null != tickets && !tickets.isEmpty()) {
			List<TicketVO> list = getVOListFromModelList(tickets);
			if(null != list && !list.isEmpty()) {
				List<TicketDetailInProgressVO> completedList = new ArrayList<TicketDetailInProgressVO>();
				for(TicketVO ticket : list ) {
					completedList.add(this.getInProgressTicketDetails(ticket.getTicket()) );
				}
				return completedList;
			}
		}
		return null;
	}

	@Override
	public synchronized MessageVO raiseTicket(RaiseTicketVO raiseTicketVO, boolean isSelfTicket) {
		LOGGER.debug("----> Create a New Ticket in Ticket Service Implm . raiseTicket() -------->" + isSelfTicket);
		String mode = AppConstants.MODE_ADD;
		if(isSelfTicket) {
			System.out.println("----> Create a New Self Ticket in Ticket Service Implm . raiseTicket() -------->" + isSelfTicket);
		} else {
			if( null == raiseTicketVO || null == raiseTicketVO.getCentreId() || null == raiseTicketVO.getDepartmentId()
					|| null == raiseTicketVO.getCoordinatorId()) 
				return new MessageVO(AppConstants.FAILURE, "Missing Mandatory Inputs. Ticket cannot be created.");
			
			if(null != raiseTicketVO.getTicket()) {
				LOGGER.debug("----> Ticket to be modified in save ticket () is . -------->" + raiseTicketVO.getTicket());
				System.out.println("------> Ticket to be modified is : -------->" + raiseTicketVO.getTicket() + " with as : " + raiseTicketVO.getTicketStatus());
				if(AppConstants.TICKET_STATUS_ASSIGNED.equalsIgnoreCase(raiseTicketVO.getTicketStatus()) 
						|| AppConstants.TICKET_STATUS_NEW.equalsIgnoreCase(raiseTicketVO.getTicketStatus()) 
						|| AppConstants.TICKET_STATUS_REJECTED.equalsIgnoreCase(raiseTicketVO.getTicketStatus())) {
					mode = AppConstants.MODE_UPDATE;
				} else {
					return new MessageVO(AppConstants.FAILURE, "This Ticket cannot be modified by the Admin. Status is :" + raiseTicketVO.getTicketStatus());
				}
			}
			
		}
		
		Ticket model = new Ticket();
		Date appointment = raiseTicketVO.getDateofappointment();
		String hourString = raiseTicketVO.getHourofappointment();
		if(null != appointment && null != hourString) {
			int hours = 0;
			int minutes = 0;
			
			try {
				hours = 8;	
				if(isSelfTicket) {
					if(!hourString.isEmpty() && hourString.contains(":")) {
						String[] splits = hourString.split(":");
						hours = Integer.parseInt(splits[0]);
						minutes = Integer.parseInt(splits[1]);
					}
				} else {
					if(hourString.contains("AM") || hourString.contains("PM")) {
						String[] splits = hourString.split(" ");
						hours = Integer.parseInt(splits[0]);
						if(hourString.contains(" PM")) {
							hours = hours + 12;			
						}
					} else {
						hours = Integer.parseInt(hourString);
					}
				}
								
				appointment.setHours(hours);
				appointment.setMinutes(minutes);
				appointment.setSeconds(0);
				model.setAppointment(appointment);
			} catch (NumberFormatException nfe) {
				LOGGER.error("------>The time of appointment is invalid " + hourString);
				return new MessageVO(AppConstants.FAILURE, "Invalid Appointment Date Time. Ticket cannot be created.");
			}
		}
		if(null != raiseTicketVO.getDpmId()) {
			DoNotDisturbVO vo = mobileService.getActiveDndByUserId(raiseTicketVO.getDpmId());
			if(null != vo && null != appointment) {
				Date start = vo.getStartDateTime();
				Date end = vo.getEndDateTime();
				System.out.println("---------->Appointment Date :" + appointment.toGMTString());
				System.out.println("---------->Start Date :" + start.toGMTString());
				System.out.println("---------->End Date :" + end.toGMTString());
				if(appointment.after(start) && appointment.before(end)) {
					LOGGER.error("------>Ticket Appointment Date Time is within Do Not Disturb Timeframe. ");
					return new MessageVO(AppConstants.FAILURE, "Appointment Date Time is within Do Not Disturb Timeframe. Ticket cannot be created.");
				}
			}			
		}
		
		if(null == raiseTicketVO.getDpmId()) {
			model.setStatus(AppConstants.TICKET_STATUS_NEW);
		} else {			
			model.setDpmid(raiseTicketVO.getDpmId() );
			model.setStatus(AppConstants.TICKET_STATUS_ASSIGNED);
		}
		
		if(AppConstants.MODE_ADD.equals(mode)) {
			Ticket lastTicket = ticketRepository.findTopByOrderByTicketDesc();
			if(null == lastTicket || null == lastTicket.getTicket()) {
				model.setTicket(AppConstants.START_TICKET_VALUE);
			} else {
				model.setTicket(1 + lastTicket.getTicket());
			}
		} else {
			model.setTicket(raiseTicketVO.getTicket());
		}
		
		if(isSelfTicket) {
			model.setSelfticket(AppConstants.STATUS_ACTIVE);
			model.setSelfticketid(raiseTicketVO.getSelfTicketId());
		} else {
			model.setCentreid(raiseTicketVO.getCentreId());
			model.setDepartmentid(raiseTicketVO.getDepartmentId());
			model.setCoordinatorid(raiseTicketVO.getCoordinatorId());
			model.setCoordinatorcontact(raiseTicketVO.getContact());
		}
		
		model.setDescription(raiseTicketVO.getDescription());
		
		Ticket savedTicket = ticketRepository.save(model);	
		if(null != savedTicket) {
			
			redisTemplate.opsForHash().delete("TICKET", "ALL_TICKETS");
			
			if(AppConstants.MODE_ADD.equals(mode)) {
				LOGGER.debug("----> New Ticket Saved successfuly with number. -------->" + savedTicket.getTicket());
				System.out.println("------> New Ticket Created  : -------->" + savedTicket.getTicket());
				redisTemplate.opsForHash().put("TICKET", savedTicket.getTicket(), this.getVOFromModel(savedTicket));
				
				return new MessageVO(AppConstants.SUCCESS, "Ticket " + savedTicket.getTicket() + " is created.");
				
			} else {
				LOGGER.debug("---->  Ticket Updated successfuly with number. -------->" + savedTicket.getTicket());
				System.out.println("------>  Ticket Updated  : -------->" + savedTicket.getTicket());
				redisTemplate.opsForHash().delete("TICKET", savedTicket.getTicket());
				redisTemplate.opsForHash().put("TICKET", savedTicket.getTicket(), this.getVOFromModel(savedTicket));
				return new MessageVO(AppConstants.SUCCESS, "Ticket " + savedTicket.getTicket() + " is updated.");
			}			
			
		} else {
			return new MessageVO(AppConstants.FAILURE, "Ticket cannot be created. Please contact Administrator.");
		}
	}

	@Override
	public List<TicketVO> getAllTickets() {
		Object object = redisTemplate.opsForHash().get("TICKET", "ALL_TICKETS");
		if(null != object) {
			return (List<TicketVO>)object;
		}
		
		List<Ticket> list = ticketRepository.findByOrderByTicketidDesc();
		if(null == list) return null;
		
		List<TicketVO> voList = this.getVOListFromModelList(list);
		redisTemplate.opsForHash().put("TICKET", "ALL_TICKETS", voList);
		return voList;
	}

	@Override
	public String deleteTicket(Integer ticket) {
		if(null == ticket ) return AppConstants.FAILURE;
		TicketVO vo = this.getTicketByTicketNumber(ticket);
		if (null == vo || null == vo.getTicketId()) return AppConstants.FAILURE;
		ticketRepository.delete(vo.getTicketId());
		return AppConstants.SUCCESS;
	}

	@Override
	public TicketDetailInProgressVO getInProgressTicketDetails(Integer ticketNumber) {
		return this.getInProgressTicketDetails(ticketNumber, false);
	}

	@Override
	public TicketTripCostWrapper getReimbursementForDPM(Integer dpmId) {
		if(null == dpmId) return null;
		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("---> Getting Ticket Reimbursments for DPM " + dpmId);
		}
		List<String> ticketStatusList = new ArrayList<String>();
		ticketStatusList.add(AppConstants.TICKET_STATUS_COMPLETED);
		//List<Ticket> tickets = ticketRepository.findByDpmidAndStatusInOrderByTicketidDesc(dpmId, ticketStatusList);
		List<Ticket> tickets = ticketRepository.findByDpmidAndStatusInOrderByStartdatetimeDesc(dpmId, ticketStatusList);
		
		if(null != tickets && !tickets.isEmpty()) {
			
			//List<TripCostVO> tripsPerDay = new ArrayList<TripCostVO>();
			ZoneId defaultZone = ZoneId.systemDefault();
			LocalDate localDate = null;
			List<TripCostVO> tripCostList = new ArrayList<TripCostVO>();
			List<TripDateVO> tripDateList = new ArrayList<TripDateVO>();
			TripDateVO tripDate = new TripDateVO();
			BigDecimal bd = null;
			for(Ticket ticket: tickets) {	
				
				TicketTripVO vo = ticketTripService.getTripByTicketAndDpm(ticket.getTicket(), dpmId);
				
				Instant instant = vo.getTripStartDate().toInstant();
				LocalDate ticketLocalDate = instant.atZone(defaultZone).toLocalDate();
				bd = new BigDecimal(AppConstants.TRIP_COST_PER_KM * vo.getDistance()).setScale(2, RoundingMode.HALF_EVEN);
				
				if(null == localDate) {
					localDate = ticketLocalDate;
					TripCostVO cost = new TripCostVO();
					cost.setCost(bd);
					cost.setKms(vo.getDistance());
					cost.setTicket(vo.getTicket());
					tripCostList.add(cost);
					tripDate.setTripDate(vo.getTripStartDate());
					
					if(LOGGER.isInfoEnabled()) {
						LOGGER.info("---> Getting Ticket Reimbursments for the ticket " + ticket.getTicket() + " for the first date " + localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
					}
					
				}
				else if(ticketLocalDate.compareTo(localDate) == 0) {
					localDate = ticketLocalDate;
					TripCostVO cost = new TripCostVO();
					cost.setCost(bd);
					cost.setKms(vo.getDistance());
					cost.setTicket(vo.getTicket());
					tripCostList.add(cost);
					if(LOGGER.isInfoEnabled()) {
						LOGGER.info("---> Getting Ticket Reimbursments Same Date " + ticket.getTicket() + " for the same date " + localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
					}
					
				} else {
					tripDate.setTrips(tripCostList);
					tripDateList.add(tripDate);
					
					
					tripDate = new TripDateVO(); //start for the new date
					tripCostList = new ArrayList<TripCostVO>();  //reset the trip costs list
														
					localDate = ticketLocalDate;
					TripCostVO cost = new TripCostVO();
					cost.setCost(bd);
					cost.setKms(vo.getDistance());
					cost.setTicket(vo.getTicket());
					tripCostList.add(cost);
					
					tripDate.setTripDate(vo.getTripStartDate()); // point to the new date		
					
					if(LOGGER.isInfoEnabled()) {
						LOGGER.info("---> Getting Ticket Reimbursments Date Changed now " + ticket.getTicket() + " for the new date " + localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
					}
				}
								
			} 
			if(!tripCostList.isEmpty()) {	//this is after the last ticket has completed and the for loop has completed
				tripDate.setTrips(tripCostList);
				tripDateList.add(tripDate);
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("---> Getting Ticket Reimbursments for the last date " + localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
				}
			}
			
			TicketTripCostWrapper wrapper = new TicketTripCostWrapper();
			UserVO userVO = userService.getUserById(dpmId);
			wrapper.setEmail(userVO.getEmail());
			wrapper.setFirstName(userVO.getFirstName() + " " + userVO.getLastName());
			wrapper.setStatus(AppConstants.SUCCESS);
			wrapper.setTripList(tripDateList);
			return wrapper;
		}
		
		return null;
	}

	@Override
	public TicketTripsReimbursementWrapper getTripCostsForDPM(Integer dpmId) {
		if(null == dpmId) return null;
		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("---> Getting Trip Costs Reimbursments for DPM " + dpmId);
		}
		List<String> ticketStatusList = new ArrayList<String>();
		ticketStatusList.add(AppConstants.TICKET_STATUS_COMPLETED);
		List<Ticket> tickets = ticketRepository.findByDpmidAndStatusInOrderByStartdatetimeDesc(dpmId, ticketStatusList);
		Double navigationDistance = null;
		if(null != tickets && !tickets.isEmpty()) {			
			List<TripDateCostVO> tripCosts = new ArrayList<TripDateCostVO>();
			for(Ticket ticket: tickets) {					
				
				TicketTripVO vo = ticketTripService.getTripByTicketAndDpm(ticket.getTicket(), dpmId);
				
				navigationDistance = this.ticketTripNavigationService.getTripNavigationDistance(vo.getTicketTripId());				
								
				TripDateCostVO tripDateCostVO = new TripDateCostVO();				
				tripDateCostVO.setTripDate(vo.getTripStartDate());
				
				//Defect Fix - trip cost should be from actual distance from navigation
				BigDecimal tripcost = new BigDecimal(AppConstants.TRIP_COST_PER_KM * navigationDistance)
														.setScale(2, RoundingMode.HALF_EVEN);
				//BigDecimal tripcost = new BigDecimal(AppConstants.TRIP_COST_PER_KM * vo.getDistance()).setScale(2, RoundingMode.HALF_EVEN);
				System.out.println("------->Navigation trip Cost for ticket " + ticket.getTicket() + " is : " + tripcost.toString() + "<-----------");
				tripDateCostVO.setCost(tripcost.toString());
				//tripDateCostVO.setKms(vo.getDistance());
				tripDateCostVO.setKms(navigationDistance.floatValue());
				tripDateCostVO.setTicket(vo.getTicket());
				tripCosts.add(tripDateCostVO);
			} 
			
			TicketTripsReimbursementWrapper wrapper = new TicketTripsReimbursementWrapper();
			UserVO userVO = userService.getUserById(dpmId);
			wrapper.setEmail(userVO.getEmail());
			wrapper.setFirstName(userVO.getFirstName() + " " + userVO.getLastName());
			wrapper.setStatus(AppConstants.SUCCESS);
			wrapper.setTripList(tripCosts);
			return wrapper;
		}
		
		return null;
	}

	@Override
	@Transactional
	public synchronized MobileResponseVO  createSelfTicket(SelfTicketRequestVO selfTicketRequestVO) {
		if(null == selfTicketRequestVO || null == selfTicketRequestVO.getCentre() || null == selfTicketRequestVO.getAddress() 
				|| null == selfTicketRequestVO.getDateofappointment() || null == selfTicketRequestVO.getHourofappointment()) {
			return new MobileResponseVO(AppConstants.FAILURE, "Please fill in the fields to create a ticket.");
		} else if(null == selfTicketRequestVO.getLatitude() || null == selfTicketRequestVO.getLongitude()) {
			return new MobileResponseVO(AppConstants.FAILURE, "Please choose a location on the map to create a ticket.");
		} else if(null == selfTicketRequestVO.getUserId()) {
			return new MobileResponseVO(AppConstants.FAILURE, "Please login as a DPM to create a ticket.");
		} else {
			//add a validation to check if the user is a dpm and match him with the email id sent
			//create a selfticket detail and get the PK
			SelfTicket self = new SelfTicket();
			self.setAddress(selfTicketRequestVO.getAddress());
			self.setCentre(selfTicketRequestVO.getCentre());
			self.setCreatedby(selfTicketRequestVO.getUserId());
			if(selfTicketRequestVO.getLatitude().equals(0D) || selfTicketRequestVO.getLongitude().equals(0D)) {
				return new MobileResponseVO(AppConstants.FAILURE, "Cannot create Self Ticket. Please ensure GPS and Internet connectivity.");
			}
			self.setLatitude(selfTicketRequestVO.getLatitude());
			self.setLongitude(selfTicketRequestVO.getLongitude());
			self.setCreatedon(new Date());
			SelfTicket newSelfTicket = this.selfTicketRepository.save(self);
			if(null == newSelfTicket) {
				return new MobileResponseVO(AppConstants.FAILURE, "Could not create a Self Ticket. Please contact admin.");
			}
			redisTemplate.opsForHash().delete("SELF_TICKET", newSelfTicket.getSelfticketid());
			redisTemplate.opsForHash().put("SELF_TICKET", newSelfTicket.getSelfticketid(), newSelfTicket);
			
			//raise a new ticket
			RaiseTicketVO vo = new RaiseTicketVO();
			vo.setDateofappointment(selfTicketRequestVO.getDateofappointment());
			vo.setHourofappointment(selfTicketRequestVO.getHourofappointment());
			vo.setDescription(selfTicketRequestVO.getDescription());
			vo.setDpmId(selfTicketRequestVO.getUserId());
			vo.setEmail(selfTicketRequestVO.getEmail());
			vo.setSelfTicketId(newSelfTicket.getSelfticketid());
			MessageVO messageVO = this.raiseTicket(vo, true);
			return new MobileResponseVO(messageVO.getResult(), messageVO.getMessage());
		}
	}
	
	@Override
	public SelfTicket getSelfTicket(Integer selfTicketId) {
		if(null == selfTicketId) return null;
		Object object = redisTemplate.opsForHash().get("SELF_TICKET", selfTicketId);
		if(null != object) {
			return (SelfTicket)object;
		}
		SelfTicket self = selfTicketRepository.findBySelfticketid(selfTicketId);
		if(null != self) {
			redisTemplate.opsForHash().put("SELF_TICKET", self.getSelfticketid(), self);
			return self;
		}
		return null;
	}

	@Override
	public List<TicketTripStatsVO> getTicketTripStatistics() {		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String startString = "28/11/2019 00:00";
		Date start = new Date();
		try {
			start = formatter.parse(startString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date end = new Date();
		List<Ticket> tickets = ticketRepository.findByStatusAndStartdatetimeBetweenOrderByTicketidDesc(AppConstants.TICKET_STATUS_COMPLETED, start, end);
		
		List<TicketTripStatsVO> tripStatsList = new ArrayList<TicketTripStatsVO>();		
		//Startdate, DPM name, Trip start, Trip End, Start Location, End Location, Ticket #, distance, cost
		for(Ticket ticket: tickets) {
			TicketVO ticketVO = this.getTicketByTicketNumber(ticket.getTicket());
			TicketTripStatsVO vo = new TicketTripStatsVO();
			vo.setDate(ticket.getAppointment());
			vo.setDpm(ticketVO.getDpm());
			vo.setTicket(ticket.getTicket());
			if(null != ticket.getSelfticket()) {
				vo.setType(AppConstants.TICKET_TYPE_SELF);
			} else {
				vo.setType(AppConstants.TICKET_TYPE_ADMIN);
			}
			vo.setDescription(ticket.getDescription());
			vo.setPhoto(ticket.getPhoto());
			
			TicketTripVO tripVO = this.ticketTripService.getTripByTicketAndDpm(ticket.getTicket(), ticket.getDpmid());
			vo.setDistance(tripVO.getDistance());
			vo.setCost(new BigDecimal(AppConstants.TRIP_COST_PER_KM * tripVO.getDistance()).setScale(2, RoundingMode.HALF_UP));
			vo.setTripStart(tripVO.getTripStartDate());
			vo.setTripEnd(tripVO.getTripEndDate());
			vo.setStartLocation(""+tripVO.getTripStartLatitude()+ ","+ tripVO.getTripStartLongitude());
			TicketTripNavigationWrapper wrapper = this.ticketTripNavigationService.getTripNavigation(tripVO.getTicketTripId());
			if(null != wrapper) {
				vo.setEndLocation(""+wrapper.getEndLatitude() + "," + wrapper.getEndLongitude());
			} else {
				vo.setEndLocation("0,0");
			}
			tripStatsList.add(vo);
		}
		return tripStatsList;
	}

	@Override
	public List<TicketTripStatsVO> getTripsByDateRange(SearchDatesVO search) {
		if(null == search || null == search.getStartdate() || null == search.getEnddate()) return null;	
		/*
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");		
		String startString = "28/11/2019 00:00";
		Date start = new Date();
		try {
			start = formatter.parse(startString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date end = new Date();
		*/
		Date start  = search.getStartdate();
		start.setHours(0);
		start.setMinutes(0);
		
		Date end = search.getEnddate();
		end.setHours(23);
		end.setMinutes(59);
		List<Ticket> tickets = ticketRepository.findByStatusAndStartdatetimeBetweenOrderByTicketidDesc(AppConstants.TICKET_STATUS_COMPLETED, start, end);
		
		
		List<TicketTripStatsVO> tripStatsList = new ArrayList<TicketTripStatsVO>();		
		//Startdate, DPM name, Trip start, Trip End, Start Location, End Location, Ticket #, distance, cost
		for(Ticket ticket: tickets) {
			TicketVO ticketVO = this.getTicketByTicketNumber(ticket.getTicket());
			TicketTripStatsVO vo = new TicketTripStatsVO();
			vo.setDate(ticket.getAppointment());
			vo.setDpm(ticketVO.getDpm());
			vo.setTicket(ticket.getTicket());
			if(null != ticket.getSelfticket()) {
				vo.setType(AppConstants.TICKET_TYPE_SELF);
			} else {
				vo.setType(AppConstants.TICKET_TYPE_ADMIN);
			}
			vo.setDescription(ticket.getDescription());
			vo.setPhoto(ticket.getPhoto());
			
			TicketTripVO tripVO = this.ticketTripService.getTripByTicketAndDpm(ticket.getTicket(), ticket.getDpmid());
			Double distanceTravelled = this.ticketTripNavigationService.getTripNavigationDistance(tripVO.getTicketTripId());
			if(null != distanceTravelled) vo.setDistanceTravelled(distanceTravelled.floatValue());
			if(tripVO.getDistance() > AppConstants.MAX_DISTANCE_PERMITTED) {
				vo.setDistance(0F);
				vo.setCost(new BigDecimal(0.00));
			} else {
				vo.setDistance(tripVO.getDistance());
				vo.setCost(new BigDecimal(AppConstants.TRIP_COST_PER_KM * tripVO.getDistance()).setScale(2, RoundingMode.HALF_UP));
			}			
			vo.setTripStart(tripVO.getTripStartDate());
			vo.setTripEnd(tripVO.getTripEndDate());
			vo.setStartLocation(""+tripVO.getTripStartLatitude()+ ","+ tripVO.getTripStartLongitude());
			TicketTripNavigationWrapper wrapper = this.ticketTripNavigationService.getTripNavigation(tripVO.getTicketTripId());
			if(null != wrapper) {
				vo.setEndLocation(""+wrapper.getEndLatitude() + "," + wrapper.getEndLongitude());
			} else {
				vo.setEndLocation("0,0");
			}
			tripStatsList.add(vo);
		}
		return tripStatsList;
	}
	
	@Override
	public List<TicketTripStatsVO> getTripsForDPMWithDateRange(String month, String year, Integer dpmId, boolean sendEmail) {
		System.out.println("In the TicketService Implenmentation witbh month as : " + month + " : " + dpmId);
		if(null == month || month.isEmpty()) return null;
		
		Month monthNow = Month.valueOf(month.toUpperCase());		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		String startString = "";
		String endString = "";
		LocalDate date = LocalDate.of(Integer.parseInt(year), monthNow, 1);
		boolean isLeap = date.isLeapYear();
		
		if(monthNow.getValue() < 10) {
			startString = "01/0"+ monthNow.getValue() + "/" + year + " 00:00:01";
			endString = monthNow.length(isLeap) +"/0"+ monthNow.getValue() + "/" + year + " 23:59:59";			
		} else {
			startString = "01/"+ monthNow.getValue() + "/" + year + " 00:00:01";
			endString = monthNow.length(isLeap)  + "/"+ monthNow.getValue() + "/" + year + " 23:59:59";			
		}
		
		System.out.println("Dates strings are " + startString + " and end with " + endString);
		
		Date start = new Date();
		Date end = new Date();
		try {
			start = formatter.parse(startString);
			end = formatter.parse(endString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<Ticket> tickets = ticketRepository.findByDpmidAndStatusAndStartdatetimeBetweenOrderByStartdatetimeDesc(dpmId,AppConstants.TICKET_STATUS_COMPLETED, start, end);
		if(null != tickets) {
			System.out.println("Tickets for the dpm are " + tickets.size());
		} else {
			System.out.println("There are no tickets for : " + dpmId);
		}
		
		List<TicketTripStatsVO> tripStatsList = new ArrayList<TicketTripStatsVO>();		
		//Startdate, DPM name, Trip start, Trip End, Start Location, End Location, Ticket #, distance, cost
		for(Ticket ticket: tickets) {
			TicketVO ticketVO = this.getTicketByTicketNumber(ticket.getTicket());
			TicketTripStatsVO vo = new TicketTripStatsVO();
			vo.setDate(ticket.getAppointment());
			vo.setDpm(ticketVO.getDpm());
			vo.setTicket(ticket.getTicket());
			if(null != ticket.getSelfticket()) {
				vo.setType(AppConstants.TICKET_TYPE_SELF);
			} else {
				vo.setType(AppConstants.TICKET_TYPE_ADMIN);
			}
			vo.setDescription(ticket.getDescription());
			vo.setPhoto(ticket.getPhoto());
			
			TicketTripVO tripVO = this.ticketTripService.getTripByTicketAndDpm(ticket.getTicket(), ticket.getDpmid());	
			if(tripVO.getDistance() > AppConstants.MAX_DISTANCE_PERMITTED) {
				vo.setDistance(0.0F);
			} else {
				vo.setDistance(tripVO.getDistance());
			}
			vo.setDistanceTravelled(tripVO.getDistanceTravelled());	//Cyrus - new attribute
			if(tripVO.getTripStartLatitude() < 1 || tripVO.getTripStartLongitude() < 1) {
				vo.setCost(new BigDecimal(0.00));
			} else {
				vo.setCost(new BigDecimal(AppConstants.TRIP_COST_PER_KM * tripVO.getDistance()).setScale(2, RoundingMode.HALF_UP));
			}
			vo.setTripStart(tripVO.getTripStartDate());
			vo.setTripEnd(tripVO.getTripEndDate());
			vo.setStartLocation(""+tripVO.getTripStartLatitude()+ ","+ tripVO.getTripStartLongitude());
			TicketTripNavigationWrapper wrapper = this.ticketTripNavigationService.getTripNavigation(tripVO.getTicketTripId());			
			if(null != wrapper) {
				vo.setEndLocation(""+wrapper.getEndLatitude() + "," + wrapper.getEndLongitude());
			} else {
				vo.setEndLocation("0,0");
			}
			tripStatsList.add(vo);
		}
		
		if(!tripStatsList.isEmpty()) {
			writePDFReports(month, year, dpmId, tripStatsList, sendEmail);			
		}
		return tripStatsList;
	}
	
	
	private String writePDFReports(String month, String year, Integer dpmId, List<TicketTripStatsVO> tripStatsList, boolean doSendEmail) {
		String dpm = this.userService.getNameById(dpmId);
		Document document = new Document();
		File uploadingdirs = null;
		boolean isSuccess = true;
		String reportFilePath = null;
	    try {
	    	  
	    	uploadingdirs = new File("D:\\csv\\reports\\"+dpm.replace(" ", "_")); 
	    	  uploadingdirs.mkdirs();
	    	  
	    	  uploadingdirs = new File(uploadingdirs.getAbsolutePath()+"\\"+month+ "_" + year + "_reports.pdf");
	    	  uploadingdirs.setWritable(true);
	    	  uploadingdirs.setReadable(true);
	    	  
	    	  reportFilePath = "D:" + File.separatorChar + "csv" + File.separatorChar + "reports" + File.separatorChar
	    	  		+ dpm.replace(" ", "_") + File.separatorChar +month+ "_" + year + "_reports.pdf";
			
	    	  FileOutputStream fos = new FileOutputStream(uploadingdirs);
	    	  PdfWriter writer = PdfWriter.getInstance(document, fos);
	    	  document.open();
	                  
	    	  Paragraph paragraph = new Paragraph();
	    	  paragraph.setSpacingAfter(1f);
	          paragraph.add("Monthly Field Trip Reports for " + dpm);
	          paragraph.setAlignment(Element.ALIGN_CENTER);
	                 
	          Paragraph otherParagraph = new Paragraph();
	          otherParagraph.setSpacingAfter(1f);
	          otherParagraph.add("For the month of " + month.toUpperCase() + " - " + year.toUpperCase());
	          otherParagraph.setAlignment(Element.ALIGN_CENTER);
	          
	          Paragraph topLine = new Paragraph();
	          topLine.add("--------------------------------------------------------------------------------------------------------------------------------");
	          topLine.setSpacingBefore(2f);	          	          
	          topLine.setAlignment(Element.ALIGN_CENTER);
	          
	          Paragraph bottomLine = new Paragraph();
	          bottomLine.add("--------------------------------------------------------------------------------------------------------------------------------");	         
	          bottomLine.setSpacingAfter(2f);
	          bottomLine.setAlignment(Element.ALIGN_CENTER);
	          
	          document.add(topLine);
	          document.add(paragraph);
	          document.add(otherParagraph);
	          document.add(bottomLine);
	            
	         PdfPTable table = new PdfPTable(9); // 3 columns.
	         table.setWidthPercentage(100); //Width 100%
	         table.setSpacingBefore(3f); //Space before table
	         table.setSpacingAfter(3f); //Space after table
	  
	         //Set Column widths
	         //float[] columnWidths = {0.85f, 0.6f, 0.6f, 0.7f, 1f,1f,1f,1f,0.8f,2f};
	         float[] columnWidths = {0.85f, 0.7f, 0.9f, 0.9f, 0.9f, 0.95f, 0.95f, 0.8f, 2.05f};
	         table.setWidths(columnWidths);
	  
	         PdfPCell cell1 = new PdfPCell(new Paragraph("Date"));
	         cell1.setBorderColor(BaseColor.BLUE);
	         cell1.setPaddingLeft(2);
	         cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	         cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
	  
	         PdfPCell cell2 = new PdfPCell(new Paragraph("Ticket"));
	         cell2.setBorderColor(BaseColor.BLUE);
	         cell2.setPaddingLeft(2);
	         cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	         cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
	  
	         PdfPCell cell3 = new PdfPCell(new Paragraph("P2P Distance"));
	         cell3.setBorderColor(BaseColor.RED);
	         cell3.setPaddingLeft(1);
	         cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	         cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
	         
	         PdfPCell cell4 = new PdfPCell(new Paragraph("Actual Distance"));
	         cell4.setBorderColor(BaseColor.RED);
	         cell4.setPaddingLeft(1);
	         cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
	         cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);	         
	         
	         PdfPCell cell5 = new PdfPCell(new Paragraph("Cost"));
	         cell5.setBorderColor(BaseColor.RED);
	         cell5.setPaddingLeft(1);
	         cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
	         cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
	         
	         PdfPCell cell6 = new PdfPCell(new Paragraph("Trip Start"));
	         cell6.setBorderColor(BaseColor.BLACK);
	         cell6.setPaddingLeft(2);
	         cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
	         cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
	         
	         PdfPCell cell7 = new PdfPCell(new Paragraph("Trip End"));
	         cell7.setBorderColor(BaseColor.BLACK);
	         cell7.setPaddingLeft(2);
	         cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
	         cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
	  
	         PdfPCell cell8 = new PdfPCell(new Paragraph("Type"));
	         cell8.setBorderColor(BaseColor.MAGENTA);
	         cell8.setPaddingLeft(2);
	         cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
	         cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
	         cell8.setUseBorderPadding(true);
	         
	         PdfPCell cell9 = new PdfPCell(new Paragraph("Description"));
	         cell9.setBorderColor(BaseColor.BLUE);
	         cell9.setPaddingLeft(2);
	         cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
	         cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
	         cell9.setUseBorderPadding(true);
	         
	         //To avoid having the cell border and the content overlap, if you are having thick cell borders
	         //cell1.setUserBorderPadding(true);
	         //cell2.setUserBorderPadding(true);
	         //cell3.setUserBorderPadding(true);
	  
	         table.addCell(cell1);
	         table.addCell(cell2);
	         table.addCell(cell3);
	         table.addCell(cell4);
	         table.addCell(cell5);
	         table.addCell(cell6);
	         table.addCell(cell7);	         
	         table.addCell(cell8);
	         table.addCell(cell9);
	         table.completeRow();
	         
	         SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM");
	         SimpleDateFormat fullFormatter = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	         String tripStart = "";
	         String tripEnd= "";
	         String ticketDate = "";
	         
	         for(TicketTripStatsVO vo : tripStatsList) {
	        	 
	        	 if(null != vo.getDate()) { 
	        		 ticketDate = formatter.format(vo.getDate());
	        	 }
	        	 cell1 = new PdfPCell(new Paragraph(ticketDate));
		         cell1.setBorderColor(BaseColor.BLUE);
		         cell1.setPaddingLeft(0);
		         cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		         cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  
		         cell2 = new PdfPCell(new Paragraph(""+vo.getTicket()));
		         cell2.setBorderColor(BaseColor.BLUE);
		         cell2.setPaddingLeft(2);
		         cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		         cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  
		         cell3 = new PdfPCell(new Paragraph(""+vo.getDistance()));
		         cell3.setBorderColor(BaseColor.RED);
		         cell3.setPaddingLeft(1);
		         cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		         cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		         
		         cell4 = new PdfPCell(new Paragraph(""+vo.getDistanceTravelled()));
		         cell4.setBorderColor(BaseColor.RED);
		         cell4.setPaddingLeft(1);
		         cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		         cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		         
		         cell5 = new PdfPCell(new Paragraph(""+vo.getCost().toPlainString()));
		         cell5.setBorderColor(BaseColor.RED);
		         cell5.setPaddingLeft(1);
		         cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		         cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		         
		         if(null != vo.getTripStart()) { 
		        	 tripStart = fullFormatter.format(vo.getTripStart());
	        	 }
		         
		         cell6 = new PdfPCell(new Paragraph(tripStart) );
		         cell6.setBorderColor(BaseColor.BLACK);
		         cell6.setPaddingLeft(2);
		         cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		         cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
		         
		         if(null != vo.getTripEnd()) { 
		        	 tripEnd = fullFormatter.format(vo.getTripEnd());
	        	 }
		         cell7 = new PdfPCell(new Paragraph(tripEnd) );
		         cell7.setBorderColor(BaseColor.BLACK);
		         cell7.setPaddingLeft(2);
		         cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
		         cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
		   
		         cell8 = new PdfPCell(new Paragraph(""+vo.getType()));
		         cell8.setBorderColor(BaseColor.MAGENTA);
		         cell8.setPaddingLeft(2);
		         cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
		         cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
		         cell8.setUseBorderPadding(true);
		         
		         cell9 = new PdfPCell(new Paragraph(""+ vo.getDescription()));
		         cell9.setBorderColor(BaseColor.BLUE);
		         cell9.setPaddingLeft(2);
		         cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
		         cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
		         cell9.setUseBorderPadding(true);
		         
		         table.addCell(cell1);
		         table.addCell(cell2);
		         table.addCell(cell3);
		         table.addCell(cell4);
		         table.addCell(cell5);
		         table.addCell(cell6);
		         table.addCell(cell7);
		         table.addCell(cell8);
		         table.addCell(cell9);
		         table.completeRow();
	        	 
		         tripStart = "";
		         tripEnd= "";
		         ticketDate = "";
	         }
	  
	         document.add(table);
	         document.add(bottomLine);
	         Double totalTripCost = tripStatsList.stream()
												.filter(y -> null!= y.getStartLocation() && !y.getStartLocation().startsWith("0.00") )
												.mapToDouble(x -> x.getCost().doubleValue())
												.sum();
	         bottomLine = new Paragraph();
	         DecimalFormat format = new DecimalFormat("#.##");
	         bottomLine.add("Total Cost for the Trips is : " + format.format(totalTripCost) + " INR.");	         
	         bottomLine.setSpacingAfter(2f);
	         bottomLine.setAlignment(Element.ALIGN_CENTER);
	         document.add(bottomLine);
	          
	         document.close();
	         writer.close();
	         fos.close();
	         
	      } catch (DocumentException e) {
	    	  isSuccess = false;
	    	  LOGGER.error("PDF writing exception during monthly reports generation : " + e);
	         e.printStackTrace();
	      } catch (FileNotFoundException e) {
	    	  isSuccess = false;
	    	  LOGGER.error("PDF file generation exception during monthly reports generation : " + e);
	         e.printStackTrace();
	      } catch (IOException e) {
	    	  LOGGER.error("Cannot close report file during monthly reports generation : " + e);
	    	  e.printStackTrace();
	      }
	    
	    if(doSendEmail && isSuccess && null != uploadingdirs) {
	    	String userEmail = this.userService.getUserById(dpmId).getEmail();
	    	if(null != userEmail && !userEmail.isEmpty()) {
	    		try {
					Thread.sleep(10*1000);	//10 seconds	
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		if(sendEmail(reportFilePath, dpm, userEmail, month, year)) {
	    			schedulerTasksService.sendMonthlyReport(month, Integer.parseInt(year),
	    													uploadingdirs.getAbsolutePath(), dpmId);
	    			try {
						Thread.sleep(10*1000);	//10 seconds	
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			return "Success! Email Sent To " + dpm;
	    		}
	    	}
	    	
	    }
	    return "Failure. Email Not Sent To " + dpm;
	}

	private boolean sendEmail(String attachmentFilePath, String user, String userEmail, String month, String year) {
		LOGGER.debug("Sending monthly report email to the user : " + userEmail + " file is " + attachmentFilePath);
		System.out.println("Sending monthly report email to the user : " + userEmail + " file is " + attachmentFilePath);
		MimeMessage mimeMessage = javaMailService.createMimeMessage();
        FileSystemResource file = new FileSystemResource(new File(attachmentFilePath));
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo( new InternetAddress(userEmail));
            helper.setCc( new InternetAddress(AppConstants.EMAIL_ADMIN));
            helper.setFrom(AppConstants.EMAIL_SENDER);
            helper.setText("Dear " + user +", \n PFA the field trip report for " + month +" - " + year + ".");
            helper.setSubject("Field Trips Report for " + month + " - " + year);
            helper.addAttachment(file.getFilename(), file);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
		
		try {
			javaMailService.send(mimeMessage);
		} catch (MailException e) {
			LOGGER.error("Could not send email to : " + userEmail + " because of " + e );
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean sendEscalationEmail(TicketVO vo) {
		LOGGER.debug("Sending Escalation for ticket to the admin : " + AppConstants.EMAIL_ADMIN );
		System.out.println("Sending Escalation for ticket to the admin : " + AppConstants.EMAIL_ADMIN );
		MimeMessage mimeMessage = javaMailService.createMimeMessage();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo( new InternetAddress(AppConstants.EMAIL_ADMIN)); 
            helper.setCc( new InternetAddress(AppConstants.EMAIL_SUPPORT));
            helper.setFrom(AppConstants.EMAIL_SENDER);
            helper.setText("Dear Admin, \n\n Please check the ticket " + vo.getTicket() 
            					+ " assigned to " + vo.getDpm() + "\n whose appointment scheduled at " 
            					+ sdf.format(vo.getAppointment())
            				);
            helper.setSubject("Escalation for Ticket " + vo.getTicket());
            //helper.addAttachment(file.getFilename(), file);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
		
		try {
			javaMailService.send(mimeMessage);
		} catch (MailException e) {
			LOGGER.error("Could not send email to  ADMIN because of " + e );
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private String getReportText(String user, String month, String year) {
		StringBuilder builder = new StringBuilder();
		builder.append("<html><body style=\"margin: 0; padding: 0;\">")
				.append(" <table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">")
				.append("  <tr>")
				.append("   <td>")
				.append("    Dear ").append(user).append(",<br/><br/>")
				.append("	PFA the Monthly Reports on Field Trips. <br/><br/>")
				.append("	Regards, <br/>")
				.append("	FTA Support.		")
				.append("   </td>")
				.append("  </tr>")
				.append(" </table>")
				.append("</body></html>");
		return builder.toString();
	}

	@Override
	public TripHistogramVO getPreviousLastMonthTripStats() {
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
		
		return this.getMonthlyTripStats("January 2020", start, end);
	}
	
	@Override
	public TripHistogramVO getLastMonthTripStats() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");		
		String startString = "01/02/2020 00:00:01";
		String endString = "29/02/2020 23:59:59";
		Date start = new Date();
		Date end = new Date();
		try {
			start = formatter.parse(startString);
			end = formatter.parse(endString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return this.getMonthlyTripStats("February 2020", start, end);
	}
	
	@Override
	public TripHistogramVO getThisMonthTripStats() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");		
		String startString = "01/03/2020 00:00:01";
		String endString = "31/03/2020 23:59:59";
		Date start = new Date();
		Date end = new Date();
		try {
			start = formatter.parse(startString);
			end = formatter.parse(endString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return this.getMonthlyTripStats("March 2020", start, end);
	}
	
	private TripHistogramVO getMonthlyTripStats(String monthYear, Date start, Date end) {
		List<Ticket> tickets = ticketRepository.findByStatusAndStartdatetimeBetweenOrderByTicketidDesc(AppConstants.TICKET_STATUS_COMPLETED, start, end);
		int selfTickets = 0;
		int adminTickets = 0;
		float kms = 0.0f;
		double cost = 0.0D;
		
		for(Ticket ticket: tickets) {
			if(null != ticket.getSelfticket()) {
				selfTickets++;
			} else {
				adminTickets++;
			}
			TicketTripVO tripVO = this.ticketTripService.getTripByTicketAndDpm(ticket.getTicket(), ticket.getDpmid(), true);
			kms = kms + tripVO.getDistance();
			cost = cost + AppConstants.TRIP_COST_PER_KM * tripVO.getDistance();
		}
		
		return new TripHistogramVO(monthYear, kms , cost, (selfTickets + adminTickets));
	}
	
	@Override
	public TicketHistogramVO getPreviousLastMonthTicketStats() {
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
		
		return this.getMonthlyTicketStats("January 2020", start, end);
	}
	
	@Override
	public TicketHistogramVO getLastMonthTicketStats() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");		
		String startString = "01/02/2020 00:00:01";
		String endString = "29/02/2020 23:59:59";
		Date start = new Date();
		Date end = new Date();
		try {
			start = formatter.parse(startString);
			end = formatter.parse(endString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return this.getMonthlyTicketStats("February 2020", start, end);
	}
	
	@Override
	public TicketHistogramVO getThisMonthTicketStats() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");		
		String startString = "01/03/2020 00:00:01";
		String endString = "31/03/2020 23:59:59";
		Date start = new Date();
		Date end = new Date();
		try {
			start = formatter.parse(startString);
			end = formatter.parse(endString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return this.getMonthlyTicketStats("March 2020", start, end);
	}
	
	private TicketHistogramVO getMonthlyTicketStats(String monthYear, Date start, Date end) {
		List<Ticket> tickets = ticketRepository.findByStatusAndStartdatetimeBetweenOrderByTicketidDesc(AppConstants.TICKET_STATUS_COMPLETED, start, end);
		int selfTickets = 0;
		int adminTickets = 0;
		float kms = 0.0f;
		double cost = 0.0D;
		
		for(Ticket ticket: tickets) {
			if(null != ticket.getSelfticket()) {
				selfTickets++;
			} else {
				adminTickets++;
			}
			TicketTripVO tripVO = this.ticketTripService.getTripByTicketAndDpm(ticket.getTicket(), ticket.getDpmid(), true);
			kms = kms + tripVO.getDistance();
			cost = cost + AppConstants.TRIP_COST_PER_KM * tripVO.getDistance();
		}
		
		return new TicketHistogramVO(monthYear, (selfTickets + adminTickets), selfTickets, adminTickets);
	}
	
	@Override
	public List<TicketVO> getOpenTickets() {
		List<String> status = new ArrayList<String>();
		status.add(AppConstants.TICKET_STATUS_NEW);
		status.add(AppConstants.TICKET_STATUS_ASSIGNED);
		List<Ticket> tickets = this.ticketRepository.findByStatusInOrderByTicketidDesc(status);
		
		return getVOListFromModelList(tickets);
	}
	
	@Override
	public List<TicketDPMRankingVO> getClosedTicketsByDateRange() {
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
		
		List<TicketDPMRankingVO> list = new ArrayList<TicketDPMRankingVO>();
		List<Ticket> ticketList = this.ticketRepository.findByStatusAndStartdatetimeBetweenOrderByDpmid(AppConstants.TICKET_STATUS_COMPLETED, start, end);
		
		if(null == ticketList) return null;
		Integer tempDpm = null;
		Integer currentDpm = null;
		String dpm = null;
		int tickets = 0;
		for(Ticket ticket : ticketList) {
			if(null == tempDpm) {
				tempDpm = ticket.getDpmid();
				tickets++;
			}
			else {
				if(tempDpm.equals(ticket.getDpmid())) {
					tickets++;
				} else {
					dpm = this.userService.getNameById(tempDpm);
					list.add(new TicketDPMRankingVO(0, dpm, tickets));
					tickets = 1;
					tempDpm = ticket.getDpmid();
				}
			}			
		}
		dpm = this.userService.getNameById(tempDpm);
		list.add(new TicketDPMRankingVO(0, dpm, tickets));
		
		if(null != list & !list.isEmpty()) {
			//Collections.sort(list, (TicketDPMRankingVO vo1, TicketDPMRankingVO vo2) -> vo1.tickets - vo2.tickets);
			Collections.sort(list, Comparator.comparingInt(TicketDPMRankingVO :: getTickets).reversed());
			for(TicketDPMRankingVO vo : list) {
				System.out.println("----------------->list -->" + vo.getDpm() + " : " + vo.getTickets());				
			}
			return list;
		} else {
			return null;
		}
		
	}

	@Override
	public List<TicketTripStatsVO> getMonthlyTripsStatsByDpmId(Integer dpmId, SearchDatesVO search) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generateTripReportsForAllDPMsForMonth(String month, String year, boolean sendEmail) {
		LOGGER.debug("The Trip Expense report is run for " + month + " " + year + " : " + sendEmail);
		//boolean sendEmail = true;		//send generated reports to respective dpm
		StringBuilder builder = new StringBuilder();
		List<UserVO> dpms = this.userService.getUsersWithRole(AppConstants.ROLE_DPM);
		if(null == dpms || dpms.isEmpty()) {
			return "There are no DPMS in the system to generate reports";
		}
		builder.append("<p><bold> DPM Reports ").append(month).append(" - ").append(year).append("</bold></p><br/>");
		DecimalFormat df = new DecimalFormat("#.##");
		for(UserVO dpm : dpms) {
			if(sendEmail) {
				//Check if it is to send Email and if the dpm report has been sent			
				boolean isSent = schedulerTasksService.isMonthlyReportSent(month, Integer.parseInt(year), dpm.getUserid());
				if(isSent) {
					LOGGER.debug("The email has been sent to DPM already : " + dpm.getFirstName() + " " + dpm.getLastName());
					continue;
				}
			}
			
			List<TicketTripStatsVO> trips = this.getTripsForDPMWithDateRange(month, year, dpm.getUserid(), sendEmail);
			Double totalTripCost = trips.stream()
										.filter(y -> null!= y.getStartLocation() && !y.getStartLocation().startsWith("0.00") )
										.mapToDouble(x -> x.getCost().doubleValue())
										.sum();
			
			builder.append(dpm.getFirstName()).append(" ").append(dpm.getLastName())
					.append(" - Trips : ").append(trips.size())
					.append(" - Cost : " ).append(df.format(totalTripCost)).append("<br/>");
		}
		return builder.toString();
	}

	@Override
	public boolean escalateOpenTicket(TicketVO vo) {
		// TODO Auto-generated method stub
		//check if an escalation is done for the ticket
		if(schedulerTasksService.isEscalationSentForTicket(vo.getTicket())) {
			System.out.println("This ticket has already been escalated : " + vo.getTicket());
			return false;
		} else {
			//if no, insert a new escalation in the system
			if(schedulerTasksService.sendEscationForTicket(vo.getTicket(), vo.getDpmId(), vo.getDpm()) ) {
				System.out.println("This ticket is escalated :" + vo.getTicket());
				this.sendEscalationEmail(vo);
				return true;
			} else {
				System.out.println("Check with Admin. The ticket escalation fails for ticket :" + vo.getTicket());
				return false;
			}
			
		}
		
	}

	
}
