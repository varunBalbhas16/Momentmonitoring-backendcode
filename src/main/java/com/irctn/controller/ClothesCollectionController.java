package com.irctn.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.irctn.repository.ClothesCollectionRepository;
import com.irctn.service.ClothesCollectionService;
import com.irctn.service.SchoolProgramMappingService;
import com.irctn.util.AppConstants;
import com.irctn.vo.ClothesCollectionVO;
import com.irctn.vo.MessageVO;
import com.irctn.vo.SchoolProgramMappingVO;

@RestController
@RequestMapping(value = "/api/home")
public class ClothesCollectionController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger("com.irctn.controller.ClothesCollectionController");

	@Autowired
	ClothesCollectionService clothesCollectionService;

	@Autowired
	ClothesCollectionRepository clothesCollectionRepository;

	@Autowired
	SchoolProgramMappingService schoolProgramMappingService;

	@CrossOrigin
	@RequestMapping(value = "/saveClothesCollection", method = RequestMethod.POST)
	public MessageVO saveClothesCollection(@RequestBody ClothesCollectionVO clothesCollectionVO) {
		String clothesCollection = clothesCollectionService.saveClothesCollection(clothesCollectionVO);

		if ("success".equals(clothesCollection)) {
			System.out.println("saved Successfully");
			return new MessageVO("saved successfully", "Clothes Collection Saved", clothesCollectionVO.getType());
		} else if ("updatesuccess".equals(clothesCollection)) {
			System.out.println("updated successfully");
			return new MessageVO("updated successfully", "Clothes Collection Updated", clothesCollectionVO.getType());
		} else {
			return new MessageVO("Failure to Save", "Contact Admin");
		}

	}

	@CrossOrigin
	@RequestMapping(value = "/getSchoolHistory", method = RequestMethod.GET)
	public List<ClothesCollectionVO> getAllSchoolDetails() {
		return clothesCollectionService.getCollectionsByAllSchools();
	}

	@CrossOrigin
	@RequestMapping(value = "/getRetailerHistory", method = RequestMethod.GET)
	public List<ClothesCollectionVO> getAllRetailerDetails() {
		return clothesCollectionService.getCollectionsByAllRetailers();
	}

	@CrossOrigin
	@RequestMapping(value = "/getSchoolContributorNames/{search}", method = RequestMethod.GET)
	public List<ClothesCollectionVO> getAllSchoolContributor(@PathVariable String search) {
		return clothesCollectionService.searchSchoolsWithContribution(search);
	}

	@CrossOrigin
	@RequestMapping(value = "/getRetailerContributorNames/{search}", method = RequestMethod.GET)
	public List<ClothesCollectionVO> getAllRetailerContributor(@PathVariable String search) {
		return clothesCollectionService.searchRetailersWithContribution(search);
	}

	@CrossOrigin
	@GetMapping("/getProgramsByContributors/{id}")
	public List<SchoolProgramMappingVO> getAllProgramsByContributors(@PathVariable Integer id) {
		return schoolProgramMappingService.getAllProgramsByContributors(id);

	}

	@CrossOrigin
	@RequestMapping(value = "/getBatchNumberByInfo/{batchNumber}", method = RequestMethod.GET)
	public ClothesCollectionVO getBatchDetails(@PathVariable Integer batchNumber) {

		return clothesCollectionService.getBatchInfo(batchNumber);

	}
	
	@CrossOrigin
	@GetMapping("getStatusByBatchNumber/{batchNumber}")
	public MessageVO getStatusByBatchNumber(@PathVariable Integer batchNumber){	 
		String message = clothesCollectionService.getStatusByBatchNumber(batchNumber);
		
		if("failure".equalsIgnoreCase(message)){
			return new MessageVO("failure","Batch Already Closed Cannot Start Again","contact Supervisor");
		}else{
			return new MessageVO("success","Batch Not Closed","Start the Batch");

		}
				
	}
	
	@CrossOrigin
	@GetMapping("getMonthlyContributons")
	public List<ClothesCollectionVO> getMonthlyContributions(){
		return clothesCollectionService.getMonthlyContributionByTypeAndStatus(AppConstants.CONTRIBUTOR_TYPE_SCHOOL,AppConstants.STATUS_CLOSED);
		
	}
	

}
