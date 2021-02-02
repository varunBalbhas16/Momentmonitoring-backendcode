package com.irctn.service;

import java.util.List;

import com.irctn.vo.TicketTripVO;
import com.irctn.vo.mobile.TicketTripVOWrapper;
import com.irctn.vo.mobile.request.TicketTripStartRequestVO;

public interface TicketTripService {

	public TicketTripVO getTripById(Integer tripId);
	
	public TicketTripVO getTripByTripNumber(Integer trip);
	
	public List<TicketTripVO> getTripsByTicket(Integer ticket);
	
	public List<TicketTripVO> getTripsByDpmId(Integer dpmId);
	
	public TicketTripVOWrapper saveTrip(TicketTripStartRequestVO vo);

	public TicketTripVOWrapper continueTrip(TicketTripStartRequestVO ticketTripStartRequestVO);

	public String stopTrip(Integer ticket);
	
	public TicketTripVO getTripByTicketAndDpm(Integer ticket, Integer dpmId);
	
	public TicketTripVO getTripByTicketAndDpm(Integer ticket, Integer dpmId, boolean getPTPDistance);

	public Double getTripDistances();
	
	
	
}
