package com.irctn.serviceimpl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.irctn.model.ClothesCollection;
import com.irctn.model.Contributor;
import com.irctn.repository.ClothesCollectionRepository;
import com.irctn.service.ClothesCollectionService;
import com.irctn.service.ContributorService;
import com.irctn.service.ProgramService;
import com.irctn.service.SchoolProgramMappingService;
import com.irctn.service.SchoolStudentContributionService;
import com.irctn.service.StudentService;
import com.irctn.service.SupervisorService;
import com.irctn.util.AppConstants;
import com.irctn.util.ErrorConstants;
import com.irctn.vo.ClothesCollectionVO;
import com.irctn.vo.ContributorVO;
import com.irctn.vo.ProgramVO;
import com.irctn.vo.SchoolProgramMappingVO;
import com.irctn.vo.SupervisorVO;

@Service
public class ClothesCollectionServiceImpl implements ClothesCollectionService {

	@Autowired
	ClothesCollectionRepository clothesCollectionRepository;

	@Autowired
	ProgramService programService;

	@Autowired
	ContributorService contributorService;

	@Autowired
	SupervisorService supervisorService;

	@Autowired
	RedisTemplate<Object, Object> redisTemplate;

	@Autowired
	StudentService studentService;

	@Autowired
	SchoolStudentContributionService schoolStudentContributionService;

	@Autowired
	SchoolProgramMappingService schoolProgramMappingService;

	@Override
	public String saveClothesCollection(ClothesCollectionVO clothesCollectionVO) {

		ClothesCollection clothesCollection = clothesCollectionRepository
				.findByCollectionid(clothesCollectionVO.getCollectionId());
		String success = "success";
		if (null == clothesCollection) {
			clothesCollection = new ClothesCollection();
			System.out.println("Clothes collection is null");
			int batchNumber = 50;
			ClothesCollection batchNumberObject = clothesCollectionRepository.findTopByOrderByBatchnumberDesc();
			if (null != batchNumberObject && null != batchNumberObject.getBatchnumber()) {
				int dbBatchNumber = batchNumberObject.getBatchnumber();
				if (dbBatchNumber >= 50) {
					batchNumber = dbBatchNumber + 1;
				}
			}
			clothesCollection.setBatchnumber(batchNumber);
			clothesCollection.setStatus(AppConstants.STATUS_NEW);
		} else {
			clothesCollection.setStatus(clothesCollectionVO.getStatus());
			success = "updatesuccess";
		}
		clothesCollection.setType(clothesCollectionVO.getType());
		clothesCollection.setCollectionUserId(clothesCollectionVO.getCollectionUserId());
		clothesCollection.setContributorid(clothesCollectionVO.getContributorId());
		clothesCollection.setProgramid(clothesCollectionVO.getProgramId());
		clothesCollection.setNoofbags(clothesCollectionVO.getNoOfBags());
		clothesCollection.setCollectionTotalWeight(clothesCollectionVO.getCollectionTotalWeight());
		clothesCollectionRepository.save(clothesCollection);

		if (AppConstants.CONTRIBUTOR_TYPE_RETAILER.equalsIgnoreCase(clothesCollectionVO.getType())) {
			redisTemplate.opsForHash().delete("CLOTHESCOLLECTION_BY_RETAILER", "RETAILER");
		} else {
			redisTemplate.opsForHash().delete("CLOTHESCOLLECTION_BY_SCHOOLS", "SCHOOLS");
		}

		return success;
	}

	@Override
	public List<ClothesCollectionVO> getCollectionsByAllSchools() {
		Object object = redisTemplate.opsForHash().get("CLOTHESCOLLECTION_BY_SCHOOLS", "SCHOOLS");
		if (null == object) {
			List<ClothesCollection> schoolCollections = clothesCollectionRepository
					.findByType(AppConstants.CONTRIBUTOR_TYPE_SCHOOL);
			if (null == schoolCollections)
				return null;

			List<ClothesCollectionVO> list = getClothesCollectionVOFromModel(schoolCollections);
			if (null != list && !list.isEmpty()) {
				List<ClothesCollectionVO> sortedList = list.stream()
						.sorted(Comparator.comparing(ClothesCollectionVO::getCollectionId).reversed())
						.collect(Collectors.toList());
				redisTemplate.opsForHash().put("CLOTHESCOLLECTION_BY_SCHOOLS", "SCHOOLS", sortedList);
				return sortedList;

			} else {
				return null;
			}

		} else {
			return (List<ClothesCollectionVO>) object;
		}
	}

