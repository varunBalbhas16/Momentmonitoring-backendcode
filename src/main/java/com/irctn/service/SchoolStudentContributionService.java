package com.irctn.service;

import java.util.List;

import com.irctn.vo.MessageVO;
import com.irctn.vo.SchoolSortingVO;
import com.irctn.vo.SchoolStudentContributionVO;

public interface SchoolStudentContributionService {

	public SchoolStudentContributionVO getSchoolStudentContributionById(Integer id);

	//public MessageVO mapStudentsInSchoolToProgram( SchoolProgramMappingVO contributorId, Integer schoolprogrammappingid);

	//public List<SchoolStudentContributionVO> getSchoolStudentContributionByStudentId(Integer studentId);	
	
	public List<SchoolStudentContributionVO> getSchoolStudentContributionByStudentId(Integer studentId);
	
	public SchoolStudentContributionVO getContributionByStudentAndProgram(Integer schoolprogrammappingid,Integer studentid);

	//public SchoolStudentContributionVO getSchoolStudentContributionById(Integer schoolStudentContributionId);

	public String saveSchoolStudentContribution(SchoolStudentContributionVO schoolStudentContribution);

	public MessageVO mapStudentIdToSchoolStudentContribution(Integer studentId, SchoolSortingVO schoolSortingVO);


}
