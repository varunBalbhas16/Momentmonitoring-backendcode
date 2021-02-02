package com.irctn.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irctn.model.TicketTrip;
import com.irctn.repository.TicketTripRepository;
import com.irctn.service.TicketService;
import com.irctn.service.TicketTripNavigationService;
import com.irctn.service.TicketTripService;
import com.irctn.util.AppConstants;
import com.irctn.vo.TicketDetailVO;
import com.irctn.vo.TicketTripVO;
import com.irctn.vo.TicketVO;
import com.irctn.vo.mobile.TicketTripVOMobile;
import com.irctn.vo.mobile.TicketTripVOWrapper;
import com.irctn.vo.mobile.TicketVOWrapper;
import com.irctn.vo.mobile.request.TicketTripStartRequestVO;

@Service
public class TicketTripServiceImpl implements TicketTripService {

	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.serviceimpl.TicketTripServiceImpl");	
	
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	TicketTripRepository ticketTripRepository;
	
	@Autowired
	TicketService ticketService;
	
	@Autowired
	TicketTripNavigationService ticketTripNavigationService;
	
	@Override
	public TicketTripVO getTripById(Integer tripId) {
		TicketTrip ticketTrip = ticketTripRepository.findByTickettripid(tripId);
		return getVOFromModel(ticketTrip);
	}

	private TicketTripVO getVOFromModel(TicketTrip model) {
		if(null == model) {
			return null;
		} 
		TicketTripVO vo = new TicketTripVO();
		vo.setTicketTripId(model.getTickettripid());		
		vo.setTicket(model.getTicket());
		vo.setDistance(model.getDistance());
		vo.setDpmid(model.getDpmid());
		vo.setTrip(model.getTrip());
		vo.setTripStartDate(model.getTripstartdate());
		vo.setTripStartLatitude(model.getTripstartlatitude());
		vo.setTripStartLongitude(model.getTripstartlongitude());
		vo.setTripEndDate(model.getTripenddate());
		
		return vo;
	}

	@Override
	public TicketTripVO getTripByTripNumber(Integer trip) {
		return getVOFromModel(ticketTripRepository.findByTrip(trip));
	}

	@Override
	public List<TicketTripVO> getTripsByTicket(Integer ticket) {
		return getVOListFromModelList(ticketTripRepository.findByTicket(ticket));
	}

	private List<TicketTripVO> getVOListFromModelList(List<TicketTrip> tripList) {
		if(null == tripList && tripList.isEmpty()) return null;
		List<TicketTripVO> list = new ArrayList<TicketTripVO>();
		for(TicketTrip model : tripList) {
			list.add(getVOFromModel(model));			
		}
		return list;
	}

	@Override
	public List<TicketTripVO> getTripsByDpmId(Integer dpmId) {
		return getVOListFromModelList(ticketTripRepository.findByDpmid(dpmId));
	}

