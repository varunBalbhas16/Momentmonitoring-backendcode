package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.SchoolSortingProcess;

public interface SchoolSortingProcessRepository extends JpaRepository<SchoolSortingProcess, Integer> {

	public SchoolSortingProcess findBySchoolsortingprocessid(Integer schoolSortingProcessId);

	public List<SchoolSortingProcess> findByClothessortingid(Integer clothessortingid);

	public SchoolSortingProcess findBySortingcategoryid(Integer sortingcategoryid);
	
	public SchoolSortingProcess findByClothessortingidAndSchoolstudentcontributionid(Integer clothessortingid,Integer schoolstudentcontributionid);
	
	public List<SchoolSortingProcess> findBySortingcategoryidIsNotNullOrderBySortinguserid();

}
