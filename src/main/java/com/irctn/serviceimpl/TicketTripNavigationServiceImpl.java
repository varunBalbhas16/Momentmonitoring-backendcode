package com.irctn.serviceimpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.irctn.model.SelfTicket;
import com.irctn.model.TicketTripNavigation;
import com.irctn.repository.TicketTripNavigationRepository;
import com.irctn.service.CentreService;
import com.irctn.service.TicketService;
import com.irctn.service.TicketTripNavigationService;
import com.irctn.service.TicketTripService;
import com.irctn.util.AppConstants;
import com.irctn.vo.CentreVO;
import com.irctn.vo.TicketDetailInProgressVO;
import com.irctn.vo.TicketDetailVO;
import com.irctn.vo.TicketTripVO;
import com.irctn.vo.TicketVO;
import com.irctn.vo.mobile.TicketProgressWrapper;
import com.irctn.vo.mobile.TicketTripNavigationWrapper;
import com.irctn.vo.mobile.TicketVOWrapper;
import com.irctn.vo.mobile.request.TicketTripNavigationRequestVO;

@Service
public class TicketTripNavigationServiceImpl implements TicketTripNavigationService {

	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.serviceimpl.TicketTripNavigationService");	
	
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	TicketTripNavigationRepository ticketTripNavigationRepository;
	
	@Autowired
	TicketTripService ticketTripService;
	
	@Autowired
	TicketService ticketService;
	
	@Autowired
	CentreService centreService;
	
	@Override
	public TicketTripNavigationWrapper saveTripNavigation(TicketTripNavigationRequestVO vo) {
		if(null == vo || null == vo.getTicket() || null == vo.getTrip()) {
			System.out.println("--------->In saveTripNavigation() ----------------- Missing fields - ticket or trip number.");
			LOGGER.error("--------->In saveTripNavigation() ----------------- Missing fields - ticket or trip number.");
			return getTicketTripNavigatorWrapper(AppConstants.FAILURE, vo, "Missing fields - ticket or trip number.", vo.getTicket(), vo.getTrip());
		}
		TicketTripVO ticketTripVO = ticketTripService.getTripByTripNumber(vo.getTrip());
		if(null == ticketTripVO || null == ticketTripVO.getTicket()) {
			System.out.println("--------->In saveTripNavigation() ----------------- Invalid Ticket Trip Details. Please check.");
			LOGGER.error("--------->In saveTripNavigation() ----------------- Invalid Ticket Trip Details. Please check.");
			return getTicketTripNavigatorWrapper(AppConstants.FAILURE, vo, "Invalid Ticket Trip Details. Please check.", vo.getTicket(), vo.getTrip());
		}
		if(!ticketTripVO.getTicket().equals(vo.getTicket())) {
			System.out.println("--------->In saveTripNavigation() ----------------- Invalid fields - ticket or trip number.");
			LOGGER.error("--------->In saveTripNavigation() ----------------- Invalid fields - ticket or trip number.");
			return getTicketTripNavigatorWrapper(AppConstants.FAILURE, vo, "Invalid fields - ticket or trip number.", vo.getTicket(), vo.getTrip());
		}
		
		TicketVO ticketVO = ticketService.getTicketByTicketNumber(vo.getTicket());
		if(! AppConstants.TICKET_STATUS_TRIPSTARTED.equalsIgnoreCase(ticketVO.getStatus()) ) {
			System.out.println("--------->In saveTripNavigation() ----------------- Invalid Ticket Trip Status. Cannot save coordinates.");
			LOGGER.error("--------->In saveTripNavigation() ----------------- Invalid Ticket Trip Status. Cannot save coordinates.");
			return getTicketTripNavigatorWrapper(AppConstants.FAILURE, vo, "Invalid Ticket Trip Status. Cannot save coordinates", vo.getTicket(), vo.getTrip());
		}
		
		TicketTripNavigation navigation = new TicketTripNavigation();
		navigation.setTickettripid(ticketTripVO.getTicketTripId());
		navigation.setTriptime(new Date());
		navigation.setLatitude(vo.getLatitude());
		navigation.setLongitude(vo.getLongitude());
		ticketTripNavigationRepository.save(navigation);
		
		return getTicketTripNavigatorWrapper(AppConstants.SUCCESS, vo, "Trip Route Corodinates saved.", vo.getTicket(), vo.getTrip());
	}