	@Override
	@Transactional
	public synchronized TicketTripVOWrapper saveTrip(TicketTripStartRequestVO vo) {
		if(null == vo || null == vo.getTicket() || null == vo.getUserId()) {
			return getTicketTripVOWrapper(vo, null, null, false, "Missing fields to start the trip.");
		}
				
		TicketVO ticketVO = ticketService.getTicketByTicketNumber(vo.getTicket());
		if(null == ticketVO || ticketVO.getDpmId().intValue() != vo.getUserId().intValue()) {
			if(null != ticketVO) {
				System.out.println("------------>Saving Trip for dpm " + ticketVO.getDpmId() + "<------------------");
			}	
			System.out.println("------------>Access Denied. Plz login again. Current user id is : " + vo.getUserId() + "<------------------");
			return getTicketTripVOWrapper(vo, null, null, false, "Access Denied. Plz login again.");
		}
		if(! AppConstants.TICKET_STATUS_ACCEPTED.equalsIgnoreCase(ticketVO.getStatus()) ) {
			System.out.println("------------>Access Denied. The Ticket is not ready to start the trip.: " + vo.getUserId() + "<------------------");
			return getTicketTripVOWrapper(vo, null, null, false, "Access Denied. The Ticket is not ready to start the trip.");
		}
		
		//THis is a fix for the null start coordinates.
		if(null == vo.getStartLatitude() || null == vo.getStartLongitude() 
				|| vo.getStartLatitude().equals(0D) || vo.getStartLongitude().equals(0D)) {
			System.out.println("------------>Cannot start the Trip. Please check internet network and GPS and try again for ticket: " + vo.getTicket() + "<------------------");
			LOGGER.error("The Start Coordinates for the ticket is zero. So cannot start Trip for ticket : " + vo.getTicket());
			return getTicketTripVOWrapper(vo, null, null, false, "Cannot start the Trip. Please check internet network and GPS and try again.");
		
		} else if(vo.getStartLatitude() < AppConstants.MIN_LATITUDE || vo.getStartLatitude() > AppConstants.MAX_LATITUDE 
				|| vo.getStartLongitude() < AppConstants.MIN_LONGITUDE || vo.getStartLongitude() > AppConstants.MAX_LONGITUDE) {
			System.out.println("------------>Cannot start the Trip. DPM is out of boundaries of lat and long coordinates for ticket: " + vo.getTicket() + "<------------------");
			LOGGER.error("The Start Coordinates for the ticket is beyond boundaries. So cannot start Trip for ticket : " + vo.getTicket());
			return getTicketTripVOWrapper(vo, null, null, false, "Cannot start the Trip. Please check internet network connection and GPS.");
			
		}
		
		if(vo.getDistanceEstimate() > 1200F) {
			System.out.println("------------>Cannot start the Trip. DPM is out of state of TN for ticket: " + vo.getTicket() + "<------------------");
			LOGGER.error("The distance estimates for this ticket is beyond boundaries. So cannot start Trip for ticket : " + vo.getTicket());
			return getTicketTripVOWrapper(vo, null, null, false, "Cannot start the Trip. Please check internet network connection and GPS.");
		}
		
		TicketTrip ticketTrip = new TicketTrip();
		ticketTrip.setDistance(vo.getDistanceEstimate());
		ticketTrip.setDpmid(vo.getUserId());
		ticketTrip.setTicket(vo.getTicket());

		TicketTrip maxTrip = ticketTripRepository.findTopByOrderByTripDesc();
		int tripNumber = AppConstants.START_TRIP_NUMBER;
		if(null != maxTrip) {
			tripNumber = maxTrip.getTrip();	
		}
		tripNumber++;
		
		ticketTrip.setTrip(tripNumber);
		//ticketTrip.setTripenddate(vo.getTripEndDate());		
		ticketTrip.setTripstartdate(new Date());
		
		if(null != vo.getStartLatitude()) {
			//Double latitude;
			//if(null != (latitude = this.getCoordinates(vo.getStartLatitude())) ) {
				ticketTrip.setTripstartlatitude(vo.getStartLatitude());
			//}
		}
		if(null != vo.getStartLongitude()) {
			//Double longitude;
			//if(null != (longitude = this.getCoordinates(vo.getStartLongitude())) ) {
				ticketTrip.setTripstartlongitude(vo.getStartLongitude());
			//}
		}
		
		TicketTrip savedTicketTrip = ticketTripRepository.save(ticketTrip);
		
		//now update the status of the ticket
		ticketService.updateTicketStatus(vo.getTicket(), AppConstants.TICKET_STATUS_TRIPSTARTED, "Started the trip successfully.", vo.getUserId());
		TicketVOWrapper wrapper = ticketService.getTicket(savedTicketTrip.getTicket());
		
		return getTicketTripVOWrapper(vo, wrapper, savedTicketTrip, true, "Started the trip successfully.");
		
	}
	
