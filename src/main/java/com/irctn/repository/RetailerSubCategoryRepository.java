package com.irctn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.RetailerSubCategory;

public interface RetailerSubCategoryRepository extends JpaRepository<RetailerSubCategory, Integer> {

	public RetailerSubCategory findByRetailersubcategoryname(String retailersubcategoryname);

}
