package com.irctn.serviceimpl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irctn.model.Supervisor;
import com.irctn.repository.SupervisorRepository;
import com.irctn.service.ClothesCategoryService;
import com.irctn.service.ClothesCollectionService;
import com.irctn.service.ContributorService;
import com.irctn.service.ProgramService;
import com.irctn.service.RetailerSortingBoxCategoryService;
import com.irctn.service.RetailerSortingBoxService;
import com.irctn.service.SchoolProgramMappingService;
import com.irctn.service.SchoolSortingProcessService;
import com.irctn.service.SupervisorService;
import com.irctn.service.UserService;
import com.irctn.util.AppConstants;
import com.irctn.util.ErrorConstants;
import com.irctn.vo.ClothesCategoryVO;
import com.irctn.vo.ClothesCollectionVO;
import com.irctn.vo.ContributorVO;
import com.irctn.vo.MessageVO;
import com.irctn.vo.ProgramVO;
import com.irctn.vo.RetailerSortingBoxDetailVO;
import com.irctn.vo.RetailerSortingBoxVO;
import com.irctn.vo.SchoolAdminVO;
import com.irctn.vo.SchoolSortingVO;
import com.irctn.vo.SupervisorVO;
import com.irctn.vo.UserVO;

@Service
public class SupervisorServiceImpl implements SupervisorService {

	@Autowired
	SupervisorRepository supervisorRepository;

	@Autowired
	ClothesCollectionService clothesCollectionService;

	@Autowired
	SchoolSortingProcessService schoolSortingProcessService;

	@Autowired
	ProgramService programService;

	@Autowired
	ClothesCategoryService clothesCategoryService;

	@Autowired
	ContributorService contributorService;

	@Autowired
	UserService userService;

	@Autowired
	SchoolProgramMappingService schoolProgramMappingService;

	@Autowired
	RetailerSortingBoxService retailerSortingBoxService;

	@Autowired
	RetailerSortingBoxCategoryService retailerSortingBoxCategoryService;

	@Override
	public String saveSupervisor(SupervisorVO supervisorVO) {
		Supervisor addSupervisor = new Supervisor();
		Supervisor supervisor = supervisorRepository.findByClothessortingid(supervisorVO.getClothesSortingId());

		if (null != supervisor) {
			System.out.println("supervisor details are not null");
			supervisor.setCollectionid(supervisorVO.getCollectionId());
			supervisor.setSupervisorid(supervisorVO.getSupervisorId());
			supervisor.setNumberofbags(supervisorVO.getNumberOfBags());
			supervisor.setTotalweight(supervisorVO.getTotalWeight());
			supervisor.setStartdate(supervisorVO.getStartDate());
			supervisor.setEnddate(supervisorVO.getEndDate());
			supervisor.setTotalwaste(supervisorVO.getTotalWaste());
			supervisor.setTotalreusable(supervisorVO.getTotalReusable());
			supervisor.setStatus(AppConstants.STATUS_INPROGRESS);
			supervisorRepository.save(supervisor);
			System.out.println("supervisor details Updated");
			return "updatesuccess";
		} else {
			System.out.println("supervisor Details is null");
			addSupervisor.setCollectionid(supervisorVO.getCollectionId());
			addSupervisor.setSupervisorid(supervisorVO.getSupervisorId());
			addSupervisor.setNumberofbags(supervisorVO.getNumberOfBags());
			addSupervisor.setTotalweight(supervisorVO.getTotalWeight());
			addSupervisor.setStartdate(supervisorVO.getStartDate());
			addSupervisor.setEnddate(supervisorVO.getEndDate());
			addSupervisor.setTotalwaste(supervisorVO.getTotalWaste());
			addSupervisor.setTotalreusable(supervisorVO.getTotalReusable());
			addSupervisor.setStatus(AppConstants.STATUS_INPROGRESS);
			supervisorRepository.save(addSupervisor);
			System.out.println("supervisor Details saved successfully");
			return "success";
		}
	}

