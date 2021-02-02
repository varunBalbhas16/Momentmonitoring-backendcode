package com.irctn.service;

import java.util.List;

import com.irctn.model.Program;
import com.irctn.vo.ProgramVO;

public interface ProgramService {

	public String savePrograms(ProgramVO programVO);

	public List<ProgramVO> getAllPrograms();

	public String deleteProgram(Integer programid);

	public long getActivePrograms();

	public List<Program> getProgramNameLike(String search);

	public List<ProgramVO> getAllActivePrograms();

	public List<Program> getAllProgramsByNames(String search);

	public ProgramVO getProgramById(Integer programid);
	


}
