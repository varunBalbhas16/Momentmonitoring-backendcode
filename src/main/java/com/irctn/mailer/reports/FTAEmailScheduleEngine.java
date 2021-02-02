package com.irctn.mailer.reports;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.irctn.service.TicketService;
import com.irctn.vo.TicketVO;

@Service
@EnableScheduling
public class FTAEmailScheduleEngine {

	@Autowired
	TicketService ticketService;	
	
	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.mailer.reports.FieldTripsMonthlyMailer");
		
	//60 seconds * 60 minute *12 hours - sends system report every half a day.
	@Scheduled(fixedDelay = 4*60*60*1000)
	public void sendMonthlyEmails(){		
		if(LocalDate.now().getDayOfMonth() == 1) {
			System.out.println("Sending Monthly Reports on the first day of this month ....................." + Instant.now());			
			String result = ticketService.generateTripReportsForAllDPMsForMonth(LocalDate.now().minusMonths(1).getMonth().name(),
					""+LocalDate.now().getYear(), true);
			LOGGER.debug("Sent Monthly Emails Response is -------->: " + result);
		}
	}

	@Scheduled(fixedDelay = 5*60*1000)
	public void escalateTickets() {
		System.out.println("This is a escalate Tickets Engine....................." + Instant.now());
		List<TicketVO> tickets = ticketService.getOpenTickets();
		for(TicketVO vo : tickets) {			
			Date appointment = vo.getAppointment();
			Instant appointmentInstant = appointment.toInstant();
			Instant currentTime = Instant.now();
			//if the difference is less than 15 minutes raise an escalation				
			Duration between = Duration.between(appointmentInstant, currentTime);
			//System.out.println("--------->>>The difference is ........................................" + vo.getTicket() + " -- " + between.getSeconds());
			if(between.getSeconds() < 0 && between.getSeconds() > -(15*60) ) {
				System.out.println("--------->>>Send the Escalation now........................................" + vo.getTicket() + " -- " + between.getSeconds());
				ticketService.escalateOpenTicket(vo);
			}
		}
		
	}
	
}