	@Override
	public List<ClothesCollectionVO> getCollectionsByAllRetailers() {

		Object object = redisTemplate.opsForHash().get("CLOTHESCOLLECTION_BY_RETAILER", "RETAILER");
		if (null == object) {

			List<ClothesCollection> retailerCollections = clothesCollectionRepository
					.findByType(AppConstants.CONTRIBUTOR_TYPE_RETAILER);
			if (null == retailerCollections)
				return null;

			List<ClothesCollectionVO> list = getClothesCollectionVOFromModel(retailerCollections);
			if (null != list && !list.isEmpty()) {
				List<ClothesCollectionVO> sortedList = list.stream()
						.sorted(Comparator.comparing(ClothesCollectionVO::getCollectionId).reversed())
						.collect(Collectors.toList());
				redisTemplate.opsForHash().put("CLOTHESCOLLECTION_BY_RETAILER", "RETAILER", sortedList);
				return sortedList;

			} else {
				return null;
			}

		} else {
			return (List<ClothesCollectionVO>) object;
		}
	}

	@Override
	public ClothesCollectionVO getBatchInfo(Integer batchNumber) {

		ClothesCollection batchNumberObject = clothesCollectionRepository.findByBatchnumber(batchNumber);
		ClothesCollectionVO vo = new ClothesCollectionVO();
		if (null == batchNumberObject) {
			return null;
		} else {
			vo.setStatus(batchNumberObject.getStatus());
			vo.setBatchNumber(batchNumber);
			vo.setCollectionId(batchNumberObject.getCollectionid());
			vo.setNoOfBags(batchNumberObject.getNoofbags());
			ProgramVO program = programService.getProgramById(batchNumberObject.getProgramid());
			if (null == program) {
				return null;
			} else {
				vo.setProgramName(program.getProgramName());
				vo.setProgramId(program.getProgramId());
				ContributorVO contributor = contributorService.getContributorById(batchNumberObject.getContributorid());
				if (null == contributor) {
					return null;
				} else {
					vo.setType(contributor.getType());
					vo.setContributorId(contributor.getContributorid());
					vo.setName(contributor.getName());
					vo.setCollectionTotalWeight(batchNumberObject.getCollectionTotalWeight());
				}

			}
		}
		return vo;
	}

	@Override
	public List<ClothesCollectionVO> searchSchoolsWithContribution(String schoolName) {
		List<Contributor> contributorList = null;
		List<ClothesCollectionVO> voList = new ArrayList<ClothesCollectionVO>();
		contributorList = contributorService.getContributorNameLike(schoolName);
		System.out.println("Size of Contributor" + contributorList);
		if (null != contributorList) {
			for (Contributor contributors : contributorList) {
				if (AppConstants.CONTRIBUTOR_TYPE_SCHOOL.equalsIgnoreCase(contributors.getType())) {
					System.out.println("Iterating List for SchoolContribution" + contributors.getContributorid());
					ClothesCollectionVO clothesCollectionVO = new ClothesCollectionVO();
					clothesCollectionVO.setName(contributors.getName());
					clothesCollectionVO.setType(contributors.getType());
					clothesCollectionVO.setContributorId(contributors.getContributorid());
					voList.add(clothesCollectionVO);
				}

			}
			return voList;
		} else {
			return null;
		}
	}

	@Override
	public List<ClothesCollectionVO> searchRetailersWithContribution(String retailerName) {
		List<Contributor> contributorList = null;
		List<ClothesCollectionVO> voList = new ArrayList<ClothesCollectionVO>();
		contributorList = contributorService.getContributorNameLike(retailerName);
		System.out.println("Size of Contributor" + contributorList);
		if (null != contributorList) {
			for (Contributor contributors : contributorList) {
				if (AppConstants.CONTRIBUTOR_TYPE_RETAILER.equalsIgnoreCase(contributors.getType())) {
					System.out.println("Iterating List for SchoolContribution" + contributors.getContributorid());
					ClothesCollectionVO clothesCollectionVO = new ClothesCollectionVO();
					clothesCollectionVO.setName(contributors.getName());
					clothesCollectionVO.setType(contributors.getType());
					clothesCollectionVO.setContributorId(contributors.getContributorid());
					voList.add(clothesCollectionVO);
				}
			}
			return voList;
		} else {
			return null;
		}
	}

