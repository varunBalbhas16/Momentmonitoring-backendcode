package com.irctn.util;

import java.math.BigDecimal;

public class AppConstants {
	
	public static final Integer STATUS_ACTIVE = 1;
	public static final Integer STATUS_INACTIVE = 0;

	public static final String STATUS_ACTIVE_STR = "Active";
	public static final String STATUS_INACTIVE_STR = "Inactive";
	public static final String STATUS_INPROGRESS_STR = "InProgress";
	public static final String STATUS_COMPLETED_STR = "Completed";
	
	public static final Integer STATUS_INPROGRESS = 2;
	public static final Integer STATUS_COMPLETED = 3;
	public static final Integer STATUS_CLOSED = 5;
	public static final Integer STATUS_NEW = 4;

	public static final String CONTRIBUTOR_TYPE_SCHOOL = "school";
	public static final String CONTRIBUTOR_TYPE_RETAILER = "retailer";
	
	public static final String ROLE_DEPARTMENT_HEAD = "department head";
	public static final String ROLE_DPM = "dpa";
	public static final String ROLE_DRIVER = "driver";
	public static final String ROLE_TICKET_ADMIN = "ticket admin";
	public static final String ROLE_ADMIN = "admin";
	public static final String ROLE_CENTRE_HEAD = "centre head";
	
	public static final String SCHOOL_ADMIN = "School Admin";
	public static final String RETAILER_ADMIN = "Retailer Admin";
	
	public static final Double TOLERANCE = 5.0 ; 
	
	public static final String FAILURE = "failure";
	public static final String SUCCESS = "success";
	public static final String UPDATE_SUCCESS = "updatesuccess";
	
	public static final String ABSENT = "Absent";
	public static final String PRESENT = "Present";
	
	public synchronized static String  qualifySearchString(String search) {
		return "%"+search+"%";
	}
	
	 public static final String UPLOAD_FOLDER = "E:\\c2gx\\uploaded\\";
	 
	 public static final Double TOLERANCE_LIMIT_RETAILERS = 10.0;
	 
	 public static final String STATUS_PROGRESS = "INPROGRESS";
	 
	 public static final String STATUS_ClOSE = "CLOSED";
	 
	 public static final Integer CLOTHES = 1;
	 
	 public static final Integer SOCKS_PAIR = 2;
	 
	 public static final Integer UNDERWEAR = 3;
	 
	 public static final Integer HAND_BAGS = 4;
	 
	 public static final Integer SHOES_PAIR = 5;
	 
	 public static final Integer JEWELLERYANDACCESSORIES = 6;
	 
	 public static final Integer HATS = 7;
	 
	 public static final Integer WATCHES = 8;
	 
	 public static final Integer HOUSEHOLDGOODS = 9;
	 
	 public static final Integer OTHERS = 10;

	 public static final String DATA = "data";
	 
	 //constants for Ticket Status
	 public static final String TICKET_STATUS_NEW = "New";
	 public static final String TICKET_STATUS_REOPENED = "Reopened";
	 public static final String TICKET_STATUS_ASSIGNED = "Assigned";
	 public static final String TICKET_STATUS_ACCEPTED = "Accepted";
	 public static final String TICKET_STATUS_REJECTED = "Rejected";	 
	 public static final String TICKET_STATUS_TRIPSTARTED = "TripStarted";
	 public static final String TICKET_STATUS_TRIPCOMPLETED = "TripCompleted";
	 public static final String TICKET_STATUS_INPROGRESS = "InProgress"; 
	 public static final String TICKET_STATUS_COMPLETED = "Completed";
	 public static final String TICKET_STATUS_CLOSED = "Closed";
	 
	 public static final BigDecimal LOCATION_MATCH_TOLERANCE = new BigDecimal(0.005);
	 
	 public static final int START_TRIP_NUMBER = 49;
	public static final Integer START_TICKET_VALUE = 100;
	public static final String MODE_ADD = "ADD";
	public static final String MODE_UPDATE = "UPDATE";
	public static final Double TRIP_COST_PER_KM = 2.40;
	
	public static final String FIRST_SHIFT = "Shift 1";
	public static final String SECOND_SHIFT = "Shift 2";
	
	public static final Integer FIRST_SHIFT_INT = 1;
	public static final Integer SECOND_SHIFT_INT = 2;
	public static final String TICKET_TYPE_SELF = "Self Ticket";
	public static final String TICKET_TYPE_ADMIN = "Admin Ticket";
	public static final String VEHICLE_OTHERS = "Others";
	
	public static final String EMAIL_ADMIN = "ticketadmin@indianredcrosstnb.com";
	public static final String EMAIL_SUPPORT = "cyrus.s@balbhas.com";
	public static final String EMAIL_SENDER = "irctn.fta@gmail.com"; 
	
	public static final Double MIN_LATITUDE = 7D;
	public static final Double MAX_LATITUDE = 14D;
	
	public static final Double MIN_LONGITUDE = 72D;
	public static final Double MAX_LONGITUDE = 81D;
	public static final Double MAX_DISTANCE_PERMITTED = 1000D;
	
}
