package com.irctn.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irctn.model.RetailerCategory;
import com.irctn.repository.RetailerCategoryRepository;
import com.irctn.service.RetailerCategoryService;

@Service
public class RetailerCategoryServiceImpl implements RetailerCategoryService {

	@Autowired
	RetailerCategoryRepository retailerCategoryRepository;

	@Override
	public RetailerCategory getRetailerCategoryByName(String categoryName) {
		return retailerCategoryRepository.findByCategorynameIgnoreCase(categoryName);
	}
	

}
