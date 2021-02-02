package com.irctn.controller;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.irctn.service.AdminService;
import com.irctn.service.ClothesCollectionService;
import com.irctn.service.StudentService;
import com.irctn.service.SupervisorService;
import com.irctn.util.AppConstants;
import com.irctn.vo.ClothesCollectionVO;
import com.irctn.vo.MessageVO;
import com.irctn.vo.SchoolAdminVO;
import com.irctn.vo.StudentVO;

@RestController
@RequestMapping(value = "/api/home")
public class SchoolAdminController {

	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.controller.SchoolAdminController");

	@Autowired
	StudentService studentService;

	@Autowired
	SupervisorService supervisorService;
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	ClothesCollectionService clothesCollectionService;
	

	@CrossOrigin
	@GetMapping("/getStudentHistory")
	public List<StudentVO> getAllStudentDetails() {
		return studentService.getAllStudentDetails();

	}

	@CrossOrigin
	@GetMapping("/getStudentById/{id}")
	public StudentVO getStudentById(@PathVariable Integer id) {
		
		return studentService.getStudentById(id);

	}

	
	
	@CrossOrigin
	@GetMapping("/getSchoolAdminDetails")
	public List<SchoolAdminVO> getSchoolAdminDetails() {
		return supervisorService.getAllSchoolAdminDetails();

	}
	
	@CrossOrigin
	@GetMapping("/getSchoolContributions/{schoolId}")
	public List<SchoolAdminVO> getSchoolById(@PathVariable Integer schoolId) {
		return supervisorService.getSchoolContributions(schoolId);

	}
	
	@CrossOrigin
	@GetMapping("/getSchoolStudents/{schoolId}")
	public List<StudentVO> getStudentsBySchool(@PathVariable Integer schoolId) {
		
		List<StudentVO> list = studentService.getStudentsBySchoolId(schoolId);
		
		//list.stream().sorted((p1, p2) -> p2.getTotalWeight().compareTo(p1.getTotalWeight())).collect(sortedList);
		List<StudentVO> sortedList = list.stream()
				.sorted(Comparator.comparing(StudentVO::getTotalWeight).reversed())
				.collect(Collectors.toList());
		return sortedList;

	}

	@CrossOrigin
	@PostMapping(value = "/uploadfile")
	public MessageVO uploadingFiles(@RequestParam("uploadingFile") MultipartFile uploadingFile,
			@RequestParam("schoolKey") String folderName) {
        System.out.println("----------------------------> Before saving file");
		File uploadingdirs = new File(AppConstants.UPLOAD_FOLDER + folderName);
		uploadingdirs.mkdirs();
		System.out.println("----------------------------------> After Creating File Name");
		String excelFilePath = uploadingdirs + "" + File.separatorChar + uploadingFile.getOriginalFilename();
		File file = new File(excelFilePath);
		System.out.println("----------------------------------> File is uploading");
		try {
			uploadingFile.transferTo(file);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			return new MessageVO("failure","failed to upload file","contact schooladmin");
		}
        System.out.println("-------------------------------> Upload success");
        
        MessageVO message = adminService.addStudentsToSchool(folderName, excelFilePath);
        return new MessageVO("success","File Uploaded Successfully","Successfully Saved");
	}

	
	@CrossOrigin
	@GetMapping("getTopContributors")
	public List<ClothesCollectionVO> getTopContributors(){
		
		return clothesCollectionService.getPreviousOneMonthContribution(AppConstants.CONTRIBUTOR_TYPE_SCHOOL,AppConstants.STATUS_CLOSED);
		
		
		
	}

}
