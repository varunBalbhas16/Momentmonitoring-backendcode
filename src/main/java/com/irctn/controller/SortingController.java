package com.irctn.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.irctn.service.ClothesCategoryService;
import com.irctn.service.ClothesCollectionService;
import com.irctn.service.MapDigitalScaleService;
import com.irctn.service.RetailerSortingBoxService;
import com.irctn.service.SchoolSortingProcessService;
import com.irctn.service.SchoolStudentContributionService;
import com.irctn.service.StudentService;
import com.irctn.vo.ClothesCategoryVO;
import com.irctn.vo.ClothesCollectionVO;
import com.irctn.vo.MapDigitalScaleVO;
import com.irctn.vo.MessageVO;
import com.irctn.vo.RetailerSortingBoxVO;
import com.irctn.vo.SchoolSortingAgentDetailsVO;
import com.irctn.vo.SchoolSortingVO;
import com.irctn.vo.SchoolStudentContributionVO;
import com.irctn.vo.StudentVO;

@RestController
@RequestMapping(value = "/api/home")
public class SortingController {

	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.controller.SortingController");

	@Autowired
	MapDigitalScaleService mapDigitalScaleService;

	@Autowired
	ClothesCategoryService clothesCategoryService;

	@Autowired
	SchoolStudentContributionService schoolStudentContributionService;

	@Autowired
	SchoolSortingProcessService schoolSortingProcessService;

	@Autowired
	ClothesCollectionService clothesCollectionService;

	@Autowired
	StudentService studentService;
	
	@Autowired
	RetailerSortingBoxService retailerSortingBoxService;

	@CrossOrigin
	@RequestMapping(value = "/saveDigitalScaleNumber", method = RequestMethod.POST)
	public MessageVO saveDigitalScaleNumber(@RequestBody MapDigitalScaleVO mapDigitalScaleVO) {

		String mapDigitalScale = mapDigitalScaleService.saveDigitalScaleNumber(mapDigitalScaleVO);

		if ("success".equals(mapDigitalScale)) {
			System.out.println("saved Successfully");
			return new MessageVO("success", "savedSuccessfully");
		} else {
			return new MessageVO("failure", "Failure to Save", "Contact Supervisor");
		}
	}

	@CrossOrigin
	@RequestMapping(value = "/getStudentDetails/{id}", method = RequestMethod.GET)
	public SchoolStudentContributionVO getStudentDetailsById(@PathVariable Integer id) {
		return schoolStudentContributionService.getSchoolStudentContributionById(id);

	}

	@CrossOrigin
	@RequestMapping(value = "/saveClothesDetails", method = RequestMethod.POST)
	public MessageVO saveClothes(@RequestBody ClothesCategoryVO clothesCategoryVO) {
		System.out.println("Saving Clothes controller --------------> "+ clothesCategoryVO.toString());
		String saveClothes = clothesCategoryService.saveClothes(clothesCategoryVO);
		if ("success".equals(saveClothes)) {
			System.out.println("Clothes Saved Successfully");
			return new MessageVO("success", "saved successfully", clothesCategoryVO.getTotalReusable(),
					clothesCategoryVO.getTotalWaste());
		} else if ("updatesuccess".equals(saveClothes)) {
			System.out.println("Updated Successfully");
			return new MessageVO("update success", "UpdatedSuccessfully", clothesCategoryVO.getTotalReusable(),
					clothesCategoryVO.getTotalWaste());

		} else {
			return new MessageVO("failure", "save Operation Failed", "Contact Admin");
		}
	}

