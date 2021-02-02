package com.irctn.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irctn.model.RetailerSortingBox;
import com.irctn.repository.RetailerSortingBoxRepository;
import com.irctn.service.RetailerSortingBoxCategoryService;
import com.irctn.service.RetailerSortingBoxService;
import com.irctn.vo.MessageVO;
import com.irctn.vo.RetailerSortingBoxVO;

@Service
public class RetailerSortingBoxServiceImpl implements RetailerSortingBoxService {

	@Autowired
	RetailerSortingBoxRepository retailerSortingBoxRepository;

	@Autowired
	RetailerSortingBoxCategoryService retailerSortingBoxCategoryService;

	@Override
	public synchronized MessageVO saveRetailerCategoryProcess(RetailerSortingBoxVO retailerSortingBoxVO) {

		if (null == retailerSortingBoxVO) {
			System.out.println("-------------> retailerCategoryVO cannot be Null");
			return new MessageVO("failure", "RetailerCategoryVO Cannot be Null", "failure");
		}

		if (null == retailerSortingBoxVO.getBatchNumber() && null == retailerSortingBoxVO.getBatchType()) {
			System.out.println("--------------> Batch Number and Batch Type Cannot be Null");
			return new MessageVO("failure", "Batch Number and Batch type Not Found", "failure");
		}
		if (null == retailerSortingBoxVO.getClothesSortingprocessId()) {
			System.out.println("----------------> Retailer Sortng ProcessId cannot be Null");
			return new MessageVO("failure", "RetailerSortingprocessId Cannot be Null", "failure");
		}
		if (null == retailerSortingBoxVO.getBoxNumber()) {
			System.out.println("----------------> Box Number cannot be Null");
			return new MessageVO("failure", "RetailerSortingprocessId Cannot be Null", "failure");
		}

		RetailerSortingBox box = retailerSortingBoxRepository.findByClothessortingprocessidAndBoxnumber(
				retailerSortingBoxVO.getClothesSortingprocessId(), retailerSortingBoxVO.getBoxNumber());

		MessageVO saveRetailerSortingBoxCategory = retailerSortingBoxCategoryService
				.saveRetailerSortingBoxCategory(box.getRetailersortingboxid(), retailerSortingBoxVO);
		
		if ("success".equalsIgnoreCase(saveRetailerSortingBoxCategory.getMessage())) {
			
			return new MessageVO("success", "successfully saved RetailerSortingBox Category",saveRetailerSortingBoxCategory.getDescription() );
		} else {
			return new MessageVO("failure", "failed to save RetailerSortingBoxCategory", "failure");
		}
	}

	@Override
	public MessageVO saveRetailerSortingBoxInfo(RetailerSortingBoxVO retailerSortingBoxVO) {

		if (null == retailerSortingBoxVO) {
			System.out.println("-------------------> RetailerSortingVo cannot be Null");
			return new MessageVO("failure", "RetailerSortingBox Cannot be Null", "failure");
		}
		
		if(null == retailerSortingBoxVO.getBatchNumber() || null == retailerSortingBoxVO.getBatchType()){
			System.out.println("------------------> Batch Number And Batch Type Cannot be Null");
			return new MessageVO("failure", "BatchNumber And BatchType Cannot be Null", "failure");
		}

		if (null == retailerSortingBoxVO.getClothesSortingprocessId()) {
			System.out.println("---------------------------------> Clothes SortingProcessId cannot be Null");
			return new MessageVO("failure", "ClothesSortingprocessId Cannot be Null", "failure");
		}
		if(null == retailerSortingBoxVO.getSortingUserId()){
			System.out.println("--------------------------> Sorting User Id Cannot be Null");
			return new MessageVO("failure", "SortingUserId Cannot be Null", "failure");
		}
		if(null != retailerSortingBoxVO.getRetailerSortingBoxId()){
			System.out.println("----------------------------> Cannot Update");
			return new MessageVO("failure", "RetailerSortingBoxId Cannot be Update", "failure");
		}

		RetailerSortingBox retailerSortingBox = retailerSortingBoxRepository
				.findByRetailersortingboxid(retailerSortingBoxVO.getRetailerSortingBoxId());
		
		if(null == retailerSortingBox){
			retailerSortingBox = new RetailerSortingBox();
		}else{
			return new MessageVO("failure", "RetailerSortingBoxId Cannot be Update", "failure");
		}
		RetailerSortingBox boxNumber = retailerSortingBoxRepository.findByBoxnumber( retailerSortingBoxVO.getBoxNumber());
		if(null == boxNumber){
			System.out.println("Box Number is Not in First Time");
			retailerSortingBox.setBoxweight(retailerSortingBoxVO.getBoxWeight());
			retailerSortingBox.setBoxnumber(retailerSortingBoxVO.getBoxNumber());
			retailerSortingBox.setClothessortingprocessid(retailerSortingBoxVO.getClothesSortingprocessId());
			retailerSortingBox.setSortingagentid(retailerSortingBoxVO.getSortingUserId());
			retailerSortingBoxRepository.save(retailerSortingBox);
		}else{
			return new MessageVO("failure", "BoxNumber Already Exist", "failure");
		}
		
		
		return new MessageVO("success","successfully saved","saved successfully" );
	}

	@Override
	public List<RetailerSortingBoxVO> getRetailerSortingBoxByClothesSortingId(Integer clothessortingid) {
		
		return getVOListFromModel(retailerSortingBoxRepository.findByClothessortingprocessid(clothessortingid));
	}
	
private RetailerSortingBoxVO getVOFromModel(RetailerSortingBox retailerSortingBox){
	if(null == retailerSortingBox)
		return null;
	RetailerSortingBoxVO vo = new RetailerSortingBoxVO();
	vo.setBoxNumber(retailerSortingBox.getBoxnumber());
	vo.setBoxWeight(retailerSortingBox.getBoxweight());
	vo.setClothesSortingprocessId(retailerSortingBox.getClothessortingprocessid());
	vo.setRetailerSortingBoxId(retailerSortingBox.getRetailersortingboxid());
	vo.setSortingUserId(retailerSortingBox.getSortingagentid());
	
	return vo;
	
}

private List<RetailerSortingBoxVO> getVOListFromModel(List<RetailerSortingBox> retailerSortingBox){
	if(null == retailerSortingBox)return null;
	List<RetailerSortingBoxVO> list = new ArrayList<RetailerSortingBoxVO>();
	retailerSortingBox.forEach(boxInfo ->{
		list.add(getVOFromModel(boxInfo));
		
	});
	return list;
	
}
}