	@Override
	public ClothesCollectionVO getClothesCollectionDetailsByBatchNumber(Integer batchNumber) {
		ClothesCollection clothesCollection = clothesCollectionRepository.findByBatchnumber(batchNumber);
		ClothesCollectionVO vo = new ClothesCollectionVO();
		System.out.println("Batch Number Check" + clothesCollection);
		if (null == clothesCollection) {
			System.out.println("Batch Number is Not Found");
			vo.setErrorCode(ErrorConstants.BATCH_INVALID_CODE);
			vo.setErrorMsg(ErrorConstants.BATCH_INVALID_MSG);
			return vo;
		} else {
			if( null != clothesCollection.getStatus() && AppConstants.STATUS_CLOSED == clothesCollection.getStatus()) {
				vo.setErrorCode(ErrorConstants.BATCH_CLOSED_CODE);
				vo.setErrorMsg(ErrorConstants.BATCH_CLOSED_MSG);
				return vo;
			}
			
			System.out.println("Batch Number is Not Null " + clothesCollection.getType());
			vo.setBatchNumber(batchNumber);
			vo.setCollectionId(clothesCollection.getCollectionid());
			vo.setType(clothesCollection.getType());
			vo.setContributorId(clothesCollection.getContributorid());
			ContributorVO contributor = contributorService.getEntityById(clothesCollection.getContributorid());
			System.out.println("Contributor with Batch Number " + contributor);
			if (null == contributor) {
				System.out.println("Contributor is Null");
				return null;
			} else {
				System.out.println("Contributor is Found" + contributor.getName());
				vo.setName(contributor.getName());
				vo.setProgramId(clothesCollection.getProgramid());
				ProgramVO program = programService.getProgramById(clothesCollection.getProgramid());
				vo.setProgramName(program.getProgramName());
				SupervisorVO supervisor = supervisorService
						.findByClothescollectionId(clothesCollection.getCollectionid());
				System.out.println("Collection id is Found");
				if (null == supervisor) {
					System.out.println("Collection id is Null");
					return null;
				} else {
					System.out.println("Collection id is Found with" + supervisor.getCollectionId());
					vo.setClothesSortingId(supervisor.getClothesSortingId());

				}

				// get the school program mapping id - use contributor id and program id
				SchoolProgramMappingVO schoolProgramMappingVO = schoolProgramMappingService
						.getSchoolProgramMappingByContributorIdAndProgramId(vo.getContributorId(), vo.getProgramId());
				if (null == schoolProgramMappingVO) {
					System.out.println("------------------ERROR : No mapping in place for school and a program");
				} else {
					vo.setSchoolProgramMappingId(schoolProgramMappingVO.getSchoolProgrammappingId());
				}

			}
		}
		return vo;
	}

	@Override
	public List<ClothesCollectionVO> getAllRetailerContributions() {
		List<ClothesCollection> retailerContributionList = clothesCollectionRepository
				.findByType(AppConstants.CONTRIBUTOR_TYPE_RETAILER);
		List<ClothesCollectionVO> voList = new ArrayList<ClothesCollectionVO>();
		if (null == retailerContributionList) {
			return null;
		} else {
			for (ClothesCollection retailer : retailerContributionList) {
				ClothesCollectionVO vo = new ClothesCollectionVO();
				vo.setProgramId(retailer.getProgramid());
				vo.setCollectionTotalWeight(retailer.getCollectionTotalWeight());
				vo.setCollectionDate(retailer.getCollectionDate());
				vo.setCollectionId(retailer.getCollectionid());
				ContributorVO retailerContributor = contributorService.getEntityById(retailer.getContributorid());
				if (null == retailerContributor) {
					return null;
				} else {
					vo.setName(retailerContributor.getName());
					vo.setType(retailerContributor.getType());
					ProgramVO program = programService.getProgramById(retailer.getProgramid());
					if (null == program) {
						return null;
					} else {
						vo.setProgramName(program.getProgramName());
						vo.setProgramId(program.getProgramId());
						voList.add(vo);
					}

				}
			}
			return voList;
		}

	}

	@Override
	public ClothesCollectionVO getRetailerContributionForBatchByCollectionId(Integer clothesCollectionId) {
		ClothesCollection collection = clothesCollectionRepository.findByCollectionid(clothesCollectionId);
		ClothesCollectionVO vo = new ClothesCollectionVO();
		if (null == collection) {
			return null;
		} else {
			vo.setProgramId(collection.getProgramid());
			vo.setCollectionTotalWeight(collection.getCollectionTotalWeight());
			vo.setCollectionDate(collection.getCollectionDate());
			vo.setCollectionId(collection.getCollectionid());

			ContributorVO retailerContributor = contributorService.getEntityById(collection.getContributorid());
			// Contributor retailerContributor =
			// contributorRepository.findByContributorid(collection.getContributorid());

			if (null == retailerContributor) {
				return null;
			} else {
				vo.setName(retailerContributor.getName());
				vo.setType(retailerContributor.getType());
				ProgramVO program = programService.getProgramById(collection.getProgramid());
				// Program program =
				// programRepository.findByProgramid(collection.getProgramid());
				if (null == program) {
					return null;
				} else {
					vo.setProgramName(program.getProgramName());
					vo.setProgramId(program.getProgramId());
				}
			}

			return vo;
		}
	}

