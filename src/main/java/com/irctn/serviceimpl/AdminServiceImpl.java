package com.irctn.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irctn.model.Student;
import com.irctn.service.AdminService;
import com.irctn.service.ClothesCategoryService;
import com.irctn.service.ClothesCollectionService;
import com.irctn.service.ContributorService;
import com.irctn.service.DriverVehicleService;
import com.irctn.service.ProgramService;
import com.irctn.service.RetailerSortingBoxCategoryService;
import com.irctn.service.StudentService;
import com.irctn.service.SupervisorService;
import com.irctn.service.TicketService;
import com.irctn.service.TicketTripNavigationService;
import com.irctn.service.TicketTripService;
import com.irctn.util.AppConstants;
import com.irctn.vo.AdminDashboardVO;
import com.irctn.vo.ClothesCategoryVO;
import com.irctn.vo.ClothesCollectionVO;
import com.irctn.vo.ContributionHistogramVO;
import com.irctn.vo.ContributionsDoneVO;
import com.irctn.vo.ContributorVO;
import com.irctn.vo.DriverAttendanceRankingVO;
import com.irctn.vo.MessageVO;
import com.irctn.vo.RetailerContributedClothesVO;
import com.irctn.vo.RetailerSortingBoxDetailVO;
import com.irctn.vo.SchoolContributedClothesVO;
import com.irctn.vo.StudentVO;
import com.irctn.vo.SupervisorVO;
import com.irctn.vo.TicketDPMRankingVO;
import com.irctn.vo.TicketHistogramVO;
import com.irctn.vo.TicketVO;
import com.irctn.vo.TripHistogramVO;

