package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.RetailerSortingBoxCategory;

public interface RetailerSortingBoxCategoryRepository extends JpaRepository<RetailerSortingBoxCategory, Integer> {

	//public RetailerSortingBoxCategory findByRetailersortingboxid(Integer retailersortingboxid);

	public List<RetailerSortingBoxCategory> findByRetailersortingboxid(Integer retailersortingboxid);

}
