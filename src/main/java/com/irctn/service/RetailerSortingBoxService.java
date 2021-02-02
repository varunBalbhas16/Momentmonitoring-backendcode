package com.irctn.service;

import java.util.List;

import com.irctn.vo.MessageVO;
import com.irctn.vo.RetailerSortingBoxVO;

public interface RetailerSortingBoxService {

	public MessageVO saveRetailerCategoryProcess(RetailerSortingBoxVO retailerCategoryVO);

	public MessageVO saveRetailerSortingBoxInfo(RetailerSortingBoxVO retailerSortingBoxVO);

	public List<RetailerSortingBoxVO> getRetailerSortingBoxByClothesSortingId(Integer clothessortingid);

	//public String saveRetailerSortingProcessId(Integer retailersortingprocessid);

}
