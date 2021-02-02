package com.irctn.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.irctn.model.ClothesCategory;
import com.irctn.repository.ClothesCategoryRepository;
import com.irctn.service.ClothesCategoryService;
import com.irctn.service.SchoolSortingProcessService;
import com.irctn.service.SupervisorService;
import com.irctn.util.AppConstants;
import com.irctn.vo.ClothesCategoryVO;
import com.irctn.vo.MessageVO;
import com.irctn.vo.SupervisorVO;

@Service
public class ClothesCategoryServiceImpl implements ClothesCategoryService {

	@Autowired
	ClothesCategoryRepository clothesCategoryRepository;

	@Autowired
	RedisTemplate<Object, Object> redisTemplate;

	@Autowired
	SchoolSortingProcessService schoolSortingProcessService;

	@Autowired
	SupervisorService supervisorService;

	
	@Override
	public String saveClothes(ClothesCategoryVO clothesCategoryVO) {
		System.out.println("In save clothes .................." + clothesCategoryVO.getBatchType() + " -- id-->"+ clothesCategoryVO.getSchoolStudentContributionId());
		if (null == clothesCategoryVO)
			return AppConstants.FAILURE;

		if (null == clothesCategoryVO.getBatchNumber() || null == clothesCategoryVO.getBatchType()) {
			System.out.println("Invalid BatchNumber");
			return AppConstants.FAILURE;
		}
		if (AppConstants.CONTRIBUTOR_TYPE_SCHOOL.equalsIgnoreCase(clothesCategoryVO.getBatchType())
				&& null == clothesCategoryVO.getSchoolStudentContributionId()) {
			System.out.println("SchoolContribution Missing Student");
			return AppConstants.FAILURE;
		}
		if (null == clothesCategoryVO.getSortingUserId()) {
			System.out.println("Cant save Without Sorting Agent UserId");
			return AppConstants.FAILURE;
		}

		SupervisorVO supervisorVO = supervisorService.getSupervisorByBatchNumber(clothesCategoryVO.getBatchNumber());
		if (null == supervisorVO) {
			System.out.println("Invalid BatchNumber");
			return AppConstants.FAILURE;
		}

		ClothesCategory clothesCategory = clothesCategoryRepository
				.findBySortingcategoryid(clothesCategoryVO.getSortingCategoryId());
		if (null == clothesCategory) {
			clothesCategory = new ClothesCategory();
		}

		ClothesCategory saveClothesCategory = null;

		clothesCategory.setCategory1(clothesCategoryVO.getCategory1());
		clothesCategory.setCategory2(clothesCategoryVO.getCategory2());
		clothesCategory.setCategory3(clothesCategoryVO.getCategory3());
		clothesCategory.setCategory4(clothesCategoryVO.getCategory4());
		clothesCategory.setCategory5(clothesCategoryVO.getCategory5());
		clothesCategory.setCategory6(clothesCategoryVO.getCategory6());
		clothesCategory.setCategory7(clothesCategoryVO.getCategory7());
		clothesCategory.setCategory8(clothesCategoryVO.getCategory8());
		clothesCategory.setCategory9(clothesCategoryVO.getCategory9());
		clothesCategory.setCategory10(clothesCategoryVO.getCategory10());
		clothesCategory.setCategory11(clothesCategoryVO.getCategory11());
		clothesCategory.setCategory12(clothesCategoryVO.getCategory12());
		clothesCategory.setCategory13(clothesCategoryVO.getCategory13());
		clothesCategory.setCategory14(clothesCategoryVO.getCategory14());
		clothesCategory.setCategory15(clothesCategoryVO.getCategory15());
		clothesCategory.setCategory16(clothesCategoryVO.getCategory16());
		clothesCategory.setCategory17(clothesCategoryVO.getCategory17());
		clothesCategory.setCategory18(clothesCategoryVO.getCategory18());
		clothesCategory.setCategory19(clothesCategoryVO.getCategory19());
		clothesCategory.setTotalreusable(clothesCategoryVO.getTotalReusable());
		clothesCategory.setTotalwaste(clothesCategoryVO.getTotalWaste());
		clothesCategory.setNumberofitems(clothesCategoryVO.getNumberOfItems());
		saveClothesCategory = clothesCategoryRepository.save(clothesCategory);

		MessageVO result = schoolSortingProcessService.mapCategoryToSchoolSortingProcess(clothesCategoryVO,
				saveClothesCategory.getSortingcategoryid(), supervisorVO);
		return result.getMessage();

	}

