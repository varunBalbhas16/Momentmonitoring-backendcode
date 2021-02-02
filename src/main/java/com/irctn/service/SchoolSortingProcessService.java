package com.irctn.service;

import java.util.List;

import com.irctn.vo.ClothesCategoryVO;
import com.irctn.vo.MessageVO;
import com.irctn.vo.SchoolSortingAgentDetailsVO;
import com.irctn.vo.SchoolSortingVO;
import com.irctn.vo.SupervisorVO;

public interface SchoolSortingProcessService {

	public String saveSchoolSortingProcess(SchoolSortingVO schoolSortingVO);

	public SchoolSortingVO getSchoolSortingProcessById(Integer schoolSortingProcessId);
	
	public List<SchoolSortingVO> getAllSortingProcessBySchoolCollectionId(Integer clothesSortingId);

	public MessageVO mapCategoryToSchoolSortingProcess(ClothesCategoryVO clothesCategoryVO, Integer sortingcategoryid,SupervisorVO supervisorVO);

	public Double getSortedClothesWeightBySortingId(Integer clothesSortingId);

	public List<SchoolSortingAgentDetailsVO> getSortingAgentDetails();
	
}
