package com.irctn.service;

import java.util.List;

import com.irctn.model.TicketHistory;
import com.irctn.vo.TicketHistoryVO;

public interface TicketHistoryService {
	
	public TicketHistoryVO getTicketHistoryById(Integer ticketHistoryId);
	
	public List<TicketHistoryVO> getTicketHistoryByTicket(Integer ticket);

	public String saveTicketHistory(TicketHistory ticketHistory);

}
