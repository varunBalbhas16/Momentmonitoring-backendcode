package com.irctn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.ClothesCategory;

public interface ClothesCategoryRepository extends JpaRepository<ClothesCategory, Integer> {

	public ClothesCategory findBySortingcategoryid(Integer sortingCategoryId);

}