@Service
public class AdminServiceImpl implements AdminService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.serviceimpl.AdminServiceImpl");

	@Autowired
	ProgramService programService;

	@Autowired
	ContributorService contributorService;

	@Autowired
	StudentService studentService;

	@Autowired
	SupervisorService supervisorService;

	@Autowired
	ClothesCollectionService clothesCollectionService;

	@Autowired
	ClothesCategoryService clothesCategoryService;

	@Autowired
	RetailerSortingBoxCategoryService retailerSortingBoxCategoryService;
	
	@Autowired
	TicketService ticketService;
	
	@Autowired
	TicketTripService ticketTripService;
	
	@Autowired
	TicketTripNavigationService ticketTripNavigationService;
	
	@Autowired
	DriverVehicleService driverVehicleService; 

	@Override
	public AdminDashboardVO getAdminDashboard() {
		LOGGER.debug("Admin Service Imple ............ getAdmin Dash");
		
		// Total weight of clothes sorted till date in Kilograms
		// List<ProgramVO> programs = programService.getActivePrograms();
		long programs = programService.getActivePrograms();
		// finalList.add(programs);
		long schools = contributorService.countByStatusAndType(AppConstants.CONTRIBUTOR_TYPE_SCHOOL);
		long retailers = contributorService.countByStatusAndType(AppConstants.CONTRIBUTOR_TYPE_RETAILER);
		// List<ContributorVO> schools =
		// contributorService.countByStatusAndType(AppConstants.CONTRIBUTOR_TYPE_SCHOOL);
		// List<ContributorVO> retailers =
		// contributorService.countByStatusAndType(AppConstants.CONTRIBUTOR_TYPE_RETAILER);

		/*
		 * 1. Above you are getting all the schools who are active. Get all the
		 * total weight contributed by these schools 2. From this you will get
		 * total Weight of Clothes sorted and how much is total weight of
		 * clothes usable 3. For the retailers, you wil know how much slothes
		 * have been sorted. Just get the total sorted weight for all retailers.
		 * 4. From this get the top 5 schools and top 5 retailers. 5. To find
		 * the collection values for this month, get todays date and get the
		 * month. So pass the from and to based on the month to get monthly
		 * Collection. Here you will know this month contribution, And fir the
		 * previous month, minus 1 from the month and do the same process to get
		 * the Collection details and thereby the sorting details.
		 */
		// 5. Total weight of usable recycled clothes sorted till date in
		// Kilograms
		double totalClothesSorted = 0D;
		double totalClothesUsable = 0D;
		List<SupervisorVO> totalClothes = supervisorService.countByStatus();
		if (null == totalClothes) {
			System.out.println("------------------------------> Null Value in Total Clothes");
			return null;
		} else {
			System.out.println("-----------------------------> Size of List of Batch Closed" + totalClothes.size());
			for (SupervisorVO clothes : totalClothes) {
				System.out.println(
						"-------------------------> Weight of Individulal Contributor " + clothes.getTotalWeight());
				totalClothesSorted = totalClothesSorted + clothes.getTotalWeight();
				

				if (null == clothes.getTotalReusable()) {
					totalClothesUsable = 0D;
				} else {
					totalClothesUsable = totalClothesUsable + clothes.getTotalReusable();
				}

			}

		}
		String currentMonthName = null;
		String previousMonthName = null;
		String previousTwoMonthName = null;

		Double schoolCurrentMonthTotalContribution = 0D;
		Double schoolPreviousMonthTotalContribution = 0D;
		Double schoolPreviousTwoMonthTotalContribution = 0D;

		Double schoolCurrentMonthTotalRecyclable = 0D;
		Double schoolPreviousMonthTotalRecyclable = 0D;
		Double schoolPreviousTwoMonthTotalRecyclable = 0D;

		Double schoolTotalWaste = 0D;
		Double schoolPreviousMonthWaste = 0D;
		Double schoolPreviousTwoMonthWaste = 0D;

		Double retailerCurrentMonthTotalContribution = 0D;
		Double retailerPreviousMonthTotalContribution = 0D;
		Double retailerPreviousTwoMonthTotalContribution = 0D;

		Double retailerTotalRecyclable = 0D;

		Double retailerTotalWaste = 0D;

		List<ClothesCollectionVO> schoolCurrentMonthlyContributions = clothesCollectionService
				.getTypeAndStatus(AppConstants.CONTRIBUTOR_TYPE_SCHOOL, AppConstants.STATUS_CLOSED);
		if (null == schoolCurrentMonthlyContributions) {
			System.out.println("--------------------------------------------> Return ning null for school contributions are empty...");
			return null;
		} else {
			for (ClothesCollectionVO vo : schoolCurrentMonthlyContributions) {
				System.out.println("--------------------------------------------> Month Name" + vo.getMonth());
				System.out.println("----------------------->Size of List" + schoolCurrentMonthlyContributions.size());
				currentMonthName = vo.getMonth();
				System.out.println("---------------------------------------------------> Individula Weights "
						+ vo.getCollectionTotalWeight());
				schoolCurrentMonthTotalContribution = schoolCurrentMonthTotalContribution
						+ vo.getCollectionTotalWeight();
				schoolCurrentMonthTotalRecyclable = schoolCurrentMonthTotalRecyclable + vo.getTotalRecyclable();
				schoolTotalWaste = schoolTotalWaste + vo.getTotalWaste();
			}
		}
		List<ClothesCollectionVO> schoolPreviousMonthlyContributions = clothesCollectionService
				.getPreviousOneMonthContribution(AppConstants.CONTRIBUTOR_TYPE_SCHOOL, AppConstants.STATUS_CLOSED);
		if (null == schoolPreviousMonthlyContributions) {
			return null;
		}
		for (ClothesCollectionVO vo : schoolPreviousMonthlyContributions) {
			System.out.println("----------------------->Size of List" + schoolPreviousMonthlyContributions.size());
			previousMonthName = vo.getMonth();
			if(null != vo.getCollectionTotalWeight()) {
				schoolPreviousMonthTotalContribution = schoolPreviousMonthTotalContribution + vo.getCollectionTotalWeight();
			}
			if(null != vo.getTotalRecyclable()) {
				schoolPreviousMonthTotalRecyclable = schoolPreviousMonthTotalRecyclable + vo.getTotalRecyclable();
			}
			if(null != vo.getTotalWaste()) {
				schoolPreviousMonthWaste = schoolPreviousMonthWaste + vo.getTotalWaste();
			}

		}
		
		/*
		List<ClothesCollectionVO> schoolPreviousTwoMonthlyContributions = clothesCollectionService
				.getPreviousTwoMonthContribution(AppConstants.CONTRIBUTOR_TYPE_SCHOOL, AppConstants.STATUS_CLOSED);
				
		if (null == schoolPreviousTwoMonthlyContributions) {
			return null;
		}
		for (ClothesCollectionVO vo : schoolPreviousTwoMonthlyContributions) {
			previousTwoMonthName = vo.getMonth();
			System.out.println("--------------------------------------------> Month Name" + vo.getMonth());

			if(null != vo.getCollectionTotalWeight()) {
				schoolPreviousTwoMonthTotalContribution = schoolPreviousTwoMonthTotalContribution
						+ vo.getCollectionTotalWeight();
			}
			if(null != vo.getTotalRecyclable()) {
				schoolPreviousTwoMonthTotalRecyclable = schoolPreviousTwoMonthTotalRecyclable + vo.getTotalRecyclable();
			}
			if(null != vo.getTotalWaste()) {
				schoolPreviousTwoMonthWaste = schoolPreviousTwoMonthWaste + vo.getTotalWaste();
			}
		}
		
		List<ClothesCollectionVO> retailerCurrentMonthlyContribution = clothesCollectionService
				.getRetailerCurrenteMonthContribution();
		if (null == retailerCurrentMonthlyContribution) {
			return null;
		}
		for (ClothesCollectionVO vo : retailerCurrentMonthlyContribution) {
			currentMonthName = vo.getMonth();
			retailerCurrentMonthTotalContribution = retailerCurrentMonthTotalContribution
					+ vo.getCollectionTotalWeight();

		}
		List<ClothesCollectionVO> retailerPreviousMonthlyContribution = clothesCollectionService
				.getRetailerPreviousOneMonthContribution();
		if (null == retailerPreviousMonthlyContribution) {
			return null;
		}
		for (ClothesCollectionVO vo : retailerPreviousMonthlyContribution) {
			previousMonthName = vo.getMonth();

			retailerPreviousMonthTotalContribution = retailerPreviousMonthTotalContribution
					+ vo.getCollectionTotalWeight();

		}
		List<ClothesCollectionVO> retailerPreviousTwoMonthlyContribution = clothesCollectionService
				.getRetailerPreviousTwoMonthContribution();
		if (null == retailerPreviousTwoMonthlyContribution) {
			return null;
		}
		for (ClothesCollectionVO vo : retailerPreviousTwoMonthlyContribution) {
			previousTwoMonthName = vo.getMonth();
			retailerPreviousTwoMonthTotalContribution = retailerPreviousTwoMonthTotalContribution
					+ vo.getCollectionTotalWeight();
		}
*/
		////Monthly Trip Statistics
		List<ContributionHistogramVO> schoolMonthlyContributions = new ArrayList<ContributionHistogramVO>();
		schoolMonthlyContributions.add(new ContributionHistogramVO(currentMonthName,
				schoolCurrentMonthTotalContribution, schoolCurrentMonthTotalRecyclable, schoolTotalWaste));
		schoolMonthlyContributions.add(new ContributionHistogramVO(previousMonthName,
				schoolPreviousMonthTotalContribution, schoolPreviousMonthTotalRecyclable, schoolPreviousMonthWaste));
		schoolMonthlyContributions
				.add(new ContributionHistogramVO(previousTwoMonthName, schoolPreviousTwoMonthTotalContribution,
						schoolPreviousTwoMonthTotalRecyclable, schoolPreviousTwoMonthWaste));
		LOGGER.debug("-------------------> gettting Monthly Trip Stats.........................");
		List<TripHistogramVO> monthlyTripStatistics = new ArrayList<TripHistogramVO>();
		monthlyTripStatistics.add(ticketService.getPreviousLastMonthTripStats());
		monthlyTripStatistics.add(ticketService.getLastMonthTripStats());
		monthlyTripStatistics.add(ticketService.getThisMonthTripStats());

		List<ContributionHistogramVO> retailerMonthlyContributions = new ArrayList<ContributionHistogramVO>();
		retailerMonthlyContributions.add(new ContributionHistogramVO(currentMonthName,
				retailerCurrentMonthTotalContribution, retailerTotalRecyclable, retailerTotalWaste));
		retailerMonthlyContributions.add(new ContributionHistogramVO(previousMonthName,
				retailerPreviousMonthTotalContribution, retailerTotalRecyclable, retailerTotalWaste));
		retailerMonthlyContributions.add(new ContributionHistogramVO(previousTwoMonthName,
				retailerPreviousTwoMonthTotalContribution, retailerTotalRecyclable, retailerTotalWaste));
		
		LOGGER.debug("-------------------> gettting Monthly Ticket Stats.........................");
		//monthly Ticket statistics
		List<TicketHistogramVO> monthlyTicketStatistics = new ArrayList<TicketHistogramVO>();
		monthlyTicketStatistics.add(ticketService.getPreviousLastMonthTicketStats());
		monthlyTicketStatistics.add(ticketService.getLastMonthTicketStats());
		monthlyTicketStatistics.add(ticketService.getThisMonthTicketStats());
		
		
		/*
		List<ContributionsDoneVO> topSchools = getContributorStatistics(clothesCollectionService.getTopCollectionTotalWeightBySchools(), "school");
		List<ContributionsDoneVO> topRetailers = getContributorStatistics(clothesCollectionService.getTopCollectionTotalWeightByRetailers(), "retailer");
		*/
		
		
		/* - already commented - so remove this code block
		String schoolHighestContributorName = null;
		String schoolSecondHighestContributorName = null;
		String schoolThirdHighestContributorName = null;
		String schoolFourthHighestContributorName = null;
		String schoolFifthHighestContributorName = null;

		Double schoolHighestTotalWeight = 0D;
		Double schoolSecondHighestTotalWeight = 0D;
		Double schoolThirdHighestTotalWeight = 0D;
		Double schoolFourthHighestTotalWeight = 0D;
		Double schoolFifthHighestTotalWeight = 0D;
		
		List<ClothesCollectionVO> topSchoolsList = clothesCollectionService.getTopCollectionTotalWeightBySchools();
		List<String> topSchoolNames = null;
		List<Double> topSchoolWeights = null;
		if (null == topSchoolsList) {
			return null;
		} else {
			topSchoolNames = new ArrayList<String>(topSchoolsList.size());
			topSchoolWeights = new ArrayList<Double>(topSchoolsList.size());
			
			for(ClothesCollectionVO topSchool : topSchoolsList) {
				topSchoolNames.add(topSchool.getName());
				topSchoolWeights.add(topSchool.getCollectionTotalWeight());
			}
			
			schoolHighestContributorName = topSchoolsList.get(0).getName();
			schoolSecondHighestContributorName = topSchoolsList.get(1).getName();
			schoolThirdHighestContributorName = topSchoolsList.get(2).getName();
			schoolFourthHighestContributorName = topSchoolsList.get(3).getName();
			schoolFifthHighestContributorName = topSchoolsList.get(4).getName();

			schoolHighestTotalWeight = topSchoolsList.get(0).getCollectionTotalWeight();
			schoolSecondHighestTotalWeight = topSchoolsList.get(1).getCollectionTotalWeight();
			schoolThirdHighestTotalWeight = topSchoolsList.get(2).getCollectionTotalWeight();
			schoolFourthHighestTotalWeight = topSchoolsList.get(3).getCollectionTotalWeight();
			schoolFifthHighestTotalWeight = topSchoolsList.get(4).getCollectionTotalWeight();
			

		}
		
		String retailerHighestContributorName = null;
		String retailersecondHighestContributorName = null;
		String retailerThirdHighestContributorName = null;
		String retailerFourthHighestContributorName = null;
		String retailerFifthHighestContributorName = null;

		Double retailerHighestTotalWeight = 0D;
		Double retailerSecondHighestTotalWeight = 0D;
		Double retailerThirdHighestTotalWeight = 0D;
		Double retailerFourthHighestTotalWeight = 0D;
		Double retailerFifthThirdHighestTotalWeight = 0D;
		
		
		List<ClothesCollectionVO> topRetailersList = clothesCollectionService.getTopCollectionTotalWeightByRetailers();
		if (null == topRetailersList) {
			return null;
		} else {
			retailerHighestContributorName = topRetailersList.get(0).getName();
			retailersecondHighestContributorName = topRetailersList.get(1).getName();
			retailerThirdHighestContributorName = topRetailersList.get(2).getName();
			retailerFourthHighestContributorName = topRetailersList.get(3).getName();
			retailerFifthHighestContributorName = topRetailersList.get(4).getName();

			retailerHighestTotalWeight = topRetailersList.get(0).getCollectionTotalWeight();
			retailerSecondHighestTotalWeight = topRetailersList.get(1).getCollectionTotalWeight();
			retailerThirdHighestTotalWeight = topRetailersList.get(2).getCollectionTotalWeight();
			retailerFourthHighestTotalWeight = topRetailersList.get(3).getCollectionTotalWeight();
			retailerFifthThirdHighestTotalWeight = topRetailersList.get(4).getCollectionTotalWeight();

			System.out.println("---------------------------------------> Index value" + topRetailersList.get(0));

		}

		List<ContributionsDoneVO> topSchools = new ArrayList<ContributionsDoneVO>();
		topSchools.add(new ContributionsDoneVO(1, schoolHighestContributorName, "school", schoolHighestTotalWeight));
		topSchools.add(new ContributionsDoneVO(2, schoolSecondHighestContributorName, "school",
				schoolSecondHighestTotalWeight));
		topSchools.add(
				new ContributionsDoneVO(3, schoolThirdHighestContributorName, "school", schoolThirdHighestTotalWeight));
		topSchools.add(new ContributionsDoneVO(4, schoolFourthHighestContributorName, "school",
				schoolFourthHighestTotalWeight));
		topSchools.add(
				new ContributionsDoneVO(5, schoolFifthHighestContributorName, "school", schoolFifthHighestTotalWeight));

		List<ContributionsDoneVO> topRetailers = new ArrayList<ContributionsDoneVO>();
		topRetailers.add(
				new ContributionsDoneVO(1, retailerHighestContributorName, "retailer", retailerHighestTotalWeight));
		topRetailers.add(new ContributionsDoneVO(2, retailersecondHighestContributorName, "retailer",
				retailerSecondHighestTotalWeight));
		topRetailers.add(new ContributionsDoneVO(3, retailerThirdHighestContributorName, "retailer",
				retailerThirdHighestTotalWeight));
		topRetailers.add(new ContributionsDoneVO(4, retailerFourthHighestContributorName, "retailer",
				retailerFourthHighestTotalWeight));
		topRetailers.add(new ContributionsDoneVO(5, retailerFifthHighestContributorName, "retailer",
				retailerFifthThirdHighestTotalWeight));
*/
		//
				//
		
		LOGGER.debug("-------------------> gettting Closed Tikcets Stats.........................");
		//Max Tickets By DPMs
		List<TicketDPMRankingVO> ticketDPMRanks = new ArrayList<TicketDPMRankingVO>();
		List<TicketDPMRankingVO> ticketsDPMFromDB = ticketService.getClosedTicketsByDateRange();
		if(null != ticketsDPMFromDB && !ticketsDPMFromDB.isEmpty()) {
			if(ticketsDPMFromDB.size() > 19) {
				ticketDPMRanks.addAll(ticketsDPMFromDB.subList(0, 19));
			} else {
				ticketDPMRanks.addAll(ticketsDPMFromDB);
			}
			
		}
		/*
		ticketDPMRanks.add(new TicketDPMRankingVO(1, "Murugadoss. P", 46));
		ticketDPMRanks.add(new TicketDPMRankingVO(2, "ThamizhSelvan. N.R", 42));
		ticketDPMRanks.add(new TicketDPMRankingVO(3, "Bosco Julius. C", 32));
		*/
		
		List<DriverAttendanceRankingVO> driverAttendanceRanks = new ArrayList<DriverAttendanceRankingVO>();
		List<DriverAttendanceRankingVO> driversFromDB = driverVehicleService.getDriversAttendanceRankingByDateRange();
		if(null != driversFromDB && !driversFromDB.isEmpty()) {
			if(driversFromDB.size() > 19) {
				driverAttendanceRanks.addAll(driversFromDB.subList(0, 19));
			} else {
				driverAttendanceRanks.addAll(driversFromDB);
			}
		}
		/*
		driverAttendanceRanks.add(new DriverAttendanceRankingVO(1, "ChellaMuthu. P", 31));
		driverAttendanceRanks.add(new DriverAttendanceRankingVO(2, "Mohammed Ariff. N.R", 31));
		driverAttendanceRanks.add(new DriverAttendanceRankingVO(3, "Vendhan Thamizh. C", 30));
		*/
		
		
		DecimalFormat df = new DecimalFormat("#.##");
		totalClothesSorted = Double.parseDouble(df.format(totalClothesSorted));
		
		AdminDashboardVO vo = new AdminDashboardVO();
		vo.setTickets(getAllTickets());
		vo.setOpenTickets(getOpenTickets());
		vo.setTripKms(getTotalKms());
		vo.setTripCost(df.format(vo.getTripKms() * AppConstants.TRIP_COST_PER_KM));
		vo.setDriversAttendance("250/155");
		
		//vo.setActiveRetailers(retailers);	//Trip KMS
		//vo.setTotalWeightOfSortedClothes(totalClothesSorted);	//tripCost 
		//vo.setTotalWeightOfUsableClothes(totalClothesUsable);
		//vo.setSchoolMonthlyContribution(schoolMonthlyContributions);	//Monthly Trip Statistics		
		//vo.setRetailerMonthlyContribution(retailerMonthlyContributions);	//Monthly Ticket Statistics
		
		vo.setMonthlyTicketStatistics(monthlyTicketStatistics);
		vo.setMonthlyTripStatistics(monthlyTripStatistics);
		
		vo.setTicketDPMRanking(ticketDPMRanks);		
		vo.setDriverAttendanceRanking(driverAttendanceRanks);	
		
		//vo.setTopSchools(topSchools);			//Max Trips By DPM
		//vo.setTopRetailers(topRetailers);		//Staff Attendance Statistics
		/* */
		return vo;

	}

	private double getTotalKms() {
		// TODO Auto-generated method stub
		return this.ticketTripService.getTripDistances();
	}

	private int getOpenTickets() {
		List<TicketVO> tickets = this.ticketService.getOpenTickets();
		if(null == tickets || tickets.isEmpty())		return 0;
		else return tickets.size();
	}

	private int getAllTickets() {
		return this.ticketService.getAllTickets().size();
	}
	
	

	private List<ContributionsDoneVO> getContributorStatistics(List<ClothesCollectionVO> topContributors, String type) {
		if(null == topContributors) return null;
		List<ContributionsDoneVO> contributors = new ArrayList<ContributionsDoneVO>(topContributors.size());
		int i=1;
		for(ClothesCollectionVO contributor : topContributors) {			
			contributors.add( new ContributionsDoneVO(i , contributor.getName(), type, contributor.getCollectionTotalWeight() ));
			i++;
		}
		return contributors;		
	}

	@Override
	public MessageVO addStudentsToSchool(String schoolKey, String excelFilePath) {
		Integer key = null;
		try {
			key = new Integer(schoolKey);
		} catch (NumberFormatException nfe) {
			return new MessageVO(AppConstants.FAILURE, "Invalid School Key.",
					"Unable to save students due to invalid school key");
		}
		if (null != key) {
			// Step 1: Check if the school exists in DB. Else return Messago VO
			// that there is no school for this key
			ContributorVO contributor = contributorService.getContributorById(key);
			if (null == contributor) {
				return new MessageVO(AppConstants.FAILURE, "Invalid School Key.",
						"Please create the school and then try to add students.");
			}
			// Step 2: If school is there call getStudentList and get the List
			// of students
			List<Student> students = new ArrayList<Student>();
			try {
				students = getStudentList(key, excelFilePath);
			} catch (IOException ioe) {
				return new MessageVO(AppConstants.FAILURE, "Could not read the uploaded file.",
						"Unable to save students due to issues in the uploaded file.");
			}

			List<StudentVO> studentsVO = studentService.getStudentsBySchoolId(key);
			if (null == studentsVO || studentsVO.size() == 0) {
				System.out.println("-------------------> No Student is Found");
				Student noName = new Student();
				noName.setLastname("NO NAME");
				noName.setContributorid(key);
				students.add(noName);

			}
			return saveStudents(students, key);

		} else {
			return new MessageVO(AppConstants.FAILURE, "Invalid School Key.",
					"Unable to save students due to invalid school key");
		}

	}

	private MessageVO saveStudents(List<Student> students, Integer key) {
		int savedStudents = 0;
		List<Student> newStudents = new ArrayList<Student>();
		int studentsEntered = 1;
		for (Student student : students) {
			// Step 3: Check if the student already exists. If yes, leave him.
			StudentVO studentVO = studentService.getStudentByNameAndGradeAndSchoolId(student.getLastname(),
					student.getRegisterclass(), key);
			// Step 4: If the student is not there save the student.
			if (null == studentVO) {
				newStudents.add(student);
				savedStudents++;
				studentsEntered++;
			}
		}
		return studentService.saveStudents(newStudents);
	}

	private List<Student> getStudentList(Integer schoolKey, String excelFilePath) throws IOException {

		List<Student> studentList = new ArrayList<Student>();
		FileInputStream fis = new FileInputStream(new File(excelFilePath));
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet worksheet = workbook.getSheetAt(0);
		int schoolSerialNo = 1;

		System.out.println("Before Iterating Excel Datas -------------------" + worksheet.getPhysicalNumberOfRows());
		for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

			System.out.println("After the Loop");
			Student student = new Student();
			XSSFRow row = worksheet.getRow(i);
			System.out.println("The Row " + row.getRowNum());
			System.out.println("Problem in Rows");
			System.out.println("----------------> Cell value" + row.getCell(0).getStringCellValue());
			student.setContributorid(schoolKey);
			student.setStudentserialno(schoolSerialNo);
			String contributorId = Integer.toString(schoolKey);
			String studentSerialNo = Integer.toString(schoolSerialNo);
			String formattedContributorId = ("0000" + contributorId).substring(contributorId.length());
			String formattedstudentSerialNo = ("0000" + studentSerialNo).substring(studentSerialNo.length());
			String barcode = formattedContributorId.concat(formattedstudentSerialNo);
			student.setBarcode(barcode);
			student.setLastname(row.getCell(0).getStringCellValue());
			student.setPreferredname((row.getCell(1).getStringCellValue()));
			if (null != row.getCell(2).getStringCellValue()) {

				student.setHouse((row.getCell(2).getStringCellValue()));

			} else {
				student.setHouse("");
			}

			// int registerClass = (int) row.getCell(4).getNumericCellValue();
			student.setRegisterclass((row.getCell(3).getStringCellValue()));
			studentList.add(student);
			schoolSerialNo++;
		}

		return studentList;
	}

	@Override
	public SchoolContributedClothesVO getSchoolContributedClothes() {
		Double category1 = 0D;
		Double category2 = 0D;
		Double category3 = 0D;
		Double category4 = 0D;
		Double category5 = 0D;
		Double category6 = 0D;
		Double category7 = 0D;
		Double category8 = 0D;
		Double category9 = 0D;
		Double category10 = 0D;
		List<ClothesCategoryVO> schoolContributedClothes = clothesCategoryService.getSchoolContribedClothes();
		if (null == schoolContributedClothes) {
			return null;
		} else {
			for (ClothesCategoryVO vo : schoolContributedClothes) {
				System.out.println("----------------------> Category1 Sorted Clothes" + vo.getCategory1());

				if (null != vo.getCategory1()) {

					category1 = vo.getCategory1() + category1;
				}
				if (null != vo.getCategory2()) {

					category2 = vo.getCategory2() + category2;
				}
				if (null != vo.getCategory3()) {

					category3 = category3 + vo.getCategory3();
				}
				if (null != vo.getCategory4()) {

					category4 = category4 + vo.getCategory4();
				}
				if (null != vo.getCategory5()) {

					category5 = category5 + vo.getCategory5();
				}
				if (null != vo.getCategory6()) {

					category6 = category6 + vo.getCategory6();
				}
				if (null != vo.getCategory7()) {

					category7 = category7 + vo.getCategory7();
				}
				if (null != vo.getCategory8()) {

					category8 = category8 + vo.getCategory8();
				}
				if (null != vo.getCategory9()) {

					category9 = category9 + vo.getCategory9();
				}
				if (null != vo.getCategory10()) {

					category10 = category10 + vo.getCategory10();
				}
			}
		}
		SchoolContributedClothesVO vo = new SchoolContributedClothesVO();
		vo.setCategory1(category1);
		vo.setCategory2(category2);
		vo.setCategory3(category3);
		vo.setCategory4(category4);
		vo.setCategory5(category5);
		vo.setCategory6(category6);
		vo.setCategory7(category7);
		vo.setCategory8(category8);
		vo.setCategory9(category9);
		vo.setCategory10(category10);

		return vo;
	}

	@Override
	public RetailerContributedClothesVO getRetailerContributedClothes() {
		Double clothes = 0D;
		Double clothesWeight = 0D;
		Double socks_Pair = 0D;
		Double socksPairWeight = 0D;
		Double underwear = 0D;
		Double underWearWeight = 0D;
		Double hand_Bags = 0D;
		Double handBagsWeight = 0D;
		Double shoes_Pair = 0D;
		Double shoesPairWeight = 0D;
		Double jewelleryAndAccessories = 0D;
		Double jewelleryAndAccessoriesWeight = 0D;
		Double hats = 0D;
		Double hatsWeight = 0D;
		Double watches = 0D;
		Double watchesWeight = 0D;
		Double householdgoods = 0D;
		Double houseHoldGoodsWeight = 0D;
		Double others = 0D;
		Double othersWeight = 0D;
		List<RetailerSortingBoxDetailVO> retailerContributedList = retailerSortingBoxCategoryService
				.getRetailerContributedClothesDetails();
		if (null == retailerContributedList) {
			return null;
		} else {
			for (RetailerSortingBoxDetailVO retailerContribution : retailerContributedList) {
				if (AppConstants.CLOTHES.equals(retailerContribution.getRetailerSubcategoryId())) {
					clothes = clothes + retailerContribution.getItems();
					clothesWeight = clothesWeight + retailerContribution.getWeight();
				}
				if (AppConstants.HAND_BAGS.equals(retailerContribution.getRetailerSubcategoryId())) {
					hand_Bags = hand_Bags + retailerContribution.getItems();
					handBagsWeight = handBagsWeight + retailerContribution.getWeight();
				}
				if (AppConstants.HATS.equals(retailerContribution.getRetailerSubcategoryId())) {
					hats = hats + retailerContribution.getItems();
					hatsWeight = hatsWeight + retailerContribution.getWeight();
				}
				if (AppConstants.HOUSEHOLDGOODS.equals(retailerContribution.getRetailerSubcategoryId())) {
					householdgoods = householdgoods + retailerContribution.getItems();
					houseHoldGoodsWeight = houseHoldGoodsWeight + retailerContribution.getWeight();
				}
				if (AppConstants.JEWELLERYANDACCESSORIES.equals(retailerContribution.getRetailerSubcategoryId())) {
					jewelleryAndAccessories = jewelleryAndAccessories + retailerContribution.getItems();
					jewelleryAndAccessoriesWeight = jewelleryAndAccessoriesWeight + retailerContribution.getWeight();
				}
				if (AppConstants.UNDERWEAR.equals(retailerContribution.getRetailerSubcategoryId())) {
					underwear = underwear + retailerContribution.getItems();
					underWearWeight = underWearWeight + retailerContribution.getWeight();
				}
				if (AppConstants.SHOES_PAIR.equals(retailerContribution.getRetailerSubcategoryId())) {
					shoes_Pair = shoes_Pair + retailerContribution.getItems();
					shoesPairWeight = shoesPairWeight + retailerContribution.getWeight();
				}
				if (AppConstants.SOCKS_PAIR.equals(retailerContribution.getRetailerSubcategoryId())) {
					socks_Pair = socks_Pair + retailerContribution.getItems();
					socksPairWeight = socksPairWeight + retailerContribution.getWeight();
				}
				if (AppConstants.WATCHES.equals(retailerContribution.getRetailerSubcategoryId())) {
					watches = watches + retailerContribution.getItems();
					watchesWeight = watchesWeight + retailerContribution.getWeight();
				}
				if (AppConstants.OTHERS.equals(retailerContribution.getRetailerSubcategoryId())) {
					others = others + retailerContribution.getItems();
					othersWeight = othersWeight + retailerContribution.getWeight();
				}
			}
		}
		RetailerContributedClothesVO vo = new RetailerContributedClothesVO();
		vo.setClothes(clothes);
		vo.setHand_Bags(hand_Bags);
		vo.setHats(hats);
		vo.setHouseholdgoods(householdgoods);
		vo.setJewelleryAndAccessories(jewelleryAndAccessories);
		vo.setOthers(others);
		vo.setShoes_Pair(shoes_Pair);
		vo.setSocks_Pair(socks_Pair);
		vo.setUnderwear(underwear);
		vo.setWatches(watches);
		vo.setClothesWeight(clothesWeight);
		vo.setHandBagsWeight(handBagsWeight);
		vo.setHatsWeight(hatsWeight);
		vo.setHouseHoldGoodsWeight(houseHoldGoodsWeight);
		vo.setJewelleryAndAccessoriesWeight(jewelleryAndAccessoriesWeight);
		vo.setOthersWeight(othersWeight);
		vo.setShoesPairWeight(shoesPairWeight);
		vo.setSocksPairWeight(socksPairWeight);
		vo.setUnderWearWeight(underWearWeight);
		vo.setWatchesWeight(watchesWeight);
		return vo;
	}

}
