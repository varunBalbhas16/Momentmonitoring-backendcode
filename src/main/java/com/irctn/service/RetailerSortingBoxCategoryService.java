package com.irctn.service;

import java.util.List;

import com.irctn.vo.MessageVO;
import com.irctn.vo.RetailerSortingBoxDetailVO;
import com.irctn.vo.RetailerSortingBoxVO;

public interface RetailerSortingBoxCategoryService {

	public MessageVO saveRetailerSortingBoxCategory(Integer retailersortingboxid,
			RetailerSortingBoxVO retailerCategoryVO);

	public List<RetailerSortingBoxDetailVO> getRetailerCategoryByRetailerBoxId(Integer retailerSortingBoxId);

	//public RetailerSortingBoxCategory saveRetailerSortingBoxCategory(RetailerSortingBox saveRetailerSortingBox,
		//	RetailerCategoryVO retailerCategoryVO);

	//public List<RetailerSortingBoxCategory> getRetailerSortingBoxInfo(Integer retailersortingboxid);
	
	public List<RetailerSortingBoxDetailVO> getRetailerContributedClothesDetails();

}
