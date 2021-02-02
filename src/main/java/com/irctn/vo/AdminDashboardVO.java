package com.irctn.vo;

import java.io.Serializable;
import java.util.List;

public class AdminDashboardVO implements Serializable {

	private long programs;
	
	private long activeSchools;
	private long activeRetailers;
	private double totalWeightOfSortedClothes;
	private double totalWeightOfUsableClothes;
	
	private List<ContributionsDoneVO> topSchools;
	private List<ContributionsDoneVO> topRetailers;
	
	private List<ContributionHistogramVO> schoolMonthlyContribution;
	private List<ContributionHistogramVO> retailerMonthlyContribution;
	
	private List<TripHistogramVO> monthlyTripStatistics;
	private List<TicketHistogramVO> monthlyTicketStatistics;
	
	private List<TicketDPMRankingVO> ticketDPMRanking;
	private List<DriverAttendanceRankingVO> driverAttendanceRanking;
	
	
	private int tickets;
	
	private int openTickets;
	
	private double tripKms;
	
	private String tripCost;
	
	private String driversAttendance;
	
	public int getTickets() {
		return tickets;
	}
	public void setTickets(int tickets) {
		this.tickets = tickets;
	}
	public int getOpenTickets() {
		return openTickets;
	}
	public void setOpenTickets(int openTickets) {
		this.openTickets = openTickets;
	}
	public double getTripKms() {
		return tripKms;
	}
	public void setTripKms(double tripKms) {
		this.tripKms = tripKms;
	}
	public String getTripCost() {
		return tripCost;
	}
	public void setTripCost(String tripCost) {
		this.tripCost = tripCost;
	}
	public String getDriversAttendance() {
		return driversAttendance;
	}
	public void setDriversAttendance(String driversAttendance) {
		this.driversAttendance = driversAttendance;
	}
	public List<ContributionsDoneVO> getTopSchools() {
		return topSchools;
	}
	public void setTopSchools(List<ContributionsDoneVO> topSchools) {
		this.topSchools = topSchools;
	}
	public List<ContributionsDoneVO> getTopRetailers() {
		return topRetailers;
	}
	public void setTopRetailers(List<ContributionsDoneVO> topRetailers) {
		this.topRetailers = topRetailers;
	}
	public List<ContributionHistogramVO> getSchoolMonthlyContribution() {
		return schoolMonthlyContribution;
	}
	public void setSchoolMonthlyContribution(List<ContributionHistogramVO> schoolMonthlyContribution) {
		this.schoolMonthlyContribution = schoolMonthlyContribution;
	}
	public List<ContributionHistogramVO> getRetailerMonthlyContribution() {
		return retailerMonthlyContribution;
	}
	public void setRetailerMonthlyContribution(List<ContributionHistogramVO> retailerMonthlyContribution) {
		this.retailerMonthlyContribution = retailerMonthlyContribution;
	}
	
	
	public long getActiveSchools() {
		return activeSchools;
	}
	public void setActiveSchools(long activeSchools) {
		this.activeSchools = activeSchools;
	}
	public long getActiveRetailers() {
		return activeRetailers;
	}
	public void setActiveRetailers(long activeRetailers) {
		this.activeRetailers = activeRetailers;
	}
	public double getTotalWeightOfSortedClothes() {
		return totalWeightOfSortedClothes;
	}
	public void setTotalWeightOfSortedClothes(double totalWeightOfSortedClothes) {
		this.totalWeightOfSortedClothes = totalWeightOfSortedClothes;
	}
	public double getTotalWeightOfUsableClothes() {
		return totalWeightOfUsableClothes;
	}
	public void setTotalWeightOfUsableClothes(double totalWeightOfUsableClothes) {
		this.totalWeightOfUsableClothes = totalWeightOfUsableClothes;
	}
	
	public long getPrograms() {
		return programs;
	}
	public void setPrograms(long programs) {
		this.programs = programs;
	}

	
	public List<TicketDPMRankingVO> getTicketDPMRanking() {
		return ticketDPMRanking;
	}
	public void setTicketDPMRanking(List<TicketDPMRankingVO> ticketDPMRanking) {
		this.ticketDPMRanking = ticketDPMRanking;
	}
	public List<DriverAttendanceRankingVO> getDriverAttendanceRanking() {
		return driverAttendanceRanking;
	}
	public void setDriverAttendanceRanking(List<DriverAttendanceRankingVO> driverAttendanceRanking) {
		this.driverAttendanceRanking = driverAttendanceRanking;
	}
	public List<TripHistogramVO> getMonthlyTripStatistics() {
		return monthlyTripStatistics;
	}
	public void setMonthlyTripStatistics(List<TripHistogramVO> monthlyTripStatistics) {
		this.monthlyTripStatistics = monthlyTripStatistics;
	}
	public List<TicketHistogramVO> getMonthlyTicketStatistics() {
		return monthlyTicketStatistics;
	}
	public void setMonthlyTicketStatistics(List<TicketHistogramVO> monthlyTicketStatistics) {
		this.monthlyTicketStatistics = monthlyTicketStatistics;
	}
	
}
