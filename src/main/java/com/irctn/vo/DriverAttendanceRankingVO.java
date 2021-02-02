package com.irctn.vo;

import java.io.Serializable;

public class DriverAttendanceRankingVO implements Serializable {

	private int rank;
	private String driverName;
	private int days;
	
	public DriverAttendanceRankingVO(int rank, String driverName, int days) {
		super();
		this.rank = rank;
		this.driverName = driverName;
		this.days = days;
	}
	
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	
}
