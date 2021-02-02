package com.irctn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.ClothesCollection;

public interface ClothesCollectionRepository extends JpaRepository<ClothesCollection, Integer> {

	public ClothesCollection findByCollectionid(Integer collectionid);

	public ClothesCollection findTopByOrderByBatchnumberDesc();
	
	public ClothesCollection findByBatchnumber(Integer batchNumber);

	public List<ClothesCollection> findByContributorid(Integer contributorid);
	
	public List<ClothesCollection> findByContributoridIn(List<Integer> contributorid);

	public List<ClothesCollection> findByType(String type);

	//public List<ClothesCollection> countByTypeAndStatus(String type, Integer status);

	//public List<ClothesCollection> findTop5ByCollectionTotalWeightDesc();

	//public List<ClothesCollection> findByCollectionDateBetween(String startDate, String endDate);

	//public List<ClothesCollection> findOrderByCollectionTotalWeightDesc();

	public List<ClothesCollection> findTop10ByOrderByCollectionTotalWeightDesc();

	public List<ClothesCollection> findByTypeAndStatus(String contributorTypeSchool, Integer statusClosed);

	public List<ClothesCollection> findByCollectionDateBetween(Date startDate, Date endDate);

	public List<ClothesCollection> findTop5ByOrderByCollectionTotalWeightDesc();

	public List<ClothesCollection> findTopByOrderByCollectionTotalWeightDesc();

	//public List<ClothesCollection> findTopByOrderByCollectionTotalWeightDesc();

	//public List<ClothesCollection> findTop5ByOrderByCollectionTotalWeightDescAndType(String contributorTypeSchool);

	//public List<ClothesCollection> findByCollectionTotalWeightDesc();

	//public List<ClothesCollection> findOrderByCollectionTotalWeightDesc();

	//public List<ClothesCollection> findBycollectionDateBetween(String startDate, String endDate);
}
