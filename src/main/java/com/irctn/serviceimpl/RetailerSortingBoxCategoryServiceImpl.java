package com.irctn.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irctn.model.RetailerCategory;
import com.irctn.model.RetailerSortingBoxCategory;
import com.irctn.model.RetailerSubCategory;
import com.irctn.repository.RetailerSortingBoxCategoryRepository;
import com.irctn.service.RetailerCategoryService;
import com.irctn.service.RetailerSortingBoxCategoryService;
import com.irctn.service.RetailerSubCategoryService;
import com.irctn.vo.MessageVO;
import com.irctn.vo.RetailerSortingBoxDetailVO;
import com.irctn.vo.RetailerSortingBoxVO;

@Service
public class RetailerSortingBoxCategoryServiceImpl implements RetailerSortingBoxCategoryService {

	@Autowired
	RetailerSortingBoxCategoryRepository retailerSortingBoxCategoryRepository;
	
	@Autowired
	RetailerCategoryService retailerCategoryService;
	
	@Autowired
	RetailerSubCategoryService retailerSubCategoryService;

	@Override
	public MessageVO saveRetailerSortingBoxCategory(Integer retailersortingboxid,
			RetailerSortingBoxVO retailerSortingBoxVO) {
		Double overallWeight = 0.0d;
		
		//first fill in all the category id and sub category id
		List<RetailerSortingBoxDetailVO> boxDetailsList = retailerSortingBoxVO.getSortingCategoryDetailsList();
		for(RetailerSortingBoxDetailVO vo : boxDetailsList){
			
			RetailerCategory category = retailerCategoryService.getRetailerCategoryByName(vo.getCategoryName());
			if(null == category){
				return new MessageVO("failure","Invalid Category Name found in request. Please correct it.","failure");
			}
			System.out.println("category.getRetailercategoryid()---------------------------->" + category.getRetailercategoryid());
			vo.setRetailerCategoryId(category.getRetailercategoryid());
			
			RetailerSubCategory subCategory = retailerSubCategoryService.getRetailerSubCategoryByName(vo.getRetailerSubCategoryName());
			if(null == subCategory) {
				return new MessageVO("failure","Invalid Sub-Category Name found in request. Please correct it.","failure");
			}
			System.out.println("category.getRetailersubcategoryid()---------------------------->" + subCategory.getRetailersubcategoryid());
			vo.setRetailerSubcategoryId(subCategory.getRetailersubcategoryid());
		}
		
		for(RetailerSortingBoxDetailVO vo : boxDetailsList){
			RetailerSortingBoxCategory sortingBoxCategoryDetail = new RetailerSortingBoxCategory();			
			sortingBoxCategoryDetail.setRetailersortingboxid(retailersortingboxid);
			sortingBoxCategoryDetail.setRetailercategoryid(vo.getRetailerCategoryId());
			sortingBoxCategoryDetail.setRetailersubcategoryid(vo.getRetailerSubcategoryId());
			sortingBoxCategoryDetail.setItems(vo.getItems());
			sortingBoxCategoryDetail.setWeight(vo.getWeight());
			
			RetailerSortingBoxCategory saveRetailerSortingBoxCategory = retailerSortingBoxCategoryRepository.save(sortingBoxCategoryDetail);
			overallWeight = overallWeight + saveRetailerSortingBoxCategory.getWeight();
			retailerSortingBoxVO.setTotalWeight(overallWeight);
			
		}
		String weight =""+ retailerSortingBoxVO.getTotalWeight();
		
		return new MessageVO("success","Successfully saved",weight);
	}

	private RetailerSortingBoxDetailVO getVOFromModel(RetailerSortingBoxCategory retailerSortingBoxCategory ){
		if(null == retailerSortingBoxCategory)return null;
		RetailerSortingBoxDetailVO vo = new RetailerSortingBoxDetailVO();
		vo.setRetailerCategoryId(retailerSortingBoxCategory.getRetailercategoryid());
		vo.setRetailerSortingBoxCategoryId(retailerSortingBoxCategory.getRetailersortingboxcategoryid());
		vo.setRetailerSubcategoryId(retailerSortingBoxCategory.getRetailersubcategoryid());
		vo.setWeight(retailerSortingBoxCategory.getWeight());
		vo.setItems(retailerSortingBoxCategory.getItems());
		vo.setRetailerSortingBoxId(retailerSortingBoxCategory.getRetailersortingboxid());
		return vo;
		
	}
	
	private List<RetailerSortingBoxDetailVO> getVOListFromModel(List<RetailerSortingBoxCategory> retailerSortingBoxCategoryList){
		List<RetailerSortingBoxDetailVO> list = new ArrayList<RetailerSortingBoxDetailVO>();
		for(RetailerSortingBoxCategory retailerSortingBoxCategory : retailerSortingBoxCategoryList){
			list.add(getVOFromModel(retailerSortingBoxCategory));
		}
		return list;
		
	}

	@Override
	public List<RetailerSortingBoxDetailVO> getRetailerCategoryByRetailerBoxId(Integer retailerSortingBoxId) {
		
		return getVOListFromModel(retailerSortingBoxCategoryRepository.findByRetailersortingboxid(retailerSortingBoxId));
	}

	@Override
	public List<RetailerSortingBoxDetailVO> getRetailerContributedClothesDetails() {
		
		return getVOListFromModel(retailerSortingBoxCategoryRepository.findAll());
	}

}
