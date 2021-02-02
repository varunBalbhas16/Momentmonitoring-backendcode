package com.irctn.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.irctn.service.ClothesCollectionService;
import com.irctn.service.ContributorService;
import com.irctn.vo.ClothesCollectionVO;

@RestController
@RequestMapping(value = "/api/home")
public class RetailerController {

	@Autowired
	ContributorService contributorService;

	@Autowired
	ClothesCollectionService clothesCollectionService;

	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.controller.RetailerController");

	@CrossOrigin
	@GetMapping("/getAllRetailerDetails")
	public List<ClothesCollectionVO> getAllRetailerDetailsByClothes() {
		return clothesCollectionService.getAllRetailerContributions();

	}

	@CrossOrigin
	@GetMapping("/getRetailerDetailsById/{id}")
	public ClothesCollectionVO getAllRetailerDetailsByCollection(@PathVariable Integer id) {
		return clothesCollectionService.getRetailerContributionForBatchByCollectionId(id);

	}

}