	@Override
	public List<SupervisorVO> getAllSupervisorDetails() {

		List<Supervisor> supervisorList = supervisorRepository.findAll();
		List<SupervisorVO> voList = new ArrayList<SupervisorVO>();
		if (null == supervisorList) {
			return null;
		} else {
			for (Supervisor supervisor : supervisorList) {
				System.out.println("Iterating all Supervisors............");
				SupervisorVO vo = new SupervisorVO();

				vo.setTotalWeight(supervisor.getTotalweight());

				if (AppConstants.STATUS_INPROGRESS.equals(supervisor.getStatus())) {
					vo.setStatusName(AppConstants.STATUS_PROGRESS);
					Double d = schoolSortingProcessService
							.getSortedClothesWeightBySortingId(supervisor.getClothessortingid());
					DecimalFormat df = new DecimalFormat("#.##");
					System.out.println("Total sorted clothes weight is : " + d.doubleValue());
					vo.setTotalReusable(Double.valueOf(df.format(d.doubleValue())));
					vo.setTotalWaste(0D);
					// List<SchoolSortingVO> totalSortedBagsList =
					// schoolSortingProcessService.getAllSortingProcessBySchoolCollectionId(supervisor.getClothessortingid());
				} else {
					vo.setTotalReusable(supervisor.getTotalreusable());
					vo.setTotalWaste(supervisor.getTotalwaste());
					vo.setStatusName(AppConstants.STATUS_ClOSE);
				}
				vo.setStatus(supervisor.getStatus());
				ClothesCollectionVO clothesCollectionVO = clothesCollectionService
						.getClothesCollectionServiceById(supervisor.getCollectionid());
				if (null == clothesCollectionVO) {
					return null;
				} else {
					vo.setClothesSortingId(supervisor.getClothessortingid());
					vo.setType(clothesCollectionVO.getType());
					vo.setBatchNumber(clothesCollectionVO.getBatchNumber());
					ContributorVO contributor = contributorService
							.getContributorById(clothesCollectionVO.getContributorId());
					if (null == contributor) {
						return null;
					} else {
						vo.setName(contributor.getName());
						voList.add(vo);
						System.out.println("List of All Supervisor" + voList);
					}
				}
			}
		}

		if (voList.isEmpty())
			return null;
		List<SupervisorVO> sortedList = voList.stream()
				.sorted(Comparator.comparing(SupervisorVO::getClothesSortingId).reversed())
				.collect(Collectors.toList());
		return sortedList;
	}

	@Override
	public ClothesCollectionVO getClothesCollectionByBatchNumber(Integer batchNumber) {
		ClothesCollectionVO clothesCollection = clothesCollectionService.getBatchInfo(batchNumber);
		ClothesCollectionVO vo = new ClothesCollectionVO();
		System.out.println("Batch Number Check" + clothesCollection);
		if (null == clothesCollection) {
			System.out.println("Batch Number is Not Found");
			return null;
		} else {
			System.out.println("Batch Number is Not Null" + clothesCollection.getType());
			vo.setCollectionId(clothesCollection.getCollectionId());
			vo.setType(clothesCollection.getType());
			vo.setContributorId(clothesCollection.getContributorId());
			vo.setBatchNumber(clothesCollection.getBatchNumber());
			ContributorVO contributor = contributorService.getEntityById(clothesCollection.getContributorId());
			System.out.println("Contributor with Batch Number" + contributor);
			if (null == contributor) {
				System.out.println("Contributor is Null");
				return null;
			} else {
				System.out.println("Contributor is Found" + contributor.getName());
				vo.setName(contributor.getName());
				vo.setProgramId(clothesCollection.getProgramId());
				ProgramVO program = programService.getProgramById(clothesCollection.getProgramId());
				vo.setProgramName(program.getProgramName());
				Supervisor supervisor = supervisorRepository.findByCollectionid(clothesCollection.getCollectionId());
				System.out.println("Collection id is Found");
				if (null == supervisor) {
					System.out.println("Collection id is Null");
					return null;
				} else {
					System.out.println("Collection id is Found with" + supervisor.getCollectionid());
					vo.setClothesSortingId(supervisor.getClothessortingid());

				}

			}
		}
		return vo;
	}

