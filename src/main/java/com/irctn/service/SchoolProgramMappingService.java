package com.irctn.service;

import java.util.List;

import com.irctn.vo.SchoolProgramMappingVO;

public interface SchoolProgramMappingService {

	public String saveContributorProgramMapping(SchoolProgramMappingVO schoolProgramVO);

	public List<SchoolProgramMappingVO> getAllSchoolsByPrograms();

	public List<SchoolProgramMappingVO> getAllProgramsByContributors(Integer id);

	public List<SchoolProgramMappingVO> getSchoolProgramMappingByContributorId(Integer contributorId);
	
	public SchoolProgramMappingVO getSchoolProgramMappingByContributorIdAndProgramId(Integer contributorId,Integer programId);
}
