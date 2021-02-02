package com.irctn.service;

import java.util.List;

import com.irctn.vo.ClothesCategoryVO;

public interface ClothesCategoryService {

	public String saveClothes(ClothesCategoryVO clothesCategoryVO);

	public ClothesCategoryVO getSortingClothesById(Integer sortingCategoryId);

	public List<ClothesCategoryVO> getSchoolContribedClothes();
	
}
