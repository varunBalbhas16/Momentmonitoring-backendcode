package com.irctn.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irctn.model.SchoolStudentContribution;
import com.irctn.model.Student;
import com.irctn.repository.SchoolStudentContributionRepository;
import com.irctn.service.SchoolStudentContributionService;
import com.irctn.service.StudentService;
import com.irctn.vo.MessageVO;
import com.irctn.vo.SchoolSortingVO;
import com.irctn.vo.SchoolStudentContributionVO;

@Service
public class SchoolStudentContributionServiceImpl implements SchoolStudentContributionService {

	@Autowired
	StudentService studentService;

	@Autowired
	SchoolStudentContributionRepository schoolStudentContributionRepository;

	@Override
	public SchoolStudentContributionVO getSchoolStudentContributionById(Integer id) {
		SchoolStudentContribution studentContribution = schoolStudentContributionRepository.findBySchoolstudentcontributionid(id);
		SchoolStudentContributionVO vo = new SchoolStudentContributionVO();
		if (null == studentContribution) {
			return null;
		} else {
			Student student = studentService.getStudentDetails(studentContribution.getSchoolstudentid());
			if (null == student) {
				return null;
			} else {
				vo.setSchoolStudentContributionId(studentContribution.getSchoolstudentcontributionid());
				vo.setSchoolStudentId(student.getSchoolstudentid());
				vo.setName(student.getLastname());
				vo.setGrade(student.getPreferredname());
				vo.setClassLeader(student.getClassleader());
				vo.setHouse(student.getHouse());
				vo.setBarcode(student.getBarcode());
				vo.setRollNumber(student.getStudentserialno());
			}
		}
		return vo;
	}

	@Override
	public SchoolStudentContributionVO getContributionByStudentAndProgram(Integer schoolprogrammappingid,
			Integer studentid) {
		// TODO Auto-generated method stub
		SchoolStudentContribution schoolStudentContribution = schoolStudentContributionRepository.findBySchoolstudentidAndSchoolprogrammappingid(studentid,schoolprogrammappingid);
		
		return  getSchoolStudentContributionById(schoolStudentContribution.getSchoolstudentcontributionid());
	}

	@Override
	public String saveSchoolStudentContribution(SchoolStudentContributionVO schoolStudentContributionVO) {
		SchoolStudentContribution studentContribution =  schoolStudentContributionRepository.findBySchoolstudentcontributionid(schoolStudentContributionVO.getSchoolStudentContributionId());
		studentContribution.setBagweight(schoolStudentContributionVO.getBagWeight());
		studentContribution.setSchoolstudentid(schoolStudentContributionVO.getSchoolStudentId());
		schoolStudentContributionRepository.save(studentContribution);
		return "success";
	}

	@Override
	public List<SchoolStudentContributionVO> getSchoolStudentContributionByStudentId(Integer studentId) {
		
		List<SchoolStudentContribution> studentContributions = schoolStudentContributionRepository.findBySchoolstudentid(studentId);
		if(null == studentContributions) return null;
		List<SchoolStudentContributionVO> contributions = new ArrayList<SchoolStudentContributionVO>();
		for(SchoolStudentContribution studentContribution : studentContributions) {
			contributions.add(getVOFromModel(studentContribution));
		}
		return contributions;
	}
	
	private SchoolStudentContributionVO getVOFromModel(SchoolStudentContribution schoolStudentContribution){
		if(null == schoolStudentContribution)return null;
		SchoolStudentContributionVO vo = new SchoolStudentContributionVO();
		vo.setSchoolStudentId(schoolStudentContribution.getSchoolstudentid());
		vo.setBagWeight(schoolStudentContribution.getBagweight());
		vo.setSchoolStudentContributionId(schoolStudentContribution.getSchoolstudentcontributionid());
		return vo;
		
	}

	@Override
	public MessageVO mapStudentIdToSchoolStudentContribution(Integer studentId, SchoolSortingVO schoolSortingVO) {
		SchoolStudentContribution schoolStudentContribution = new SchoolStudentContribution();
		schoolStudentContribution.setBagweight(schoolSortingVO.getBagWeight());
		schoolStudentContribution.setSchoolstudentid(studentId);
		schoolStudentContribution.setSchoolprogrammappingid(schoolSortingVO.getSchoolProgramMappingId());
		SchoolStudentContribution saveSchoolStudentContribution = schoolStudentContributionRepository.save(schoolStudentContribution);
		
		if(null != saveSchoolStudentContribution){
			return new MessageVO("success","saved Successfully","saved Successfully",saveSchoolStudentContribution.getSchoolstudentcontributionid(),"success");
		}else{
			return new MessageVO("failure","failed to save","Failed to Successfully");
		
		
	}
	}
}


