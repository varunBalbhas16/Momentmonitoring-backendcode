package com.irctn.service;

import java.util.List;

import com.irctn.vo.ClothesCollectionVO;
import com.irctn.vo.MessageVO;
import com.irctn.vo.SchoolAdminVO;
import com.irctn.vo.SupervisorVO;

public interface SupervisorService {

//	public ClothesCollectionVO getBatchInfo(Integer batchNumber);

	public String saveSupervisor(SupervisorVO supervisorVO);

	public List<SupervisorVO> getAllSupervisorDetails();

	public ClothesCollectionVO getClothesCollectionByBatchNumber(Integer batchNumber);
	
	public SupervisorVO getSupervisorByBatchNumber(Integer batchNumber);

	public List<SchoolAdminVO> getAllSchoolAdminDetails();

	public SupervisorVO findByClothescollectionId(Integer clothesCollectionId);

	public MessageVO closeBatch(Integer batchNumber);

	public List<SupervisorVO> getClothesSortingBySupervisors();
	
	public SupervisorVO getClothesSortingBySupervisor(Integer supervisorid);

	public List<SupervisorVO> countByStatus();

	public SupervisorVO getClothesCollectionByCollectionId(Integer collectionid);

	public List<SchoolAdminVO> getSchoolContributions(Integer contributorId);

	

}
