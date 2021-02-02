package com.irctn.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irctn.model.SchoolSortingProcess;
import com.irctn.repository.ClothesCategoryRepository;
import com.irctn.repository.SchoolSortingProcessRepository;
import com.irctn.service.SchoolSortingProcessService;
import com.irctn.service.SchoolStudentContributionService;
import com.irctn.service.StudentService;
import com.irctn.service.UserService;
import com.irctn.util.AppConstants;
import com.irctn.vo.ClothesCategoryVO;
import com.irctn.vo.MessageVO;
import com.irctn.vo.SchoolSortingAgentDetailsVO;
import com.irctn.vo.SchoolSortingVO;
import com.irctn.vo.StudentVO;
import com.irctn.vo.SupervisorVO;
import com.irctn.vo.UserVO;

@Service
public class SchoolSortingProcessServiceImpl implements SchoolSortingProcessService {

	@Autowired
	SchoolSortingProcessRepository schoolSortingProcessRepository;

	@Autowired
	ClothesCategoryRepository clothesCategoryRepository;

	@Autowired
	SchoolStudentContributionService schoolStudentContributionService;

	@Autowired
	StudentService studentService;
	
	@Autowired
	UserService userService;

	@Override
	public String saveSchoolSortingProcess(SchoolSortingVO schoolSortingVO) {
		if (null == schoolSortingVO) {
			System.out.println("School Sorting vo Cannot be null");
			return AppConstants.FAILURE;
		}
		if (null == schoolSortingVO.getClothesSortingId() & null == schoolSortingVO.getSortingUserId()) {
			System.out.println("ClothesSortingId and SortingUserId is Cannot be Null ");
			return AppConstants.FAILURE;
		}
		if (null == schoolSortingVO.getStudentId()) {
			System.out.println(" StudentId cannot be Null");
			return AppConstants.FAILURE;
		}
		if (null != schoolSortingVO.getSchoolSortingProcessId()) {
			System.out.println("Cannot Update");
			return AppConstants.FAILURE;
		}
		
		StudentVO studentVO = studentService.getStudentById(schoolSortingVO.getStudentId());
		if (null != studentVO) {
			//SchoolStudentContributionVO schoolStudentContributionVO = schoolStudentContributionService
			//		.getSchoolStudentContributionByStudentId(studentVO.getStudentId());
			//the process should involve program id and if the same student has contributed to the same schoolprogramid add  new contribution
			//schoolSortingVO.getSchoolProgramMappingId();
			/*
			SchoolStudentContributionVO schoolStudentContributionVO = schoolStudentContributionService.getContributionByStudentAndProgram(
																		schoolSortingVO.getSchoolProgramMappingId(), studentVO.getStudentId());
			//even if it is there, the same student can contribute two bags in a school programme
			if (null == schoolStudentContributionVO) {
				System.out.println("New contribution record created for : " + studentVO.getStudentName());
			} else {
				System.out.println("Next New  contribution record created for : " + studentVO.getStudentName());				
			}
			*/
			MessageVO messageVO = schoolStudentContributionService
					.mapStudentIdToSchoolStudentContribution(studentVO.getStudentId(), schoolSortingVO);
			messageVO.getResult();
			Integer schoolStudentContributionId = messageVO.getBatchNumber();
			schoolSortingVO.setSchoolStudentContributionId(schoolStudentContributionId);
		}
		
		String success = AppConstants.SUCCESS;
		SchoolSortingProcess sortingProcess = schoolSortingProcessRepository
				.findBySchoolsortingprocessid(schoolSortingVO.getSchoolSortingProcessId());
		if (null == sortingProcess) {
			sortingProcess = new SchoolSortingProcess();
		} else {
			success = "updatesuccess";
		}

		sortingProcess.setClothessortingid(schoolSortingVO.getClothesSortingId());
		sortingProcess.setSortinguserid(schoolSortingVO.getSortingUserId());
		sortingProcess.setSchoolstudentcontributionid(schoolSortingVO.getSchoolStudentContributionId());
		schoolSortingProcessRepository.save(sortingProcess);

		return success;
	}