	private List<ClothesCollectionVO> getClothesCollectionVOFromModel(List<ClothesCollection> contributorCollections) {
		List<ClothesCollectionVO> list = new ArrayList<ClothesCollectionVO>();
		for (ClothesCollection contributorCollection : contributorCollections) {
			list.add(getVOFromModel(contributorCollection));
		}
		return list;
	}

	private ClothesCollectionVO getVOFromModel(ClothesCollection contributorCollection) {
		if (null == contributorCollection)
			return null;
		ClothesCollectionVO vo = new ClothesCollectionVO();
		vo.setCollectionId(contributorCollection.getCollectionid());
		vo.setCollectionDate(contributorCollection.getCollectionDate());
		vo.setBatchNumber(contributorCollection.getBatchnumber());
		vo.setContributorId(contributorCollection.getContributorid());
		vo.setType(contributorCollection.getType());
		vo.setNoOfBags(contributorCollection.getNoofbags());
		ContributorVO contributorVO = contributorService.getEntityById(contributorCollection.getContributorid());
		if (null != contributorVO && null != contributorVO.getName()) {
			vo.setName(contributorVO.getName());
		}

		vo.setProgramId(contributorCollection.getProgramid());
		ProgramVO programVO = programService.getProgramById(contributorCollection.getProgramid());
		if (null != programVO && null != programVO.getProgramName()) {
			vo.setProgramName(programVO.getProgramName());
		}
		vo.setCollectionTotalWeight(contributorCollection.getCollectionTotalWeight());
		return vo;
	}

	@Override
	public ClothesCollectionVO getClothesCollectionServiceById(Integer collectionId) {
		return getVOFromModel(clothesCollectionRepository.findByCollectionid(collectionId));
	}

	@Override
	public String getStatusByBatchNumber(Integer batchNumber) {
		ClothesCollectionVO clothesCollectionVO = getBatchInfo(batchNumber);
		if (AppConstants.STATUS_CLOSED.equals(clothesCollectionVO.getStatus())) {
			System.out.println("---------------------> Batch is Already Closed");
			return "failure";

		} else {
			System.out.println("---------------------> Batch is Not Closed");
			return "success";
		}

	}

	@Override
	public List<ClothesCollectionVO> getTypeAndStatus(String contributorTypeSchool, Integer statusClosed) {
		System.out.println("-------------------> in getTypeAndStatus()...ClothesCollectionServiceImpl.");
		List<ClothesCollectionVO> list = new ArrayList<ClothesCollectionVO>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String startDateValue = dateFormat.format(date);
		Date startDate = null;
		try {
			startDate = dateFormat.parse(startDateValue);
		} catch (ParseException ne) {
			return null;
		}
		String[] monthName = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };

		Calendar cal = Calendar.getInstance();

