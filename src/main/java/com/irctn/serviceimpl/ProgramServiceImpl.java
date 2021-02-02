package com.irctn.serviceimpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.irctn.model.Program;
import com.irctn.repository.ProgramRepository;
import com.irctn.service.ProgramService;
import com.irctn.util.AppConstants;
import com.irctn.vo.ProgramVO;

@Service
public class ProgramServiceImpl implements ProgramService {

	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.serviceimpl.ProgramServiceImpl");

	@Autowired
	ProgramRepository programRepository;

	@Autowired
	RedisTemplate<Object, Object> redisTemplate;

	@Override
	public String savePrograms(ProgramVO programVO) {

		Program program = programRepository.findByProgramid(programVO.getProgramId());
		String success = "success";
		if (null == program) {
			program = new Program();

		} else {
			redisTemplate.opsForHash().delete("PROGRAM_BY_ID", programVO.getProgramId());
			success = "updatesuccess";
		}
		program.setProgramname(programVO.getProgramName());
		program.setPurpose(programVO.getPurpose());
		program.setStartdate(programVO.getStartDate());
		System.out.println("StartDate" + programVO.getStartDate());
		program.setEnddate(programVO.getEndDate());
		System.out.println("Enddate" + programVO.getEndDate());
		program.setStatus(AppConstants.STATUS_ACTIVE);
		Program savePogram = programRepository.save(program);
		System.out.println(
				"Saved Successfully" + savePogram.getStartdate() + "And End Date is " + savePogram.getEnddate());
		LOGGER.debug("Start Date" + savePogram.getStartdate() + "End Date is" + savePogram.getEnddate());
		redisTemplate.opsForHash().delete("PROGRAMLIST", "PROGRAMS");

		return success;

	}

	@Override
	public List<ProgramVO> getAllPrograms() {
		Object object = redisTemplate.opsForHash().get("PROGRAMLIST", "PROGRAMS");
		List<ProgramVO> voList = new ArrayList<ProgramVO>();
		List<Program> programList = null;
		List<ProgramVO> sortedProgramList = null;
		if (null == object) {
			programList = programRepository.findAll();
		} else {
			return (List<ProgramVO>) object;
		}
		if (null != programList) {
			for (Program program : programList) {
				ProgramVO vo = new ProgramVO();
				System.out.println("fetching programs:" + program.getProgramname());
				vo.setProgramId(program.getProgramid());
				vo.setProgramName(program.getProgramname());
				vo.setPurpose(program.getPurpose());
				vo.setStartDate(program.getStartdate());
				vo.setEndDate(program.getEnddate());
				vo.setStatus(program.getStatus());
				voList.add(vo);
			}
			sortedProgramList = voList.stream().sorted(Comparator.comparing(ProgramVO::getProgramId).reversed())
					.collect(Collectors.toList());
			redisTemplate.opsForHash().put("PROGRAMLIST", "PROGRAMS", sortedProgramList);
			return sortedProgramList;

		} else {
			return null;
		}
	}

	@Override
	public ProgramVO getProgramById(Integer programid) {
		Object object = redisTemplate.opsForHash().get("PROGRAM_BY_ID", programid);
		Program program = null;
		if (null == object) {
			program = programRepository.findByProgramid(programid);
		} else {
			return (ProgramVO) object;
		}
		if (null != program) {
			ProgramVO vo = new ProgramVO();
			vo.setProgramName(program.getProgramname());
			vo.setProgramId(program.getProgramid());
			vo.setPurpose(program.getPurpose());
			vo.setStartDate(program.getStartdate());
			vo.setEndDate(program.getEnddate());
			vo.setStatus(program.getStatus());

			redisTemplate.opsForHash().put("PROGRAM_BY_ID", vo.getProgramId(), vo);
			redisTemplate.opsForHash().delete("PROGRAMLIST", "PROGRAMS");
			return vo;

		} else {
			return null;
		}
	}

	@Override
	public String deleteProgram(Integer programid) {

		programRepository.delete(programid);
		redisTemplate.opsForHash().delete("PROGRAM_BY_ID", programid);
		redisTemplate.opsForHash().delete("PROGRAMLIST", "PROGRAMS");
		return "success";
	}

	@Override
	public long getActivePrograms() {
		return  programRepository.countByStatus(AppConstants.STATUS_ACTIVE);
	}

	@Override
	public List<Program> getProgramNameLike(String search) {

		return programRepository.findDistinctByprogramnameLike("%" + search + "%");
	}

	@Override
	public List<ProgramVO> getAllActivePrograms() {
		List<Program> programs = programRepository.findAll();
		System.out.println("fetching programs:" + programs);
		List<ProgramVO> programList = new ArrayList<ProgramVO>();
		if (null == programs) {
			return null;
		} else {
			for (Program program : programs) {
				System.out.println("fetching programs:" + program.getProgramname());
				if (AppConstants.STATUS_ACTIVE.equals(program.getStatus())) {
					ProgramVO vo = new ProgramVO();
					vo.setProgramId(program.getProgramid());
					vo.setProgramName(program.getProgramname());
					vo.setPurpose(program.getPurpose());
					vo.setStartDate(program.getStartdate());
					vo.setEndDate(program.getEnddate());
					vo.setStatus(program.getStatus());
					programList.add(vo);
				}
			}
		}
		return programList;
	}

	@Override
	public List<Program> getAllProgramsByNames(String search) {
		Object object = redisTemplate.opsForHash().get("PROGRAM_BY_NAMES", search);
		List<Program> programList = null;
		if (null == object) {
			programList = programRepository.findByProgramnameLike(search);
		} else {
			return (List<Program>) object;
		}
		if (null == programList) {
			return null;
		} else {
			redisTemplate.opsForHash().put("PROGRAM_BY_NAMES", search, programList);
		}
		return programList;

	}
	
	private ProgramVO getVOFromModel(Program program){
		
		ProgramVO vo = new ProgramVO();
		vo.setProgramId(program.getProgramid());
		vo.setProgramName(program.getProgramname());
		vo.setStartDate(program.getStartdate());
		vo.setEndDate(program.getEnddate());
		vo.setPurpose(program.getPurpose());
		vo.setStatus(program.getStatus());
		
		return vo;
		
	}
	
	private List<ProgramVO> getVOListFromModel(List<Program> programs){
		if(null == programs)return null;
		List<ProgramVO> list = new ArrayList<ProgramVO>();
		programs.forEach(program -> {
			
			list.add(getVOFromModel(program));
		});
		return list;
		
	}
}