	private TicketTripNavigationWrapper getTicketTripNavigatorWrapper(String status, TicketTripNavigationRequestVO vo,
			String message, Integer ticketNumber, Integer trip) {
		
		System.out.println(" ticketNumber   -------------->" + ticketNumber  + " ----trip------>" + trip);
		TicketTripNavigationWrapper wrapper = new TicketTripNavigationWrapper();
		wrapper.setEmail(vo.getEmail());
		wrapper.setUserId(vo.getUserId());
		wrapper.setStatus(status);
		wrapper.setMessage(message);
		
		System.out.println(" setCurrentLatitude   -------------->" + vo.getLatitude()  + " ----setCurrentLongitude------>" + vo.getLongitude());
		
		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("-------> ticket :"+ ticketNumber + " --trip :" + trip + " --latitude :"+ vo.getLatitude() + " --longitude :" + vo.getLongitude());
		}
		wrapper.setCurrentLatitude(""+vo.getLatitude());
		wrapper.setCurrentLongitude(""+vo.getLongitude());
		
		
		if(null != ticketNumber) {
			TicketDetailInProgressVO detailVO = ticketService.getInProgressTicketDetails(ticketNumber);
			if(null != detailVO) {
				System.out.println(" setEndLatitude   -------------->" + detailVO.getLatitude()  + " ----setEndLongitude------>" + detailVO.getLongitude());
				wrapper.setEndLatitude(detailVO.getLatitude());
				wrapper.setEndLongitude(detailVO.getLongitude());
			}
		}
		