		String month = monthName[cal.get(Calendar.MONTH)];
		// Date dateValue = (Date)dateFormat.parse(date);
		// Date startDate =
		// Date.from(start_date.atZone(ZoneId.systemDefault()).toInstant());
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MONTH, 1);
		String endDateValue = formatter.format(now.getTime());
		Date endDate = null;
		try {
			endDate = formatter.parse(endDateValue);
		} catch (ParseException ne) {
			return null;
		}

		List<ClothesCollection> monthlyRecords = clothesCollectionRepository.findByCollectionDateBetween(startDate,
				endDate);
		if (null == monthlyRecords) {
			System.out.println("-------------------> Monthly Record is Not Found");
			return null;

		} else {

			System.out.println(" ----------------------->  Monthly Records are Found  " + monthlyRecords.size());
			for (ClothesCollection monthlyRecord : monthlyRecords) {
				if (AppConstants.CONTRIBUTOR_TYPE_SCHOOL.equalsIgnoreCase(monthlyRecord.getType())) {

					ClothesCollectionVO vo = new ClothesCollectionVO();

					System.out.println("-------------------------> Sorting Clothes Details");
					SupervisorVO supervisor = supervisorService
							.getClothesCollectionByCollectionId(monthlyRecord.getCollectionid());

					if (null == supervisor) {
						System.out.println("-----------------> Supervisor Data is Not Found");
						continue;
						// return null;
					} else {
						System.out.println("------------------------> Sorting Clothes Details Are Found");
						vo.setCollectionTotalWeight(supervisor.getTotalWeight());
						vo.setTotalRecyclable(supervisor.getTotalReusable());
						vo.setTotalWaste(supervisor.getTotalWaste());
						Date start = supervisor.getStartDate();						
						if(null != start) {
							vo.setMonth(monthName[start.getMonth()]);
						} else {
							vo.setMonth(month);
						}
						vo.setCollectionId(supervisor.getCollectionId());
					}

					list.add(vo);
				}
			}

		}
		return list;

	}

	@Override
	public List<ClothesCollectionVO> getContributorByTypeAndStatus(String contributorTypeSchool, Integer statusClosed) {

		return getClothesCollectionVOFromModel(
				clothesCollectionRepository.findByTypeAndStatus(contributorTypeSchool, statusClosed));
	}

	@Override
	public List<ClothesCollectionVO> getByType(String contributorTypeSchool) {

		return getClothesCollectionVOFromModel(clothesCollectionRepository.findByType(contributorTypeSchool));
	}

	@Override
	public List<ClothesCollectionVO> getPreviousOneMonthContribution(String contributorTypeSchool,
			Integer statusClosed) {

		System.out.println("-------------------> in getPreviousOneMonthContribution()...ClothesCollectionServiceImpl.");
		List<ClothesCollectionVO> list = new ArrayList<ClothesCollectionVO>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String startDateValue = dateFormat.format(date);
		Date startDate = null;
		try {
			startDate = dateFormat.parse(startDateValue);
		} catch (ParseException ne) {
			return null;
		}
		String[] monthName = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };

		Calendar cal = Calendar.getInstance();

		String month = monthName[cal.get(Calendar.MONTH)];
		// Date dateValue = (Date)dateFormat.parse(date);
		// Date startDate =
		// Date.from(start_date.atZone(ZoneId.systemDefault()).toInstant());
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MONTH, -1);
		String endDateValue = formatter.format(now.getTime());
		Date endDate = null;
		try {
			endDate = formatter.parse(endDateValue);
		} catch (ParseException ne) {
			return null;
		}
		List<ClothesCollection> monthlyRecords = clothesCollectionRepository.findByCollectionDateBetween(endDate,
				startDate);

		if (null == monthlyRecords) {

			System.out.println("-------------------> Monthly Record is Not Found");
			return null;

		} else {
			System.out.println("--------------------------> Record Size " + monthlyRecords.size());
			System.out.println(" ----------------------->  Monthly Records are Found  " + monthlyRecords.size());
			for (ClothesCollection monthlyRecord : monthlyRecords) {
				if (AppConstants.CONTRIBUTOR_TYPE_SCHOOL.equalsIgnoreCase(monthlyRecord.getType())) {
					ClothesCollectionVO vo = new ClothesCollectionVO();

					System.out.println("-------------------------> Sorting Clothes Details");
					SupervisorVO supervisor = supervisorService
							.getClothesCollectionByCollectionId(monthlyRecord.getCollectionid());

					if (null == supervisor) {
						System.out.println("-----------------> Supervisor Data is Not Found");
						continue; // since some schools might have joined and collection is complete but sorting
									// has not started yet
						/// return null;
					} else {
						System.out.println("------------------------> Sorting Clothes Details Are Found");
						vo.setCollectionTotalWeight(supervisor.getTotalWeight());
						vo.setTotalRecyclable(supervisor.getTotalReusable());
						vo.setTotalWaste(supervisor.getTotalWaste());
						Date start = supervisor.getStartDate();						
						if(null != start) {
							vo.setMonth(monthName[start.getMonth()]);
						} else {
							vo.setMonth(month);
						}
						vo.setCollectionId(supervisor.getCollectionId());
					}

					list.add(vo);
				}
			}
		}
		return list;

	}

	@Override
	public List<ClothesCollectionVO> getPreviousTwoMonthContribution(String contributorTypeSchool,
			Integer statusClosed) {

		System.out.println("-------------------> in getPreviousTwoMonthContribution()...ClothesCollectionServiceImpl.");

		List<ClothesCollectionVO> list = new ArrayList<ClothesCollectionVO>();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MONTH, -1);
		String startDateValue = formatter.format(now.getTime());
		Date startDate = null;
		try {
			startDate = formatter.parse(startDateValue);
		} catch (ParseException ne) {
			return null;
		}

		DateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd ");
		Calendar calender = Calendar.getInstance();
		now.add(Calendar.MONTH, -2);
		String endDateValue = formatter.format(now.getTime());
		Date endDate = null;
		try {
			endDate = formatter.parse(endDateValue);
		} catch (ParseException ne) {
			return null;
		}
		String[] monthName = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };

		Calendar cal = Calendar.getInstance();

		String month = monthName[cal.get(Calendar.MONTH) - 2];
		List<ClothesCollection> monthlyRecords = clothesCollectionRepository.findByCollectionDateBetween(endDate,
				startDate);
		if (null == monthlyRecords) {
			System.out.println("-------------------> Monthly Record is Not Found");
			return null;

		} else {

			System.out.println(" ----------------------->  Monthly Records are Found  " + monthlyRecords.size());
			for (ClothesCollection monthlyRecord : monthlyRecords) {
				if (AppConstants.CONTRIBUTOR_TYPE_SCHOOL.equalsIgnoreCase(monthlyRecord.getType())) {

					ClothesCollectionVO vo = new ClothesCollectionVO();

					System.out.println("-------------------------> Sorting Clothes Details");
					SupervisorVO supervisor = supervisorService
							.getClothesCollectionByCollectionId(monthlyRecord.getCollectionid());

					if (null == supervisor) {
						System.out.println("-----------------> Supervisor Data is Not Found");
						continue; // since some schools might have joined and collection is complete but sorting
									// has not started yet
						// return null;
					} else {
						System.out.println("------------------------> Sorting Clothes Details Are Found");
						vo.setCollectionTotalWeight(supervisor.getTotalWeight());
						vo.setTotalRecyclable(supervisor.getTotalReusable());
						vo.setTotalWaste(supervisor.getTotalWaste());
						Date start = supervisor.getStartDate();						
						if(null != start) {
							vo.setMonth(monthName[start.getMonth()]);
						} else {
							vo.setMonth(month);
						}
						vo.setCollectionId(supervisor.getCollectionId());
					}

					list.add(vo);
				}
			}

		}
		return list;

	}

	@Override
	public List<ClothesCollectionVO> getTopCollectionTotalWeightBySchools() {
		List<ClothesCollectionVO> list = new ArrayList<ClothesCollectionVO>();

		List<ClothesCollection> collectionWeight = clothesCollectionRepository
				.findTop10ByOrderByCollectionTotalWeightDesc();
		// findTop10ByOrderByLevelDesc()
		if (null == collectionWeight) {
			return null;
		} else {
			for (ClothesCollection collection : collectionWeight) {
				if (AppConstants.CONTRIBUTOR_TYPE_SCHOOL.equalsIgnoreCase(collection.getType())) {

					ClothesCollectionVO vo = new ClothesCollectionVO();
					ContributorVO contributor = contributorService.getContributorById(collection.getContributorid());
					if (null == contributor) {
						return null;
					} else {
						vo.setName(contributor.getName());
						vo.setCollectionTotalWeight(collection.getCollectionTotalWeight());
						vo.setType(collection.getType());
					}

					list.add(vo);
				}
			}

		}

		return list;
	}

	@Override
	public List<ClothesCollectionVO> getMonthlyContributionByTypeAndStatus(String contributorTypeSchool,
			Integer statusClosed) {
		List<ClothesCollectionVO> list = new ArrayList<ClothesCollectionVO>();
		List<ClothesCollectionVO> currentMonthContribution = getTypeAndStatus(contributorTypeSchool, statusClosed);
		System.out.println("----------------> CurrentMonthContribution Size" + currentMonthContribution.size());
		List<ClothesCollectionVO> previousOneMonthContribution = getPreviousOneMonthContribution(contributorTypeSchool,
				statusClosed);
		System.out.println("----------------> PreviousMonthContribution Size" + currentMonthContribution.size());
		List<ClothesCollectionVO> previousTwoMonthContribution = getPreviousTwoMonthContribution(contributorTypeSchool,
				statusClosed);
		System.out.println("----------------> PreviousTwoMonthContribution Size" + currentMonthContribution.size());
		List<ClothesCollection> collections = clothesCollectionRepository.findByTypeAndStatus(contributorTypeSchool,
				statusClosed);
		if (null == collections) {
			System.out.println("-----------------------> No Records found for School with Status Closed");
			return null;
		} else {
			for (ClothesCollectionVO vo : currentMonthContribution) {
				list.add(vo);
			}
			for (ClothesCollectionVO vo : previousOneMonthContribution) {
				list.add(vo);
			}
			for (ClothesCollectionVO vo : previousTwoMonthContribution) {
				list.add(vo);
			}
		}

		return list;
	}

	@Override
	public List<ClothesCollectionVO> getRetailerCurrenteMonthContribution() {

		System.out.println(
				"-------------------> in getRetailerCurrenteMonthContribution()...ClothesCollectionServiceImpl.");
		List<ClothesCollectionVO> list = new ArrayList<ClothesCollectionVO>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String startDateValue = dateFormat.format(date);
		Date startDate = null;
		try {
			startDate = dateFormat.parse(startDateValue);
		} catch (ParseException ne) {
			return null;
		}
		String[] monthName = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };

		Calendar cal = Calendar.getInstance();

		String month = monthName[cal.get(Calendar.MONTH)];
		// Date dateValue = (Date)dateFormat.parse(date);
		// Date startDate =
		// Date.from(start_date.atZone(ZoneId.systemDefault()).toInstant());
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MONTH, 1);
		String endDateValue = formatter.format(now.getTime());
		Date endDate = null;
		try {
			endDate = formatter.parse(endDateValue);
		} catch (ParseException ne) {
			return null;
		}

		List<ClothesCollection> monthlyRecords = clothesCollectionRepository.findByCollectionDateBetween(startDate,
				endDate);
		if (null == monthlyRecords) {
			System.out.println("-------------------> Monthly Record is Not Found");
			return null;

		} else {

			System.out.println(" ----------------------->  Monthly Records are Found  " + monthlyRecords.size());
			for (ClothesCollection monthlyRecord : monthlyRecords) {
				if (AppConstants.CONTRIBUTOR_TYPE_RETAILER.equalsIgnoreCase(monthlyRecord.getType())) {

					ClothesCollectionVO vo = new ClothesCollectionVO();

					System.out.println("-------------------------> Sorting Clothes Details");
					SupervisorVO supervisor = supervisorService
							.getClothesCollectionByCollectionId(monthlyRecord.getCollectionid());

					if (null == supervisor) {
						System.out.println("-----------------> Supervisor Data is Not Found");
						continue; // since some retailers might have joined and collection is complete but sorting
									// has not started yet
					} else {
						System.out.println("------------------------> Sorting Clothes Details Are Found");
						vo.setCollectionTotalWeight(supervisor.getTotalWeight());
						vo.setTotalRecyclable(supervisor.getTotalReusable());
						vo.setTotalWaste(supervisor.getTotalWaste());
						Date start = supervisor.getStartDate();						
						if(null != start) {
							vo.setMonth(monthName[start.getMonth()]);
						} else {
							vo.setMonth(month);
						}
						vo.setCollectionId(supervisor.getCollectionId());
					}

					list.add(vo);
				}
			}

		}
		return list;

	}

	@Override
	public List<ClothesCollectionVO> getRetailerPreviousOneMonthContribution() {
		System.out.println(
				"-------------------> in getRetailerPreviousOneMonthContribution()...ClothesCollectionServiceImpl.");
		List<ClothesCollectionVO> list = new ArrayList<ClothesCollectionVO>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
		Date date = new Date();
		String startDateValue = dateFormat.format(date);
		Date startDate = null;
		try {
			startDate = dateFormat.parse(startDateValue);
		} catch (ParseException ne) {
			return null;
		}
		String[] monthName = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };

		Calendar cal = Calendar.getInstance();

		String month = monthName[cal.get(Calendar.MONTH) - 1];
		// Date dateValue = (Date)dateFormat.parse(date);
		// Date startDate =
		// Date.from(start_date.atZone(ZoneId.systemDefault()).toInstant());
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MONTH, -1);
		String endDateValue = formatter.format(now.getTime());
		Date endDate = null;
		try {
			endDate = formatter.parse(endDateValue);
		} catch (ParseException ne) {
			return null;
		}
		List<ClothesCollection> monthlyRecords = clothesCollectionRepository.findByCollectionDateBetween(endDate,
				startDate);
		if (null == monthlyRecords) {
			System.out.println("-------------------> Monthly Record is Not Found");
			return null;

		} else {
			System.out.println(" ----------------------->  Monthly Records are Found  " + monthlyRecords.size());
			for (ClothesCollection monthlyRecord : monthlyRecords) {
				ClothesCollectionVO vo = new ClothesCollectionVO();
				if (AppConstants.CONTRIBUTOR_TYPE_RETAILER.equalsIgnoreCase(monthlyRecord.getType())) {

					System.out.println("-------------------------> Sorting Clothes Details");
					SupervisorVO supervisor = supervisorService
							.getClothesCollectionByCollectionId(monthlyRecord.getCollectionid());

					if (null == supervisor) {
						System.out.println("-----------------> Supervisor Data is Not Found");
						continue; // since some retailers might have joined and collection is complete but sorting
									// has not started yet
					} else {
						System.out.println("------------------------> Sorting Clothes Details Are Found");
						vo.setCollectionTotalWeight(supervisor.getTotalWeight());
						vo.setTotalRecyclable(supervisor.getTotalReusable());
						vo.setTotalWaste(supervisor.getTotalWaste());
						Date start = supervisor.getStartDate();						
						if(null != start) {
							vo.setMonth(monthName[start.getMonth()]);
						} else {
							vo.setMonth(month);
						}
						vo.setCollectionId(supervisor.getCollectionId());
					}

					list.add(vo);
				}
			}

		}
		return list;

	}

	@Override
	public List<ClothesCollectionVO> getRetailerPreviousTwoMonthContribution() {

		System.out.println(
				"-------------------> in getRetailerPreviousTwoMonthContribution()...ClothesCollectionServiceImpl.");
		List<ClothesCollectionVO> list = new ArrayList<ClothesCollectionVO>();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MONTH, -1);
		String startDateValue = formatter.format(now.getTime());
		Date startDate = null;
		try {
			startDate = formatter.parse(startDateValue);
		} catch (ParseException ne) {
			return null;
		}

		DateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd ");
		Calendar calender = Calendar.getInstance();
		now.add(Calendar.MONTH, -2);
		String endDateValue = formatter.format(now.getTime());
		Date endDate = null;
		try {
			endDate = formatter.parse(endDateValue);
		} catch (ParseException ne) {
			return null;
		}
		String[] monthName = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };

		Calendar cal = Calendar.getInstance();

		String month = monthName[cal.get(Calendar.MONTH) - 2];
		List<ClothesCollection> monthlyRecords = clothesCollectionRepository.findByCollectionDateBetween(endDate,
				startDate);
		if (null == monthlyRecords) {
			System.out.println("-------------------> Monthly Record is Not Found");
			return null;

		} else {

			System.out.println(" ----------------------->  Monthly Records are Found  " + monthlyRecords.size());
			for (ClothesCollection monthlyRecord : monthlyRecords) {
				if (AppConstants.CONTRIBUTOR_TYPE_RETAILER.equalsIgnoreCase(monthlyRecord.getType())) {

					ClothesCollectionVO vo = new ClothesCollectionVO();

					System.out.println("-------------------------> Sorting Clothes Details");
					SupervisorVO supervisor = supervisorService
							.getClothesCollectionByCollectionId(monthlyRecord.getCollectionid());

					if (null == supervisor) {
						System.out.println("-----------------> Supervisor Data is Not Found");
						continue; // since some retailers might have joined and collection is complete but sorting
									// has not started yet
					} else {
						System.out.println("------------------------> Sorting Clothes Details Are Found");
						vo.setCollectionTotalWeight(supervisor.getTotalWeight());
						vo.setTotalRecyclable(supervisor.getTotalReusable());
						vo.setTotalWaste(supervisor.getTotalWaste());
						Date start = supervisor.getStartDate();						
						if(null != start) {
							vo.setMonth(monthName[start.getMonth()]);
						} else {
							vo.setMonth(month);
						}
						vo.setCollectionId(supervisor.getCollectionId());
					}

					list.add(vo);
				}
			}

		}
		return list;

	}

	@Override
	public List<ClothesCollectionVO> getTopCollectionTotalWeightByRetailers() {
		List<ClothesCollectionVO> list = new ArrayList<ClothesCollectionVO>();

		List<ClothesCollection> collectionWeight = clothesCollectionRepository
				.findTop10ByOrderByCollectionTotalWeightDesc();
		// findTop10ByOrderByLevelDesc()
		if (null == collectionWeight) {
			return null;
		} else {
			for (ClothesCollection collection : collectionWeight) {
				if (AppConstants.CONTRIBUTOR_TYPE_RETAILER.equalsIgnoreCase(collection.getType())) {

					ClothesCollectionVO vo = new ClothesCollectionVO();
					ContributorVO contributor = contributorService.getContributorById(collection.getContributorid());
					if (null == contributor) {
						return null;
					} else {
						vo.setName(contributor.getName());
						vo.setCollectionTotalWeight(collection.getCollectionTotalWeight());
						vo.setType(collection.getType());
					}

					list.add(vo);
				}
			}

		}

		return list;
	}

	@Override
	public List<ClothesCollectionVO> getTopCollectionTotalWeightByContributorType(String contributorTypeSchool) {
		List<ClothesCollectionVO> list = new ArrayList<ClothesCollectionVO>();

		List<ClothesCollection> collectionWeight = clothesCollectionRepository
				.findTop5ByOrderByCollectionTotalWeightDesc();
		if (null == collectionWeight) {
			return null;
		} else {

			for (ClothesCollection collection : collectionWeight) {
				if (AppConstants.CONTRIBUTOR_TYPE_SCHOOL.equalsIgnoreCase(collection.getType())) {

					ClothesCollectionVO vo = new ClothesCollectionVO();
					ContributorVO contributor = contributorService.getContributorById(collection.getContributorid());
					if (null == contributor) {
						return null;
					} else {
						vo.setName(contributor.getName());
						vo.setCollectionTotalWeight(collection.getCollectionTotalWeight());
						vo.setType(collection.getType());
					}

					list.add(vo);
				}
			}

		}

		return list;
	}

	@Override
	public List<ClothesCollectionVO> getByContributor(Integer contributorId) {
		return this.getClothesCollectionVOFromModel(clothesCollectionRepository.findByContributorid(contributorId));
	}

}
