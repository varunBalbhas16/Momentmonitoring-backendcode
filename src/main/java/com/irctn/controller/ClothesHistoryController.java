package com.irctn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.irctn.service.AdminService;
import com.irctn.service.ClothesCategoryService;
import com.irctn.service.ContributorService;
import com.irctn.service.OrderService;
import com.irctn.service.ProgramService;
import com.irctn.service.SchoolProgramMappingService;
import com.irctn.service.SchoolSortingProcessService;
import com.irctn.service.UserService;
import com.irctn.vo.MessageVO;
import com.irctn.vo.OrderVO;
import com.irctn.vo.RetailerContributedClothesVO;
import com.irctn.vo.SchoolContributedClothesVO;

@RestController
@RequestMapping(value = "/api/home")
public class ClothesHistoryController {

	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.controller.ClothesHistoryController");

	@Autowired
	ContributorService contributorService;

	@Autowired
	UserService userService;

	@Autowired
	ProgramService programService;

	@Autowired
	AdminService adminService;

	@Autowired
	SchoolProgramMappingService schoolProgramMappingService;

	@Autowired
	SchoolSortingProcessService schoolSortingProcessService;

	@Autowired
	ClothesCategoryService clothesCategoryService;

	@Autowired
	OrderService orderService;

	@CrossOrigin
	@GetMapping("getSchoolContributedClothes")
	public SchoolContributedClothesVO getSchoolContributedClothes() {
		return adminService.getSchoolContributedClothes();
	}

	@CrossOrigin
	@GetMapping("getRetailerContributedClothes")
	public RetailerContributedClothesVO getRetailerContributedClothes() {
		return adminService.getRetailerContributedClothes();
	}

	@CrossOrigin
	@PostMapping("saveOrderDetails")
	public MessageVO saveOrderDetails(@RequestBody OrderVO orderVO) {
		String message = orderService.saveOrderDetails(orderVO);
		if ("success".equalsIgnoreCase(message)) {
			return new MessageVO("success", "Orders Successfully Saved", "saved");
		} else {
			return new MessageVO("Failure", "Failed to Save", "Contact Admin");
		}

	}

}
