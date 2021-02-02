package com.irctn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.RetailerCategory;

public interface RetailerCategoryRepository extends JpaRepository<RetailerCategory, Integer> {

	public RetailerCategory findByCategorynameIgnoreCase(String categoryname);

}