		if(null != trip) {
			TicketTripVO tripVO = ticketTripService.getTripByTripNumber(trip);
			if(null != tripVO) {
				System.out.println(" setStartLatitude   -------------->" + tripVO.getTripStartLatitude()  + " ----setStartLongitude------>" + ""+tripVO.getTripStartLongitude() );
				wrapper.setStartLatitude(""+tripVO.getTripStartLatitude() );
				wrapper.setStartLongitude(""+tripVO.getTripStartLongitude() );
			}
		}
		return wrapper;
	}

	@Override
	public TicketProgressWrapper endTripNavigation(TicketTripNavigationRequestVO ticketTripNavigationRequestVO) {
		//TODO: include validation whether the DPM has arrived in the destination or not.
		if(null == ticketTripNavigationRequestVO || null == ticketTripNavigationRequestVO.getUserId()) {
			return getTicketProgressWrapper(AppConstants.FAILURE, ticketTripNavigationRequestVO, "Lacking data to save.");
		}
		if(null == ticketTripNavigationRequestVO.getTicket() || null == ticketTripNavigationRequestVO.getTrip() || null == ticketTripNavigationRequestVO.getTripMovement()) {
			return getTicketProgressWrapper(AppConstants.FAILURE, ticketTripNavigationRequestVO, "Missing fields in the request. Please check.");
		}
		TicketVO ticketVO = ticketService.getTicketByTicketNumber(ticketTripNavigationRequestVO.getTicket());
		if(! AppConstants.TICKET_STATUS_TRIPSTARTED.equalsIgnoreCase(ticketVO.getStatus()) ) {
			return getTicketProgressWrapper(AppConstants.FAILURE, ticketTripNavigationRequestVO, "Ticket Status does not allow the trip to end.");
		}
		//TODO : add check for user id // if(null != ticketTripNavigationRequestVO.getUserId() )
		if(null != ticketTripNavigationRequestVO.getLatitude() && null != ticketTripNavigationRequestVO.getLongitude()
							&& isCoordinatesWithinBounds(ticketTripNavigationRequestVO)) {
			
			TicketVO vo = ticketService.getTicketByTicketNumber(ticketTripNavigationRequestVO.getTicket());
			if(null == vo) {
				return getTicketProgressWrapper(AppConstants.FAILURE, ticketTripNavigationRequestVO, "Invalid ticket number to be updated.");
			} else if(!vo.getSelfTicket() && null == vo.getCentreId()) {
				return getTicketProgressWrapper(AppConstants.FAILURE, ticketTripNavigationRequestVO, "No Centre mapped to this ticket to be validated.");
			}
			
			BigDecimal clat = null;
			BigDecimal clong = null;
			
			if(vo.getSelfTicket()) {
				if(null == vo.getSelfTicketId()) {
					return getTicketProgressWrapper(AppConstants.FAILURE, ticketTripNavigationRequestVO, "Self Ticket does not have destination to be validated.");
				} else {
					SelfTicket self = this.ticketService.getSelfTicket(vo.getSelfTicketId());
					clat = new BigDecimal(self.getLatitude()).setScale(3, BigDecimal.ROUND_HALF_UP);
					clong = new BigDecimal(self.getLongitude()).setScale(3, BigDecimal.ROUND_HALF_UP);
				}
				
			} else {
				CentreVO centreVO = centreService.getCentreById(vo.getCentreId());
				if(null == centreVO || null == centreVO.getLatitude() || null == centreVO.getLongitude()) {
					return getTicketProgressWrapper(AppConstants.FAILURE, ticketTripNavigationRequestVO, "Invalid Centre data to be validated. Contact System Admin.");
				}				
				clat = new BigDecimal(centreVO.getLatitude()).setScale(3, BigDecimal.ROUND_HALF_UP);
				clong = new BigDecimal(centreVO.getLongitude()).setScale(3, BigDecimal.ROUND_HALF_UP);
			}
			
			
			BigDecimal ulat = new BigDecimal(ticketTripNavigationRequestVO.getLatitude()).setScale(3, BigDecimal.ROUND_HALF_UP);
			BigDecimal ulong = new BigDecimal(ticketTripNavigationRequestVO.getLongitude()).setScale(3, BigDecimal.ROUND_HALF_UP);
			
			LOGGER.info("------->" + ticketTripNavigationRequestVO.getTicket() + " The User Corodinates latitude : " + ulat);
			LOGGER.info("------->" + ticketTripNavigationRequestVO.getTicket() + " Centrres Corodinates latitude : " + clat);
			LOGGER.info("------->" + ticketTripNavigationRequestVO.getTicket() + " The User Corodinates longitude : " + ulong);
			LOGGER.info("------->" + ticketTripNavigationRequestVO.getTicket() + " Centrres Corodinates longitude : " + clong);
			 
			boolean matchesLocation = false;
			if(clat.equals(ulat) && clong.equals(ulong)) {
				LOGGER.info("Coordinaates are perfectly matching......");	
				matchesLocation = true;
			}
			/*
			else if( (!clat.equals(ulat) && (clat.subtract(ulat).compareTo(AppConstants.LOCATION_MATCH_TOLERANCE) == 1 
												|| clat.subtract(ulat).compareTo(AppConstants.LOCATION_MATCH_TOLERANCE) == -1 ) )  
					|| ( !clong.equals(ulong) && (clong.subtract(ulong).compareTo(AppConstants.LOCATION_MATCH_TOLERANCE) == 1 
												|| clong.subtract(ulong).compareTo(AppConstants.LOCATION_MATCH_TOLERANCE) == -1 ) )
					) {
			*/
			else if( 
					(	!clat.equals(ulat) 
							&& (	clat.subtract(ulat).compareTo(AppConstants.LOCATION_MATCH_TOLERANCE) == 1 
									|| clat.subtract(ulat).compareTo(AppConstants.LOCATION_MATCH_TOLERANCE.negate()) == -1
								)
						
					)  || 
					( 	!clong.equals(ulong)
							&& (	 clong.subtract(ulong).compareTo(AppConstants.LOCATION_MATCH_TOLERANCE) == 1 
									|| clong.subtract(ulong).compareTo(AppConstants.LOCATION_MATCH_TOLERANCE.negate()) == -1 
								)
					)
				) {
				
				LOGGER.info("Coordinaates not matching......");
				LOGGER.error("The DPM has not reached the detsination but tries to close the trip for ticket " + ticketTripNavigationRequestVO.getTicket());
				return getTicketProgressWrapper(AppConstants.FAILURE, ticketTripNavigationRequestVO, "Trip Cannot be Closed. You have not reached the destination.");
				
			} else {
				LOGGER.info("Coordinaates matching with the standard tolerance level ......");
				matchesLocation = true;
			}
			/**/
			/*
			boolean matchesLocation = true;
			*/ 
			if(matchesLocation) {
				ticketService.updateTicketStatus(ticketTripNavigationRequestVO.getTicket(), AppConstants.TICKET_STATUS_INPROGRESS,
						"Completed the trip and started the task.", ticketTripNavigationRequestVO.getUserId()	);
				return getTicketProgressWrapper(AppConstants.SUCCESS, ticketTripNavigationRequestVO, "Completed the Trip and start the Task");
			}
			
		}
		//now update the status of the ticket
		//ticketService.updateTicketStatus(ticketTripNavigationRequestVO.getTicket(), AppConstants.TICKET_STATUS_INPROGRESS,
		//			"Completed the trip and started the task.", ticketTripNavigationRequestVO.getUserId()	);
		return getTicketProgressWrapper(AppConstants.FAILURE, ticketTripNavigationRequestVO, "Trip Cannot be Closed. Check your GPS and Internet Connection.");
		
	}

	private boolean isCoordinatesWithinBounds(TicketTripNavigationRequestVO ticketTripNavigationRequestVO) {
		if(ticketTripNavigationRequestVO.getLatitude() < AppConstants.MIN_LATITUDE 
				|| ticketTripNavigationRequestVO.getLatitude() > AppConstants.MAX_LATITUDE
				|| ticketTripNavigationRequestVO.getLongitude() < AppConstants.MIN_LONGITUDE 
				|| ticketTripNavigationRequestVO.getLongitude() > AppConstants.MAX_LONGITUDE ) {
			return false;
		} else {
			return true;
		}
	}

	private TicketProgressWrapper getTicketProgressWrapper(String success,
			TicketTripNavigationRequestVO vo, String comments) {
		
		TicketProgressWrapper wrap = new TicketProgressWrapper();
		if(AppConstants.SUCCESS.equalsIgnoreCase(success)) {
			TicketVOWrapper ticketVOWrapper = ticketService.getTicket(vo.getTicket());
			TicketDetailVO details = ticketVOWrapper.getTicketDetail();
			wrap.setTicketDetail(ticketService.getInProgressTicketDetails(vo.getTicket()) );
		}
		wrap.setEmail(vo.getEmail());
		wrap.setErrorMessage(comments);
		wrap.setStatus(success);
		return wrap;
	}

	@Override
	public TicketTripNavigationWrapper getTripNavigation(Integer ticketTripId) {
		TicketTripNavigation tripNavigation = this.ticketTripNavigationRepository.findTopByTickettripidOrderByTickettripnavigationidDesc(ticketTripId);
		if(null != tripNavigation) {
			TicketTripNavigationWrapper wrapper = new TicketTripNavigationWrapper();
			wrapper.setEndLatitude(""+tripNavigation.getLatitude());
			wrapper.setEndLongitude(""+ tripNavigation.getLongitude());
			return wrapper;
		} else {
			return null;
		}
		
	}

	@Override
	public Double getTripNavigationDistance(Integer ticketTripId) {
		List<TicketTripNavigation> list = this.ticketTripNavigationRepository.findByTickettripidOrderByTickettripnavigationid(ticketTripId);
		if(null == list || list.isEmpty()) return 0D;
		Double distance = 0D;

		List<TicketTripNavigation> zeroCordinateList = new ArrayList<TicketTripNavigation>();
		for(TicketTripNavigation trip : list) {
			if(trip.getLatitude() < 1D || trip.getLongitude() < 1D) {
				zeroCordinateList.add(trip);
			}
		}
		if(!zeroCordinateList.isEmpty()) {
			list.removeAll(zeroCordinateList);
		}
		System.out.println("Getting Distance for Trip Id ------------------->" + ticketTripId);
		for(int i=0,j =1; i < list.size()-1; i++,j++) {			
			distance = distance + getDistance(list.get(j), list.get(i));			
		}
		distance = Math.round(distance * 100.0) / 100.0;
		System.out.println("Total Distance for " + ticketTripId + " ------------------->" + distance);
		return distance;
	}

	private Double getDistance(TicketTripNavigation to, TicketTripNavigation from) {
		Double fromLat = from.getLatitude();
		Double fromLong = from.getLongitude();
		
		Double toLat = to.getLatitude();
		Double toLong = to.getLongitude();
		
		return distance(fromLat, fromLong, toLat, toLong);
	}
		 
	private double distance(double lat1, double lon1, double lat2, double lon2) {
      double theta = lon1 - lon2;
      double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
      dist = Math.acos(dist);
      dist = rad2deg(dist);
      dist = dist * 60 * 1.1515;
      //if (unit == 'K') {
        dist = dist * 1.609344;
//      } else if (unit == 'N') {
//        dist = dist * 0.8684;
//        }
        
        //added variance to this
        dist = dist + (dist*0.1);
        System.out.println("Distance between " + lat1 + "," + lon1 + " and " + lat2 + "," + lon2 + " ----------------------------->" + dist);
      if(Double.isNaN(dist)) dist = 0D;
      //dist = Math.round(dist);
      return (dist);
	}	


    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
      return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
      return (rad * 180.0 / Math.PI);
    }

}
