package com.irctn.service;

import java.util.List;

import com.irctn.model.Student;
import com.irctn.vo.MessageVO;
import com.irctn.vo.StudentVO;

public interface StudentService {

	public Student getStudentDetails(Integer schoolstudentid);

	public List<StudentVO> getAllStudentDetails();

	public StudentVO getStudentById(Integer id);
	
	public List<StudentVO> getStudentsBySchoolId(Integer contributorid);
	
	public StudentVO getStudentByNameAndGradeAndSchoolId(String name, String grade, Integer contributorid);
	
	public List<StudentVO> getAllStudentByName(String name,Integer contributorid);

	public MessageVO saveStudents(List students);
}
