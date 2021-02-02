package com.irctn.service;

import java.util.List;

import com.irctn.vo.ClothesCollectionVO;

public interface ClothesCollectionService {

	public String saveClothesCollection(ClothesCollectionVO clothesCollectionVO);

	public List<ClothesCollectionVO> getCollectionsByAllSchools();

	public List<ClothesCollectionVO> getCollectionsByAllRetailers();

//	public List<ClothesCollectionVO> getAllPrograms(String search);

	public ClothesCollectionVO getBatchInfo(Integer batchNumber);

	public List<ClothesCollectionVO> searchSchoolsWithContribution(String search);

	public List<ClothesCollectionVO> searchRetailersWithContribution(String search);

//	public ClothesCollection getBatchNumberByDetails(Integer batchNumber);

	public List<ClothesCollectionVO> getAllRetailerContributions();

	public ClothesCollectionVO getRetailerContributionForBatchByCollectionId(Integer id);

	public ClothesCollectionVO getClothesCollectionDetailsByBatchNumber(Integer batchNumber);

	//public MessageVO getClothesCollectionByBatchNumber(Integer batchNumber);

	public ClothesCollectionVO getClothesCollectionServiceById(Integer collectionId);

	public String getStatusByBatchNumber(Integer batchNumber);

	public List<ClothesCollectionVO> getTypeAndStatus(String contributorTypeSchool, Integer statusClosed);
	
	public List<ClothesCollectionVO> getMonthlyContributionByTypeAndStatus(String contributorTypeSchool, Integer statusClosed);

	//public List<ClothesCollectionVO> getTopCollectionTotalWeightByContributors();
	
	public List<ClothesCollectionVO> getTopCollectionTotalWeightBySchools();

	public List<ClothesCollectionVO> getTopCollectionTotalWeightByRetailers();


      public List<ClothesCollectionVO> getPreviousOneMonthContribution(String contributorTypeSchool,
			Integer statusClosed);
      
      public List<ClothesCollectionVO> getPreviousTwoMonthContribution(String contributorTypeSchool,
  			Integer statusClosed);
      
      public List<ClothesCollectionVO>   getRetailerCurrenteMonthContribution();
      
      public List<ClothesCollectionVO> getRetailerPreviousOneMonthContribution(
  			);
        
        public List<ClothesCollectionVO> getRetailerPreviousTwoMonthContribution(
    			);


	public List<ClothesCollectionVO> getTopCollectionTotalWeightByContributorType(String contributorTypeSchool);
	
	public List<ClothesCollectionVO> getContributorByTypeAndStatus(String contributorTypeSchool, Integer statusClosed);
	
	public List<ClothesCollectionVO> getByType(String contributorTypeSchool);
	
	public List<ClothesCollectionVO> getByContributor(Integer contributorId);

	//public List<ClothesCollectionVO> getType(String contributorTypeSchool);

	//public List<ClothesCollectionVO> getAllPrograms(String search);





	

}
