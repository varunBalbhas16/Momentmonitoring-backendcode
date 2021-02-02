package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

	public Student findBySchoolstudentid(Integer schoolstudentid);

	public List<Student> findByContributorid(Integer contributorid);

	public List<Student> findByLastnameLike(String lastname);

	public List<Student> findByLastnameLikeAndContributorid(String lastname, Integer contributorid);
	
	//public Student findByContributoridAndNameAndGradeAllIgnoreCase(Integer contributorid, String name, String grade);

	//public Student findByContributoridAndLastnameAndHouseAllIgnoreCase(Integer contributorid, String lastname,
			//String house);

	public Student findByContributoridAndLastnameAndRegisterclassAllIgnoreCase(Integer contributorid, String name,
			String grade);

	//public List<Student> findByNameStartingWith(String name);

	//public List<Student> findByNameLikeAndGradeLike(String name, String grade);

}