	@Override
	public SchoolSortingVO getSchoolSortingProcessById(Integer schoolSortingProcessId) {
		SchoolSortingProcess sortingProcess = schoolSortingProcessRepository
				.findBySchoolsortingprocessid(schoolSortingProcessId);
		return getVOFromModel(sortingProcess);
	}

	private SchoolSortingVO getVOFromModel(SchoolSortingProcess sortingProcess) {
		if (null == sortingProcess)
			return null;
		SchoolSortingVO schoolSortingVO = new SchoolSortingVO();
		schoolSortingVO.setClothesSortingId(sortingProcess.getClothessortingid());
		schoolSortingVO.setSchoolSortingProcessId(sortingProcess.getSchoolsortingprocessid());
		schoolSortingVO.setBagWeight(sortingProcess.getBagweight());
		schoolSortingVO.setSchoolStudentContributionId(sortingProcess.getSchoolstudentcontributionid());

		schoolSortingVO.setSortingCategoryId(sortingProcess.getSortingcategoryid());
		return schoolSortingVO;
	}

	private List<SchoolSortingVO> getListVOFromListModel(List<SchoolSortingProcess> models) {
		if (null == models)
			return null;
		List<SchoolSortingVO> listVO = new ArrayList<SchoolSortingVO>();
		for (SchoolSortingProcess model : models) {
			listVO.add(getVOFromModel(model));
		}
		return listVO;
	}

	@Override
	public List<SchoolSortingVO> getAllSortingProcessBySchoolCollectionId(Integer clothesSortingId) {
		return getListVOFromListModel(schoolSortingProcessRepository.findByClothessortingid(clothesSortingId));

	}

	@Override
	public MessageVO mapCategoryToSchoolSortingProcess(ClothesCategoryVO clothesCategoryVO, Integer sortingcategoryid,
			SupervisorVO supervisorVO) {
		SchoolSortingProcess school = schoolSortingProcessRepository
				.findByClothessortingidAndSchoolstudentcontributionid(supervisorVO.getClothesSortingId(),
						clothesCategoryVO.getSchoolStudentContributionId());
		if (null == school) {
			System.out.println("ClothesSorting and SchoolStudentContribution is Null");
			return new MessageVO("-----------------> Failure",
					"clothes Sorting Id and School Student Contribution Id is Null");
		} else {
			System.out.println("--------------------> ClothesSorting and SchoolStudentContribution is Not Null");
			school.setSortingcategoryid(sortingcategoryid);
			school.setBagweight(clothesCategoryVO.getTotalReusable() + clothesCategoryVO.getTotalWaste());
			school.setSortinguserid(clothesCategoryVO.getSortingUserId());
			SchoolSortingProcess saveSchoolSortingProcess = schoolSortingProcessRepository.save(school);

			if (null != saveSchoolSortingProcess) {
				return new MessageVO("success", "Sorting Category have been mapped to SchoolSortingProcess ",
						"saved Successfully");
			} else {
				return new MessageVO("failure", "Error in creating Sorting Category mapping.",
						"Sorting CategoryMapping error ");
			}

		}

	}

	@Override
	public Double getSortedClothesWeightBySortingId(Integer clothesSortingId) {
		List<SchoolSortingVO> list = this.getAllSortingProcessBySchoolCollectionId(clothesSortingId);
		Double totalSortedWeight = 0.0D;
		if(null != list) {
			/*
			totalSortedWeight = list.stream()
					.map(SchoolSortingVO :: getBagWeight)
					.reduce(Double :: sum)
					.get();
					*/
			for(SchoolSortingVO sortingVO : list) {
				Double weight = sortingVO.getBagWeight();
				if(null != weight) {
					totalSortedWeight = totalSortedWeight + weight.doubleValue();
				}
			}
		}
		return totalSortedWeight;
	}