	@Override
	public List<SchoolAdminVO> getAllSchoolAdminDetails() {
		List<Supervisor> schoolAdminList = supervisorRepository.findAll();
		List<SchoolAdminVO> voList = new ArrayList<SchoolAdminVO>();
		if (null == schoolAdminList) {
			return null;

		} else {
			for (Supervisor schoolAdmin : schoolAdminList) {
				SchoolAdminVO vo = new SchoolAdminVO();
				vo.setTotalWeight(schoolAdmin.getTotalweight());
				vo.setTotalWaste(schoolAdmin.getTotalwaste());
				vo.setTotalRecycle(schoolAdmin.getTotalreusable());
				Supervisor supervisor = supervisorRepository.findByCollectionid(schoolAdmin.getCollectionid());
				ClothesCollectionVO clothesCollectionVO = clothesCollectionService
						.getClothesCollectionServiceById(supervisor.getCollectionid());
				vo.setDate(clothesCollectionVO.getCollectionDate());
				ProgramVO program = programService.getProgramById(clothesCollectionVO.getProgramId());
				vo.setProgramName(program.getProgramName());
				voList.add(vo);

			}
		}
		return voList;
	}

	@Override
	public SupervisorVO findByClothescollectionId(Integer clothesCollectionId) {
		return getSupervisorVOFromModel(supervisorRepository.findByCollectionid(clothesCollectionId));
	}