	@Override
	@Transactional
	public synchronized TicketTripVOWrapper continueTrip(TicketTripStartRequestVO vo) {
		if(null == vo || null == vo.getTicket() || null == vo.getUserId()) {
			return getTicketTripVOWrapper(vo, null, null, false, "Missing fields to continue the trip.");
		}
				
		TicketVO ticketVO = ticketService.getTicketByTicketNumber(vo.getTicket());
		if(null == ticketVO || null == ticketVO.getDpmId() || null == vo.getUserId() || ticketVO.getDpmId().intValue() != vo.getUserId().intValue()) {
			return getTicketTripVOWrapper(vo, null, null, false, "Access Denied. Plz login again.");
		}
		if(! AppConstants.TICKET_STATUS_TRIPSTARTED.equalsIgnoreCase(ticketVO.getStatus()) ) {
			return getTicketTripVOWrapper(vo, null, null, false, "Access Denied. The Ticket is not in a state to continue the trip.");
		}
		
		TicketTrip tripFromDB = ticketTripRepository.findTopByTicketOrderByTickettripidDesc(vo.getTicket());
		if(null == tripFromDB ) {
			return getTicketTripVOWrapper(vo, null, null, false, "Access Denied. There is no Trip for this Ticket.");
		} else { 
			TicketVOWrapper wrapper = ticketService.getTicket(vo.getTicket());
			return getTicketTripVOWrapper(vo, wrapper, tripFromDB, true, "The trip is continued now.");
		}	
	}
	
	
	private TicketTripVOWrapper getTicketTripVOWrapper(TicketTripStartRequestVO vo, TicketVOWrapper wrapper, TicketTrip savedTicketTrip, boolean result,
			String message) {
		TicketTripVOWrapper tripWrapper = new TicketTripVOWrapper();
		if(!result) {
			tripWrapper.setEmail(vo.getEmail());
			tripWrapper.setErrorMessage(message);
			tripWrapper.setFirstName(null);
			tripWrapper.setStatus(AppConstants.FAILURE);
			tripWrapper.setTicketDetail(null);
			return tripWrapper;
		} else {
			TicketTripVOMobile tripVO = new TicketTripVOMobile();
			TicketDetailVO ticketDetailVO = wrapper.getTicketDetail();
			if(null != ticketDetailVO) {
				tripVO.setTicket(ticketDetailVO.getTicket());
				tripVO.setCentre(ticketDetailVO.getCentre());
				tripVO.setZone(ticketDetailVO.getZone());
				tripVO.setDate(ticketDetailVO.getDate());
				tripVO.setTime(ticketDetailVO.getTime());
				tripVO.setAddress(ticketDetailVO.getAddress());
				tripVO.setCoordinator(ticketDetailVO.getCoordinator());
				tripVO.setContact(ticketDetailVO.getContact());
				tripVO.setDescription(ticketDetailVO.getDescription());
				tripVO.setLatitude(ticketDetailVO.getLatitude());
				tripVO.setLongitude(ticketDetailVO.getLongitude());
				tripVO.setStatus(AppConstants.TICKET_STATUS_TRIPSTARTED);
				tripVO.setTripNumber(savedTicketTrip.getTrip());
				tripVO.setTripStartDate(savedTicketTrip.getTripstartdate());
				tripVO.setTripStartLatitude(savedTicketTrip.getTripstartlatitude());
				tripVO.setTripStartLongitude(savedTicketTrip.getTripstartlongitude());
				tripVO.setDistance(savedTicketTrip.getDistance());
				tripWrapper.setTicketDetail(tripVO);
			}
			
			tripWrapper.setFirstName(wrapper.getFirstName());
			tripWrapper.setErrorMessage(message);
			tripWrapper.setEmail(vo.getEmail());
			tripWrapper.setStatus(AppConstants.SUCCESS);
			return tripWrapper;
		}		
	}

	private Double getCoordinates(String coordinate) {
		if (null == coordinate) return null;
		Double latitude = null;
		try {
			latitude = Double.parseDouble(coordinate);
		} catch(NumberFormatException ignored) {			
		}
		return latitude;		
	}

	@Override
	public String stopTrip(Integer ticket) {
		if(null == ticket) return AppConstants.FAILURE;
		TicketTrip model = this.ticketTripRepository.findTopByTicketOrderByTickettripidDesc(ticket);
		if(null == model) return AppConstants.FAILURE;
		model.setTripenddate(new Date());
		this.ticketTripRepository.save(model);
		return AppConstants.SUCCESS;
	}

	@Override
	public TicketTripVO getTripByTicketAndDpm(Integer ticket, Integer dpmId) {
		/*
		TicketTrip ticketTrip = ticketTripRepository.findByTicketAndDpmid(ticket, dpmId);
		if(null == ticketTrip) return null;
		Double distanceTravelled = ticketTripNavigationService.getTripNavigationDistance(ticketTrip.getTickettripid());
		TicketTripVO vo = getVOFromModel( ticketTrip);
		vo.setDistanceTravelled(distanceTravelled.floatValue());
		return vo;
		*/
		return this.getTripByTicketAndDpm(ticket, dpmId, false);
	}

	@Override
	public Double getTripDistances() {
		// TODO Auto-generated method stub
		return this.ticketTripRepository.sumTripDistance();
	}

	@Override
	public TicketTripVO getTripByTicketAndDpm(Integer ticket, Integer dpmId, boolean getPTPDistance) {
		TicketTrip ticketTrip = ticketTripRepository.findByTicketAndDpmid(ticket, dpmId);
		if(null == ticketTrip) return null;
		Double distanceTravelled = 0D;
		if(!getPTPDistance) {
			distanceTravelled = ticketTripNavigationService.getTripNavigationDistance(ticketTrip.getTickettripid());
		} else {
			distanceTravelled = 0D + ticketTrip.getDistance();
		}
		
		TicketTripVO vo = getVOFromModel( ticketTrip);
		vo.setDistanceTravelled(distanceTravelled.floatValue());
		return vo;
	}
	

}
