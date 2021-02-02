package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.SchoolProgramMapping;
import com.irctn.vo.SchoolProgramMappingVO;

public interface SchoolProgramMappingRepository extends JpaRepository<SchoolProgramMapping, Integer> {

	public SchoolProgramMapping findBySchoolprogrammappingid(Integer schoolprogrammappingid);

	public List<SchoolProgramMapping> findByProgramid(Integer programid);

	public List<SchoolProgramMapping> findByContributorid(Integer contributorid);

	public SchoolProgramMapping findBySchoolprogrammappingid(SchoolProgramMappingVO schoolProgramVo);

	public SchoolProgramMapping findByProgramidAndContributorid(Integer programid, Integer contributorid);

}