	@Override
	public List<SchoolSortingAgentDetailsVO> getSortingAgentDetails() {
		List<SchoolSortingProcess> list = schoolSortingProcessRepository.findBySortingcategoryidIsNotNullOrderBySortinguserid();
		List<SchoolSortingAgentDetailsVO> result = null;
		if(null != list) {
			Integer sortingUserId = null;
			Integer clothesSortingId = 0;
			Double totalBagWeight = 0.0D;
			String sortingUser = null;
			Integer batchNumber = 0;
			int sortedBatches = 0;
			result = new ArrayList<SchoolSortingAgentDetailsVO>();
			SchoolSortingAgentDetailsVO vo = null;
			
			for(int i=0; i < list.size(); i++) {
				
				SchoolSortingProcess process = list.get(i);
				if(i == 0) {
					//first record
					sortingUserId = process.getSortinguserid();
					clothesSortingId = process.getClothessortingid();
					UserVO user = userService.getUserById(sortingUserId);
					if(null != user) {
						sortingUser = user.getFirstName() + " " + user.getLastName();
					}
					vo = new SchoolSortingAgentDetailsVO();
					vo.setAgent(sortingUser);
					vo.setAgentId(sortingUserId);
					totalBagWeight = totalBagWeight + process.getBagweight();
					sortedBatches++;
					vo.setSortedBatches(sortedBatches);
					vo.setTotalWeight(totalBagWeight);
					
					
				} else {
					
					if(sortingUserId != process.getSortinguserid() ) {	//this is a different user
						
						result.add(vo);
						sortedBatches = 1;
						totalBagWeight = process.getBagweight();
						sortingUserId = process.getSortinguserid();
						clothesSortingId = process.getClothessortingid();
						UserVO user = userService.getUserById(sortingUserId);
						if(null != user) {
							sortingUser = user.getFirstName() + " " + user.getLastName();
						}
						vo = new SchoolSortingAgentDetailsVO();
						vo.setAgent(sortingUser);
						vo.setAgentId(sortingUserId);
						vo.setSortedBatches(sortedBatches);
						vo.setTotalWeight(totalBagWeight);
						
					} else {
						if(clothesSortingId != process.getClothessortingid()) {		//this is a different batch	
							System.out.println("Different batches " + clothesSortingId + "next is  : "+ process.getClothessortingid());
							sortedBatches++;
							clothesSortingId = process.getClothessortingid();
						}
						totalBagWeight = totalBagWeight + process.getBagweight();
						vo.setSortedBatches(sortedBatches);
						vo.setTotalWeight(totalBagWeight);
					}
					
					if(i == list.size()-1) {
						result.add(vo);
					}
				}
				
			}
			/*
			for(SchoolSortingProcess process : list) {
				//First time additions
				if(null == sortingUserId ) {
					sortingUserId = process.getSortinguserid();
				}
				if(null == clothesSortingId) {
					clothesSortingId = process.getClothessortingid();
				}
				
				if(sortingUserId != process.getSortinguserid()) {	//this is a different user
					
					UserVO user = userService.getUserById(sortingUserId);
					if(null != user) {
						sortingUser = user.getFirstName() + " " + user.getLastName();
					}			
					
				} else {
					//if(null != process.getClothessortingid()) {
						if(clothesSortingId != process.getClothessortingid()) {		//this is a different batch					
							SchoolSortingAgentDetailsVO vo = new SchoolSortingAgentDetailsVO();
							vo.setAgent(sortingUser);
							vo.setAgentId(sortingUserId);
							vo.setSortedBatches(sortedBatches);
							vo.setTotalWeight(totalBagWeight);
							result.add(vo);
							
							clothesSortingId = process.getClothessortingid();
							sortedBatches = 0;
						} 
						totalBagWeight = totalBagWeight + process.getBagweight();
						sortedBatches++;
						
					//}
				}
			}*/
		}
		return result;
	}


}
