package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.SchoolStudentContribution;

public interface SchoolStudentContributionRepository extends JpaRepository<SchoolStudentContribution, Integer>{

	public SchoolStudentContribution findBySchoolstudentcontributionid(Integer schoolstudentcontributionid);

	public List<SchoolStudentContribution> findBySchoolstudentid(Integer studentid);

	public List<SchoolStudentContribution> findBySchoolprogrammappingid(Integer schoolprogrammappingid);
	
	public SchoolStudentContribution findBySchoolstudentidAndSchoolprogrammappingid(Integer schoolstudentid,Integer schoolprogrammappingid);
	
}