	@Override
	public MessageVO closeBatch(Integer batchNumber) {
		ClothesCollectionVO clothesCollectionVO = clothesCollectionService
				.getClothesCollectionDetailsByBatchNumber(batchNumber);
		Double totalReusable = 0.0d;
		Double totalWaste = 0.0d;
		Double sortedTotalWeight = 0.0d;
		/*
		if (null == clothesCollectionVO) {
			System.out.println("Batch Number is Not Found");
			return new MessageVO("Failure", "Batch is Not Found", "Contact Supervisor");

		} else {
			*/
		if(null != clothesCollectionVO) {
			if(null != clothesCollectionVO.getErrorCode() && ErrorConstants.BATCH_INVALID_CODE.equals(clothesCollectionVO.getErrorCode())) {
				System.out.println("Batch Number is Not Found");
				return new MessageVO("Failure", "Batch is Not Found", "Contact Supervisor");
			} else if(null != clothesCollectionVO.getErrorCode() && ErrorConstants.BATCH_CLOSED_CODE.equals(clothesCollectionVO.getErrorCode())) {
				System.out.println("Batch Number is already closed");
				return new MessageVO("Failure", "Batch is already closed. Please check with Admin.", "Batch is already closed. Please check with Admin.");
			} 
			
			System.out.println("BatchNumber is Found");
			Supervisor supervisor = supervisorRepository.findByCollectionid(clothesCollectionVO.getCollectionId());

			if (null == supervisor) {
				System.out.println("Collection Id is Not Matched in Clothes Sorting");
				return new MessageVO("Failure", "ClothesSorting Details Not Found", "Contact Supervisor");
			} else {
				if (AppConstants.CONTRIBUTOR_TYPE_SCHOOL.equalsIgnoreCase(clothesCollectionVO.getType())) {
					System.out.println("Batch Type is School -->" + clothesCollectionVO.getName());
					System.out
							.println("--------->Clothes Sorting id during close :" + supervisor.getClothessortingid());
					List<SchoolSortingVO> schoolSortingProcess = schoolSortingProcessService
							.getAllSortingProcessBySchoolCollectionId(supervisor.getClothessortingid());
					// List<SchoolSortingProcess> schoolSortingProcess =
					// schoolSortingProcessRepository
					// .findByClothessortingid(supervisor.getClothessortingid());
					if (null == schoolSortingProcess) {
						System.out.println("School Sorting Process Id is Null");
						return new MessageVO("Failure", "ClothesSorting is not Found", "Contact Supervisor");
					} else {
						for (SchoolSortingVO school : schoolSortingProcess) {

							if (null != school) {
								System.out.println(
										"------------>pk of school sorting " + school.getSchoolSortingProcessId());
								ClothesCategoryVO clothesCategory = clothesCategoryService
										.getSortingClothesById(school.getSortingCategoryId());
								// ClothesCategory clothesCategory =
								// clothesCategoryRepository.findBySortingcategoryid(school.getSortingCategoryId());
								if (null != clothesCategory) {
									System.out.println(
											"---------->reusable and waste :" + clothesCategory.getTotalReusable()
													+ " ----" + clothesCategory.getTotalWaste());
									totalReusable = clothesCategory.getTotalReusable();
									totalWaste = clothesCategory.getTotalWaste();
									sortedTotalWeight = sortedTotalWeight + totalReusable + totalWaste;
									System.out.println(
											"------------>Inside Loop Total Sorted Weight :" + sortedTotalWeight);
									// The Total Sorted Weight of This// Batch // is
									// // TotalReusable + Total Waste Which //
									// Exceeds the
									// Tolerance Limit By Kgs
								}
							}
						}
						System.out.println("------------>Outside Loop Total Sorted Weight :" + sortedTotalWeight);
						Supervisor supervisorObject = supervisorRepository
								.findByClothessortingid(supervisor.getClothessortingid());

						if (null != supervisorObject) {
							Double recordedWeight = supervisorObject.getTotalweight();
							Double minimumWeight = recordedWeight - (recordedWeight * AppConstants.TOLERANCE / 100);
							Double maximumWeight = recordedWeight + (recordedWeight * AppConstants.TOLERANCE / 100);
							System.out.println("Recorded Weight" + recordedWeight);
							System.out.println("Recorded minimumWeight" + minimumWeight);
							System.out.println("Recorded maximumWeight" + maximumWeight);

							if (sortedTotalWeight >= minimumWeight && sortedTotalWeight <= maximumWeight) {
								supervisorObject.setTotalreusable(totalReusable);
								supervisorObject.setTotalwaste(totalWaste);
								supervisorObject.setStatus(AppConstants.STATUS_CLOSED);
								supervisorRepository.save(supervisorObject);
								ClothesCollectionVO collectionObject = clothesCollectionService
										.getClothesCollectionServiceById(supervisorObject.getCollectionid());
								// ClothesCollection collectionObject =
								// clothesCollectionRepository.findByCollectionid(supervisorObject.getCollectionid());
								if (null != collectionObject) {
									System.out.println(" -------------------> Collection Object");
									collectionObject.setCollectionTotalWeight(sortedTotalWeight);
									collectionObject.setStatus(AppConstants.STATUS_CLOSED);
									// clothesCollectionRepository.save(collectionObject);
									clothesCollectionService.saveClothesCollection(collectionObject);
								}

							} else {
								return new MessageVO("failure", "Mismatch in TotalSorted Weight with Collection Weight",
										"Contact Supervisor");
							}
						}

					}

				} else if (AppConstants.CONTRIBUTOR_TYPE_RETAILER.equalsIgnoreCase(clothesCollectionVO.getType())) {
					System.out.println("Type is Retailer" + clothesCollectionVO.getType());

					List<RetailerSortingBoxVO> retailerSortingBox = retailerSortingBoxService
							.getRetailerSortingBoxByClothesSortingId(supervisor.getClothessortingid());
					if (null == retailerSortingBox) {
						return new MessageVO("failure", "clothesSortingProcess Id Not Found",
								"Failed to get ClothesSortingId");
					} else {

						for (RetailerSortingBoxVO boxInfo : retailerSortingBox) {

							List<RetailerSortingBoxDetailVO> retailerCategoryList = retailerSortingBoxCategoryService
									.getRetailerCategoryByRetailerBoxId(boxInfo.getRetailerSortingBoxId());
							// sortedTotalWeight = sortedTotalWeight +
							// boxInfo.getBoxWeight();
							if (null == retailerCategoryList) {
								System.out.println("-------------------------------> RetailerBox is Null");
								return new MessageVO("failure", "Retailer Box Id is Null", "failure");
							} else {
								for (RetailerSortingBoxDetailVO retailerCategory : retailerCategoryList) {
									sortedTotalWeight = sortedTotalWeight + retailerCategory.getWeight();
								}
							}
						}

						Supervisor supervisorObject = supervisorRepository
								.findByClothessortingid(supervisor.getClothessortingid());
						if (null != supervisorObject) {
							Double recordedWeight = supervisorObject.getTotalweight();
							Double minimumWeight = recordedWeight
									- (recordedWeight * AppConstants.TOLERANCE_LIMIT_RETAILERS / 100);
							Double maximumWeight = recordedWeight
									+ (recordedWeight * AppConstants.TOLERANCE_LIMIT_RETAILERS / 100);
							System.out.println("Recorded Weight" + recordedWeight);
							System.out.println("Recorded minimumWeight" + minimumWeight);
							System.out.println("Recorded maximumWeight" + maximumWeight);

							if (sortedTotalWeight >= minimumWeight && sortedTotalWeight <= maximumWeight) {
								supervisorObject.setStatus(AppConstants.STATUS_CLOSED);
								supervisorRepository.save(supervisorObject);
								ClothesCollectionVO collectionObject = clothesCollectionService
										.getClothesCollectionServiceById(supervisorObject.getCollectionid());

								if (null != collectionObject) {
									System.out.println(" -------------------> Collection Object");
									collectionObject.setCollectionTotalWeight(sortedTotalWeight);
									collectionObject.setStatus(AppConstants.STATUS_CLOSED);

									clothesCollectionService.saveClothesCollection(collectionObject);
								}
							}

							else {
								return new MessageVO("failure", "Mismatch in TotalSorted Weight with Collection Weight",
										"Contact Supervisor");

							}

						}

					}
				}

			}
		}
		String batchNumberResponse = Integer.toString(clothesCollectionVO.getBatchNumber());
		return new MessageVO(AppConstants.SUCCESS, "SuccessfullyBatchClosed", batchNumberResponse);
	}