	@CrossOrigin
	@RequestMapping(value = "/saveSortingDetails", method = RequestMethod.POST)
	public MessageVO saveSortingProcess(@RequestBody SchoolSortingVO schoolSortingVO) {

		System.out.println("In Sorting Controller saveSortingProces()...................." + schoolSortingVO.toString());
		String saveSchoolSortingProcess = schoolSortingProcessService.saveSchoolSortingProcess(schoolSortingVO);
		if ("success".equals(saveSchoolSortingProcess)) {
			System.out.println("saved Successfully --------->" +schoolSortingVO.getSchoolStudentContributionId());
			return new MessageVO("success", "save Operation Successfull","Success",schoolSortingVO.getSchoolStudentContributionId(),"success");
		} else if ("updatesuccess".equals(saveSchoolSortingProcess)) {
			System.out.println("updated Successfull");
			return new MessageVO("updatesuccess", "updated Successfully");

		} else {
			return new MessageVO("failure", "save Operation Failed", "contact Admin");
		}
	}


	@CrossOrigin
	@RequestMapping(value = "/getClothesCollectionByBatchNumber/{batchNumber}", method = RequestMethod.GET)
	public ClothesCollectionVO getClothesCollectionByBatchNumber(@PathVariable Integer batchNumber) {
		return clothesCollectionService.getClothesCollectionDetailsByBatchNumber(batchNumber);

	}

	@CrossOrigin
	@GetMapping("getAllStudentsByName/{name}/{contributorid}")
	public List<StudentVO> getAllStudentsByName(@PathVariable String name,@PathVariable Integer contributorid ) {
		return studentService.getAllStudentByName(name,contributorid);
	}
	
	@CrossOrigin
	@GetMapping("getStudentsById/{id}")
	public StudentVO getStudentById(@PathVariable Integer id){
		return studentService.getStudentById(id);
		
	}
	
	@CrossOrigin
	@GetMapping("getWeight")
	public MessageVO getWeight(){
		
		MessageVO weight = mapDigitalScaleService.getWeightInfo();
		
		if("success".equalsIgnoreCase(weight.getMessage())){
			return new MessageVO("success","random number Generated successfully",weight.getDescription());
		}else{
			return new MessageVO("failure","cannot generate Random numbers","failures");
		}
		
		
	}
	
	@CrossOrigin
	@PostMapping("saveRetailerCategoryProcess")
	public MessageVO saveRetailerCategoryProcess(@RequestBody RetailerSortingBoxVO retailerCategoryVO){
		
		MessageVO message = retailerSortingBoxService.saveRetailerCategoryProcess(retailerCategoryVO);
		if("success".equals(message.getMessage())){
			return new MessageVO("success","RetailerSortingProcess Saved",message.getDescription());
		}else{
			return new MessageVO("failure","failed to save RetailerSortingProcess","failure");
		}
		
		
	}
	
	@CrossOrigin
	@PostMapping("saveRetailerSortingBoxDetails")
	public MessageVO saveRetailerSortingBoxDetails(@RequestBody RetailerSortingBoxVO retailerSortingBoxVO){
		MessageVO message = retailerSortingBoxService.saveRetailerSortingBoxInfo(retailerSortingBoxVO);
		if("success".equalsIgnoreCase(message.getMessage())){
			return new MessageVO("success","saved Successfully","success");
		}else{
			return new MessageVO("failure","failed to save","failure");
		}
		
	}
	
	@CrossOrigin
	@GetMapping("getWeightByUser/{id}")
	public MessageVO getWeight(@PathVariable Integer id){
		
		Double weight = mapDigitalScaleService.getScaleWeightByUser(id);
		return new MessageVO("success","random number Generated successfully", ""+weight.doubleValue());
		/*
		if("success".equalsIgnoreCase(weight.getMessage())){
			return new MessageVO("success","random number Generated successfully", ""+weight.doubleValue());
		}else{
			return new MessageVO("failure","cannot generate Random numbers","failures");
		}
		*/
	}
	
	@CrossOrigin
	@GetMapping("/getSchoolSortingAgentsListDetails")
	public List<SchoolSortingAgentDetailsVO> geSchoolSortingAgentsStats(){
		return schoolSortingProcessService.getSortingAgentDetails();
		
	}
}
