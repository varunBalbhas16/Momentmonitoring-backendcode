package com.irctn.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_schoolstudent")
public class Student implements Serializable {

	@Id
	@GeneratedValue
	private Integer schoolstudentid;

	private Integer contributorid;

	private Integer studentserialno;

	private String lastname;
	private String preferredname;
	private String house;
	private String registerclass;
	private String barcode;
	
	private String classleader;
	
	/*@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	private List<SchoolStudentContribution> schoolStudentContribution;*/

	public String getClassleader() {
		return classleader;
	}

	public void setClassleader(String classleader) {
		this.classleader = classleader;
	}

	public Integer getSchoolstudentid() {
		return schoolstudentid;
	}

	public void setSchoolstudentid(Integer schoolstudentid) {
		this.schoolstudentid = schoolstudentid;
	}

	public Integer getContributorid() {
		return contributorid;
	}

	public void setContributorid(Integer contributorid) {
		this.contributorid = contributorid;
	}

	public Integer getStudentserialno() {
		return studentserialno;
	}

	public void setStudentserialno(Integer studentserialno) {
		this.studentserialno = studentserialno;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPreferredname() {
		return preferredname;
	}

	public void setPreferredname(String preferredname) {
		this.preferredname = preferredname;
	}

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getRegisterclass() {
		return registerclass;
	}

	public void setRegisterclass(String registerclass) {
		this.registerclass = registerclass;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

}