	private SupervisorVO getSupervisorVOFromModel(Supervisor supervisor) {
		if (null == supervisor)
			return null;
		SupervisorVO vo = new SupervisorVO();
		vo.setClothesSortingId(supervisor.getClothessortingid());
		vo.setCollectionId(supervisor.getCollectionid());
		vo.setSupervisorId(supervisor.getSupervisorid());
		vo.setNumberOfBags(supervisor.getNumberofbags());
		vo.setTotalWeight(supervisor.getTotalweight());
		vo.setStartDate(supervisor.getStartdate());
		vo.setEndDate(supervisor.getEnddate());
		vo.setTotalWaste(supervisor.getTotalwaste());
		vo.setTotalReusable(supervisor.getTotalreusable());
		UserVO user = userService.getUserById(supervisor.getSupervisorid());
		if (null == user) {
			return null;
		} else {
			if (null != user && null != user.getRoleName()) {
				vo.setFirstName(user.getFirstName());
				vo.setLastName(user.getLastName());

			}
			ClothesCollectionVO collection = clothesCollectionService
					.getClothesCollectionServiceById(supervisor.getCollectionid());
			if (null == collection) {
				return null;
			} else {
				vo.setBatchNumber(collection.getBatchNumber());
			}
		}

		return vo;

	}