	@Override
	public ClothesCategoryVO getSortingClothesById(Integer sortingCategoryId) {
		if(null == sortingCategoryId) return null;
		Object object = redisTemplate.opsForHash().get("SORTING_CATEGORY_BY_ID", sortingCategoryId);
		if (null == object) {
			ClothesCategory clothesCategory = clothesCategoryRepository.findBySortingcategoryid(sortingCategoryId);
			if (null == clothesCategory)
				return null;
			else {
				ClothesCategoryVO vo = getClothesCategoryModelToVO(clothesCategory);
				redisTemplate.opsForHash().put("SORTING_CATEGORY_BY_ID", vo.getSortingCategoryId(), vo);
				return vo;
			}
		} else {
			return (ClothesCategoryVO) object;
		}
	}

	private ClothesCategoryVO getClothesCategoryModelToVO(ClothesCategory clothesCategory) {
		if (null == clothesCategory)
			return null;
		else {
			ClothesCategoryVO clothesCategoryVO = new ClothesCategoryVO();
			clothesCategoryVO.setSortingCategoryId(clothesCategory.getSortingcategoryid());
			clothesCategoryVO.setCategory1(clothesCategory.getCategory1());
			clothesCategoryVO.setCategory2(clothesCategory.getCategory2());
			clothesCategoryVO.setCategory3(clothesCategory.getCategory3());
			clothesCategoryVO.setCategory4(clothesCategory.getCategory4());
			clothesCategoryVO.setCategory5(clothesCategory.getCategory5());
			clothesCategoryVO.setCategory6(clothesCategory.getCategory6());
			clothesCategoryVO.setCategory7(clothesCategory.getCategory7());
			clothesCategoryVO.setCategory8(clothesCategory.getCategory8());
			clothesCategoryVO.setCategory9(clothesCategory.getCategory9());
			clothesCategoryVO.setCategory10(clothesCategory.getCategory10());
			clothesCategoryVO.setCategory11(clothesCategory.getCategory11());
			clothesCategoryVO.setCategory12(clothesCategory.getCategory12());
			clothesCategoryVO.setCategory13(clothesCategory.getCategory13());
			clothesCategoryVO.setCategory14(clothesCategory.getCategory14());
			clothesCategoryVO.setCategory15(clothesCategory.getCategory15());
			clothesCategoryVO.setCategory16(clothesCategory.getCategory16());
			clothesCategoryVO.setCategory17(clothesCategory.getCategory17());
			clothesCategoryVO.setCategory18(clothesCategory.getCategory18());
			clothesCategoryVO.setCategory19(clothesCategory.getCategory19());
			clothesCategoryVO.setNumberOfItems(clothesCategory.getNumberofitems());
			clothesCategoryVO.setTotalReusable(clothesCategory.getTotalreusable());
			clothesCategoryVO.setTotalWaste(clothesCategory.getTotalwaste());

			return clothesCategoryVO;
		}
	}

	private List<ClothesCategoryVO> getVOListFromModel(List<ClothesCategory> clothesCategoryList){
		
		List<ClothesCategoryVO> list = new ArrayList<ClothesCategoryVO>();
		for(ClothesCategory clothesCategory :clothesCategoryList){
			list.add(getClothesCategoryModelToVO(clothesCategory));
		}
		return list;
	}
	
	
	@Override
	public List<ClothesCategoryVO> getSchoolContribedClothes() {
		
		return getVOListFromModel(clothesCategoryRepository.findAll());
	}

}
