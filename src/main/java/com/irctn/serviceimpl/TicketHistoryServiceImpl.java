package com.irctn.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.irctn.model.TicketHistory;
import com.irctn.repository.TicketHistoryRepository;
import com.irctn.repository.TicketRepository;
import com.irctn.service.TicketHistoryService;
import com.irctn.util.AppConstants;
import com.irctn.vo.TicketHistoryVO;

@Service
public class TicketHistoryServiceImpl implements TicketHistoryService {

	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.serviceimpl.TicketServiceImpl");
	
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	TicketRepository ticketRepository;
	
	@Autowired
	TicketHistoryRepository ticketHistoryRepository;
	
	@Override
	public TicketHistoryVO getTicketHistoryById(Integer ticketHistoryId) {
		return getVOFromModel(ticketHistoryRepository.findByTickethistoryid(ticketHistoryId));
	}

	private TicketHistoryVO getVOFromModel(TicketHistory ticketHistory) {
		if (null == ticketHistory)  return null;
		TicketHistoryVO vo = new TicketHistoryVO();
		vo.setTickethistoryid(ticketHistory.getTickethistoryid());
		vo.setTicket(ticketHistory.getTicket());
		vo.setDpm(ticketHistory.getDpm());
		vo.setLogdate(ticketHistory.getLogdate());
		vo.setStatus(ticketHistory.getStatus());
		vo.setComments(ticketHistory.getComments());
		return vo;
	}

	@Override
	public List<TicketHistoryVO> getTicketHistoryByTicket(Integer ticket) {
		return getVOListFromModels(ticketHistoryRepository.findByTicket(ticket));
	}
	
	@Override
	public String saveTicketHistory(TicketHistory ticketHistory) {
		TicketHistory savedHistory = ticketHistoryRepository.save(ticketHistory);
		LOGGER.debug("The TicketHistory is saved with id : " + savedHistory.getTickethistoryid() + " and ticket " + savedHistory.getTicket());
		return AppConstants.SUCCESS;
	}

	private List<TicketHistoryVO> getVOListFromModels(List<TicketHistory> historyList) {
		if(null == historyList || historyList.isEmpty()) return null;
		List list = new ArrayList<TicketHistoryVO>();
		for(TicketHistory history : historyList) {
			list.add(getVOFromModel(history));
		}
		return list;
	}

}
