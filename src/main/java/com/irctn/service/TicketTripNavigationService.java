package com.irctn.service;

import com.irctn.vo.mobile.TicketProgressWrapper;
import com.irctn.vo.mobile.TicketTripNavigationWrapper;
import com.irctn.vo.mobile.request.TicketTripNavigationRequestVO;
import com.irctn.vo.mobile.request.TicketUpdateRequestVO;

public interface TicketTripNavigationService {
	
	public TicketTripNavigationWrapper saveTripNavigation(TicketTripNavigationRequestVO vo);

	public TicketProgressWrapper endTripNavigation(TicketTripNavigationRequestVO ticketTripNavigationRequestVO);

	public TicketTripNavigationWrapper getTripNavigation(Integer ticketTripId);
	
	public Double getTripNavigationDistance(Integer ticketTripId);
	

}
