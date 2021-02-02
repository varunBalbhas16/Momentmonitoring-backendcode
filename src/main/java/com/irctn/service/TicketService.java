package com.irctn.service;

import java.io.File;
import java.util.List;

import com.irctn.model.SelfTicket;
import com.irctn.vo.MessageVO;
import com.irctn.vo.RaiseTicketVO;
import com.irctn.vo.TicketDPMRankingVO;
import com.irctn.vo.TicketDetailInProgressVO;
import com.irctn.vo.TicketHistogramVO;
import com.irctn.vo.TicketTripStatsVO;
import com.irctn.vo.TicketVO;
import com.irctn.vo.TripHistogramVO;
import com.irctn.vo.mobile.MobileResponseVO;
import com.irctn.vo.mobile.TicketProgressListWrapper;
import com.irctn.vo.mobile.TicketProgressWrapper;
import com.irctn.vo.mobile.TicketTripCostWrapper;
import com.irctn.vo.mobile.TicketTripsReimbursementWrapper;
import com.irctn.vo.mobile.TicketVOWrapper;
import com.irctn.vo.mobile.request.SearchDatesVO;
import com.irctn.vo.mobile.request.SelfTicketRequestVO;
import com.irctn.vo.mobile.request.TicketUpdateRequestVO;

public interface TicketService {

	public List<TicketVO> getTicketsForDPM(Integer dpmId);
	
	public TicketProgressWrapper getCurrentTicketForDPM(Integer dpmId);
	
	public List<TicketVO> getTicketListForDPM(Integer dpmId);
	
	public List<TicketVO> getClosedTicketListForDPM(Integer dpmId);
	
	public List<TicketVO> getCompletedTicketListForDPM(Integer dpmId);
	
	public List<TicketDetailInProgressVO> getCompletedTicketListForCoordinator(Integer coordinatorId);
	
	public TicketProgressListWrapper getTicketsForDPMWrapped(Integer dpmId);

	public TicketVOWrapper getTicket(Integer ticket);
	
	public TicketVO getTicketByTicketNumber(Integer ticket);

	public TicketVOWrapper acceptTicket(TicketUpdateRequestVO ticketUpdateRequestVO, boolean isAccepted);

	public String updateTicketStatus(Integer ticket, String status, String comments, Integer userId);

	public TicketDetailInProgressVO getInProgressTicketDetails(Integer ticketNumber);

	public TicketProgressWrapper uploadFile(Integer ticket, File file, String url, Boolean isPhoto);

	public TicketProgressWrapper completeTask(TicketUpdateRequestVO ticketUpdateRequestVO);

	public TicketProgressWrapper closeTask(TicketUpdateRequestVO ticketUpdateRequestVO);

	public MessageVO raiseTicket(RaiseTicketVO raiseTicketVO, boolean isSelfTicket);

	public List<TicketVO> getAllTickets();

	public String deleteTicket(Integer ticket);

	public TicketDetailInProgressVO getInProgressTicketDetails(Integer ticketNumber, boolean isCurrentTicket);

	public TicketTripCostWrapper getReimbursementForDPM(Integer dpmId);

	public TicketTripsReimbursementWrapper getTripCostsForDPM(Integer dpmId);

	public MobileResponseVO createSelfTicket(SelfTicketRequestVO selfTicketRequestVO);

	public SelfTicket getSelfTicket(Integer selfTicketId);

	public List<TicketTripStatsVO> getTicketTripStatistics();

	public List<TicketTripStatsVO> getTripsByDateRange(SearchDatesVO search);

	public List<TicketVO> getOpenTickets();
	
	TripHistogramVO getPreviousLastMonthTripStats();

	TripHistogramVO getLastMonthTripStats();

	TripHistogramVO getThisMonthTripStats();

	TicketHistogramVO getLastMonthTicketStats();

	TicketHistogramVO getThisMonthTicketStats();

	TicketHistogramVO getPreviousLastMonthTicketStats();

	List<TicketDPMRankingVO> getClosedTicketsByDateRange();

	public List<TicketTripStatsVO> getMonthlyTripsStatsByDpmId(Integer dpmId, SearchDatesVO search);

	public String generateTripReportsForAllDPMsForMonth(String month, String year, boolean sendEmails);

	public List<TicketTripStatsVO> getTripsForDPMWithDateRange(String month, String year, Integer dpmId, boolean sendEmail);
	
	public boolean escalateOpenTicket(TicketVO vo);
	
}
