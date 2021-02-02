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

import com.irctn.service.ClothesCollectionService;
import com.irctn.service.SupervisorService;
import com.irctn.vo.ClothesCollectionVO;
import com.irctn.vo.MessageVO;
import com.irctn.vo.SchoolSortingAgentDetailsVO;
import com.irctn.vo.SupervisorVO;

@RestController
@RequestMapping(value = "/api/home")
public class SupervisorController {

	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.controller.SupervisorController");

	@Autowired
	SupervisorService supervisorService;

	@Autowired
	ClothesCollectionService clothesCollectionService;

	@CrossOrigin
	@RequestMapping(value = "/saveSupervisor", method = RequestMethod.POST)
	public MessageVO saveSupervisor(@RequestBody SupervisorVO supervisorVO) {

		String supervisor = supervisorService.saveSupervisor(supervisorVO);
		if ("success".equals(supervisor)) {
			System.out.println("Supervisor saved Successfully");
			return new MessageVO("Saved Successfully", "Supervisor Details Saved", supervisorVO.getName() );
		} else if ("updatesuccess".equals(supervisor)) {
			System.out.println("Updated Supervisor Details Successfully");
			return new MessageVO("Updated Successfully", "Supervisor Details Updated", supervisorVO.getName());
		} else {
			return new MessageVO("Failure to Save", "Contact Admin");
		}

	}

	@CrossOrigin
	@RequestMapping(value = "/getAllSupervisorDetails", method = RequestMethod.GET)
	public List<SupervisorVO> getAllSupervisorDetails() {
		return supervisorService.getAllSupervisorDetails();

	}

	@CrossOrigin
	@RequestMapping(value = "/getBatchNumberBySupervisorDetails/{batchNumber}", method = RequestMethod.GET)
	public ClothesCollectionVO getBatchNumberBySupervisor(@PathVariable Integer batchNumber) {

		return supervisorService.getClothesCollectionByBatchNumber(batchNumber);

	}

	@CrossOrigin
	@GetMapping("/closeBatch/{batchNumber}")
	public MessageVO closeBatchByWeightInfo(@PathVariable Integer batchNumber) {
		return supervisorService.closeBatch(batchNumber);

	}
	
	@CrossOrigin
	@GetMapping("/getAllSortingSupervisor")
	public List<SupervisorVO> getClothesSortingBySupervisor(){
		return supervisorService.getClothesSortingBySupervisors();
		
	}
	
	

}