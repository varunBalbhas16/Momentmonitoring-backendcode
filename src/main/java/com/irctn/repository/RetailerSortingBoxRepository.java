package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.RetailerSortingBox;

public interface RetailerSortingBoxRepository extends JpaRepository<RetailerSortingBox, Integer> {

	public List<RetailerSortingBox> findByClothessortingprocessid(Integer clothessortingprocessid); //This is wrong. For one process id you will have 1 to many boxes.
	
	public RetailerSortingBox findByClothessortingprocessidAndBoxnumber(Integer clothessortingprocessid, Integer boxnumber);

	public RetailerSortingBox findTopByOrderByBoxnumberDesc();
	
	//public RetailerSortingBox findTopByOrderByRetailersortingprocessidDesc(Integer retailersortingprocessid);

	public RetailerSortingBox findByRetailersortingboxid(Integer retailersortingboxid);

	public RetailerSortingBox findByBoxnumber(Integer boxnumber);

}
