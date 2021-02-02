package com.irctn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

	public Ticket findByTicketid(Integer ticketid);
	
	public Ticket findByTicket(Integer ticket);
	
	public List<Ticket> findByCentreid(Integer centreid);
	
	public List<Ticket> findByDepartmentid(Integer departmentid);
	
	public List<Ticket> findByDpmid(Integer dpmid);
	
	public List<Ticket> findByDpmidAndStatusInOrderByTicketidDesc(Integer dpmid, List<String> status);
	
	public List<Ticket> findByDpmidAndStatusInOrderByStartdatetimeDesc(Integer dpmid, List<String> status);
	
	public List<Ticket> findByCoordinatorid(Integer coordinatorid);
	
	public List<Ticket> findByCoordinatoridAndStatus(Integer coordinatorid, String status);
	
	public Ticket findTopByOrderByTicketDesc();
	
	public List<Ticket> findByOrderByTicketidDesc();
	
	public List<Ticket> findByStatusAndStartdatetimeBetweenOrderByTicketidDesc(String status, Date start, Date end);
	
	public List<Ticket> findByDpmidAndStatusAndStartdatetimeBetweenOrderByStartdatetimeDesc(Integer dpmid, String status, Date start, Date end);
	
	public List<Ticket> findByStatusInOrderByTicketidDesc(List<String> status);
	
	public List<Ticket> findByStatusAndStartdatetimeBetweenOrderByDpmid(String status, Date start, Date end);
	
}
