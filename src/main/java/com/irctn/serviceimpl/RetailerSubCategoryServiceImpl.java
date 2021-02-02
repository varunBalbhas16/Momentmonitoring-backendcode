package com.irctn.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irctn.model.RetailerSubCategory;
import com.irctn.repository.RetailerSubCategoryRepository;
import com.irctn.service.RetailerSubCategoryService;

@Service
public class RetailerSubCategoryServiceImpl implements RetailerSubCategoryService {
	
	@Autowired
	RetailerSubCategoryRepository retailerSubCategoryRepository;

	@Override
	public RetailerSubCategory getRetailerSubCategoryByName(String retailerSubCategoryName) {
		 
		return retailerSubCategoryRepository.findByRetailersubcategoryname(retailerSubCategoryName);
	}

}