	private List<SupervisorVO> getVOListFromModel(List<Supervisor> supervisors) {
		List<SupervisorVO> list = new ArrayList<SupervisorVO>();
		supervisors.forEach(supervisor -> {
			list.add(getSupervisorVOFromModel(supervisor));
		});
		return list;

	}

	@Override
	public List<SupervisorVO> getClothesSortingBySupervisors() {
		List<Supervisor> supervisors = supervisorRepository.findAll();
		List<SupervisorVO> list = new ArrayList<SupervisorVO>();
		List<SupervisorVO> sortedList = null;
		if (null == supervisors) {
			return null;
		} else {
			supervisors.forEach(supervisor -> {
				SupervisorVO vo = new SupervisorVO();
				vo.setTotalWeight(supervisor.getTotalweight());
				vo.setTotalReusable(supervisor.getTotalreusable());
				vo.setTotalWaste(supervisor.getTotalwaste());
				vo.setClothesSortingId(supervisor.getClothessortingid());
				UserVO user = userService.getUserById(supervisor.getSupervisorid());
				if (null != user) {
					vo.setFirstName(user.getFirstName());
					vo.setLastName(user.getLastName());
				}
				ClothesCollectionVO collection = clothesCollectionService
						.getClothesCollectionServiceById(supervisor.getCollectionid());
				if (null != collection) {
					vo.setBatchNumber(collection.getBatchNumber());
					list.add(vo);
				}
			});
			sortedList = list.stream().sorted(Comparator.comparing(SupervisorVO::getClothesSortingId))
					.collect(Collectors.toList());
		}
		return sortedList;
	}

	@Override
	public SupervisorVO getClothesSortingBySupervisor(Integer supervisorid) {

		return getSupervisorVOFromModel(supervisorRepository.findBySupervisorid(supervisorid));
	}

	@Override
	public SupervisorVO getSupervisorByBatchNumber(Integer batchNumber) {
		ClothesCollectionVO collectionVO = getClothesCollectionByBatchNumber(batchNumber);
		if (null == collectionVO)
			return null;
		Supervisor supervisor = supervisorRepository.findByCollectionid(collectionVO.getCollectionId());
		if (null == supervisor)
			return null;
		return getSupervisorVOFromModel(supervisor);
	}

	@Override
	public List<SupervisorVO> countByStatus() {

		return getVOListFromModel(supervisorRepository.findByStatus(AppConstants.STATUS_CLOSED));
	}

	@Override
	public SupervisorVO getClothesCollectionByCollectionId(Integer collectionid) {

		return getSupervisorVOFromModel(supervisorRepository.findByCollectionid(collectionid));
	}

	@Override
	public List<SchoolAdminVO> getSchoolContributions(Integer contributorId) {
		List<ClothesCollectionVO> schoolContributionsCollected = clothesCollectionService
				.getByContributor(contributorId);
		if (null == schoolContributionsCollected)
			return null;

		List<SchoolAdminVO> voList = new ArrayList<SchoolAdminVO>();
		for (ClothesCollectionVO contribution : schoolContributionsCollected) {
			System.out.println("Collection id ----------------" + contribution.getCollectionId());
			Supervisor sortContribution = supervisorRepository.findByCollectionid(contribution.getCollectionId());
			if (null != sortContribution) {
				System.out.println(" -------------sortContribution : " + sortContribution);
				SchoolAdminVO vo = new SchoolAdminVO();
				vo.setTotalWeight(sortContribution.getTotalweight());
				vo.setTotalWaste(sortContribution.getTotalwaste());
				vo.setTotalRecycle(sortContribution.getTotalreusable());
				vo.setDate(contribution.getCollectionDate());
				ProgramVO program = programService.getProgramById(contribution.getProgramId());
				vo.setProgramName(program.getProgramName());
				voList.add(vo);
			} else {
				System.out.println("Sorting is not yet strted for collection :" + contribution.getCollectionId());
			}

		}
		return voList;
	}

}
