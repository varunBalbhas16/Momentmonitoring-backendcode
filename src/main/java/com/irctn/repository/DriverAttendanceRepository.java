package com.irctn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.DriverAttendance;

public interface DriverAttendanceRepository extends JpaRepository<DriverAttendance, Integer> {
	
	public DriverAttendance findByDriverattendanceid(Integer driverattendanceid);
	
	public List<DriverAttendance> findByDriverid(Integer driverid);
	
	public List<DriverAttendance> findByDateBetween(Date start, Date end);
	
	public List<DriverAttendance> findByDateBetweenOrderByDriverid(Date start, Date end);

	public DriverAttendance findByDriveridAndDate(Integer driverid, Date date);
	
	public List<DriverAttendance> findByDriveridAndDateBetween(Integer driverid, Date start, Date end);
	
	public DriverAttendance findByCreatedby(Integer createdby);
	
	public List<DriverAttendance> findByCreatedbyAndDateBetween(Integer createdby,Date start, Date end);
	
	public List<DriverAttendance> findByVehicleAndDateBetween(String vehicle, Date start, Date end);

}
