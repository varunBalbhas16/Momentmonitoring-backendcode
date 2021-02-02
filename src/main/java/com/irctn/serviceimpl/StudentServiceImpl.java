package com.irctn.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.irctn.model.Student;
import com.irctn.repository.StudentRepository;
import com.irctn.service.ClothesCollectionService;
import com.irctn.service.SchoolStudentContributionService;
import com.irctn.service.StudentService;
import com.irctn.util.AppConstants;
import com.irctn.vo.MessageVO;
import com.irctn.vo.SchoolStudentContributionVO;
import com.irctn.vo.StudentVO;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	ClothesCollectionService clothesCollectionService;
	
	@Autowired
	SchoolStudentContributionService schoolStudentContributionService;

	@Autowired
	RedisTemplate<Object, Object> redisTemplate;

	@Override
	public Student getStudentDetails(Integer schoolstudentid) {

		return studentRepository.findBySchoolstudentid(schoolstudentid);
	}

	@Override
	public List<StudentVO> getAllStudentDetails() {
		List<Student> studentList = studentRepository.findAll();
		List<StudentVO> voList = new ArrayList<StudentVO>();
		if (null == studentList) {
			return null;
		} else {
			for (Student student : studentList) {
				StudentVO vo = new StudentVO();
				vo.setStudentName(student.getPreferredname());
				vo.setHouse(student.getHouse());
				vo.setGrade(student.getRegisterclass());
				vo.setStudentId(student.getSchoolstudentid());
				voList.add(vo);
			}

		}
		return voList;

	}

	@Override
	public List<StudentVO> getStudentsBySchoolId(Integer contributorid) {
		if(null == contributorid) return null;
		List<Student> students = studentRepository.findByContributorid(contributorid);
		if(null == students) return null;
		List<StudentVO> list = new ArrayList<StudentVO>();
		for(Student student : students) {
			StudentVO vo = new StudentVO();
			vo.setStudentId(student.getSchoolstudentid());
			vo.setContributorId(student.getContributorid());
			vo.setStudentName(student.getPreferredname());
			vo.setGrade(student.getRegisterclass());
			vo.setHouse(student.getHouse());
			
			List<SchoolStudentContributionVO> contributions = schoolStudentContributionService.getSchoolStudentContributionByStudentId(student.getSchoolstudentid());
			if(null != contributions ) {
				double totalWeight = 0D;
				for(SchoolStudentContributionVO contribution : contributions) {
					if(null != contribution && null != contribution.getBagWeight()) {	//at times we get null for bag weights
						totalWeight = totalWeight + contribution.getBagWeight();
					}
				}
				vo.setTotalWeight(totalWeight);
			}
			//vo.setStudentClassLeader(student.getRegisterclass());
			//vo.setRollNumber(student.getStudentserialno());			
			list.add(vo);
		}
		
		return list;
	}

	@Override
	public StudentVO getStudentById(Integer id) {
		Object object = redisTemplate.opsForHash().get("STUDENT_BY_ID", id);
		StudentVO vo = new StudentVO();
		Student student = null;
		if (null == object) {
			student = studentRepository.findBySchoolstudentid(id);
		} else {
			return (StudentVO) object;
		}
		if (null != student) {
			vo.setStudentName(student.getLastname());
			vo.setGrade(student.getRegisterclass());
			vo.setRollNumber(""+student.getStudentserialno());
			vo.setStudentClassLeader(student.getClassleader());
			vo.setHouse(student.getHouse());
			vo.setBarcode(student.getBarcode());
			vo.setStudentId(student.getSchoolstudentid());
			vo.setContributorId(student.getContributorid());
			redisTemplate.opsForHash().put("STUDENT_BY_ID", vo.getStudentId(), vo);
			redisTemplate.opsForHash().delete("STUDENTLIST", "STUDENTS");
			return vo;
		} else {
			return null;
		}

	}
	
	private StudentVO getVOFromModel(Student student){
		if(null == student)return null;
		StudentVO vo = new StudentVO();
//		vo.setStudentName(student.getLastname());
//		vo.setGrade(student.getPreferredname());
//		vo.setStudentClassLeader(student.getRegisterclass());
//		vo.setStudentId(student.getSchoolstudentid());
//		vo.setContributorId(student.getContributorid());
		vo.setStudentName(student.getPreferredname() + " " + student.getLastname());
		vo.setGrade(student.getRegisterclass());
		vo.setRollNumber(""+student.getStudentserialno());
		vo.setStudentClassLeader(student.getClassleader());
		vo.setHouse(student.getHouse());
		vo.setBarcode(student.getBarcode());
		vo.setStudentId(student.getSchoolstudentid());
		vo.setContributorId(student.getContributorid());
		return vo;
	}
	
	private List<StudentVO> getVOListFromModel(List<Student> students){
		if(null == students)return null;
		List<StudentVO> list = new ArrayList<StudentVO>();
		students.forEach(student ->{
			list.add(getVOFromModel(student));
		});
		return list;
		
	}

	@Override
	public List<StudentVO> getAllStudentByName(String name,Integer contributorid) { 
		return getVOListFromModel(studentRepository.findByLastnameLikeAndContributorid("%" +name + "%",contributorid));
	}

	@Override
	public MessageVO saveStudents(List students) {
		List savedStudents = studentRepository.save(students);
		if(null == savedStudents) {
			return new MessageVO("Unable to save students.", AppConstants.FAILURE);
		} else {
			return new MessageVO(savedStudents.size() + " students were saved successfully", AppConstants.SUCCESS);
		}
	}

	@Override
	public StudentVO getStudentByNameAndGradeAndSchoolId(String name, String grade, Integer contributorid) {
		return getVOFromModel(studentRepository.findByContributoridAndLastnameAndRegisterclassAllIgnoreCase(contributorid, name,grade ));
	}

}
