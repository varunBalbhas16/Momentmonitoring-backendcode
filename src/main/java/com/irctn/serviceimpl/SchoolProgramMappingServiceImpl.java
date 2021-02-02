package com.irctn.serviceimpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irctn.model.SchoolProgramMapping;
import com.irctn.repository.SchoolProgramMappingRepository;
import com.irctn.service.ContributorService;
import com.irctn.service.ProgramService;
import com.irctn.service.SchoolProgramMappingService;
import com.irctn.service.SchoolStudentContributionService;
import com.irctn.service.StudentService;
import com.irctn.util.AppConstants;
import com.irctn.vo.ContributorVO;
import com.irctn.vo.ProgramVO;
import com.irctn.vo.SchoolProgramMappingVO;

@Service
public class SchoolProgramMappingServiceImpl implements SchoolProgramMappingService {

	@Autowired
	SchoolProgramMappingRepository schoolProgramMappingRepository;

	@Autowired
	ProgramService programService;

	@Autowired
	ContributorService contributorService;

	@Autowired
	SchoolStudentContributionService schoolStudentContributionService;

	@Autowired
	StudentService studentService;

	@Override
	public String saveContributorProgramMapping(SchoolProgramMappingVO schoolProgramVO) {

		if (null == schoolProgramVO)
			return AppConstants.FAILURE;

		if (null != schoolProgramVO.getSchoolProgrammappingId()) {
			System.out.println("---------------Cannot update a contributor program mapping. Plz check.");
			return AppConstants.FAILURE;
		}
		Integer programid = schoolProgramVO.getProgramId();
		Integer contributorid = schoolProgramVO.getContributorId();
		if (null == contributorid || null == programid) {
			System.out.println("--------SchoolStudentContributionMapping error - Null parameters to create mapping "
					+ schoolProgramVO.getContributorName() + ":" + schoolProgramVO.getProgramName());
			return AppConstants.FAILURE;
		}

		SchoolProgramMapping schoolsAndPrograms = schoolProgramMappingRepository
				.findByProgramidAndContributorid(programid, contributorid);
		if (null != schoolsAndPrograms) {
			System.out.println("---------------Mapping already done for the same school and program. Plz check.");
			return AppConstants.FAILURE;
		}
		String success = AppConstants.SUCCESS;

		schoolsAndPrograms = new SchoolProgramMapping();
		System.out.println("--------------------------> Mapping Type is Retailer");
		schoolsAndPrograms.setContributorid(schoolProgramVO.getContributorId());
		schoolsAndPrograms.setProgramid(schoolProgramVO.getProgramId());
		schoolsAndPrograms.setStartdate(schoolProgramVO.getStartDate());
		schoolsAndPrograms.setEnddate(schoolProgramVO.getEndDate());
		schoolsAndPrograms.setStatus(2);
		schoolProgramMappingRepository.save(schoolsAndPrograms);
		ProgramVO programVO = programService.getProgramById(schoolProgramVO.getProgramId());
		schoolProgramVO.setProgramName(programVO.getProgramName());
		return success;
	}

	@Override
	public List<SchoolProgramMappingVO> getAllSchoolsByPrograms() {
		List<SchoolProgramMapping> schoolProgramMappingList = schoolProgramMappingRepository.findAll();
		List<SchoolProgramMappingVO> voList = new ArrayList<SchoolProgramMappingVO>();
		List<SchoolProgramMappingVO> sortedList = null;
		if (null == schoolProgramMappingList) {
			return null;
		} else {
			for (SchoolProgramMapping schoolMapping : schoolProgramMappingList) {
				SchoolProgramMappingVO vo = new SchoolProgramMappingVO();
				ProgramVO program = programService.getProgramById(schoolMapping.getProgramid());
				if (null == program) {
					return null;
				} else {
					vo.setSchoolProgrammappingId(schoolMapping.getSchoolprogrammappingid());
					vo.setProgramId(program.getProgramId());
					vo.setProgramName(program.getProgramName());
					if (AppConstants.STATUS_INPROGRESS.equals(schoolMapping.getStatus())) {
						vo.setStatusName(AppConstants.STATUS_PROGRESS);
					}
					ContributorVO contributor = contributorService.getContributorById(schoolMapping.getContributorid());
					if (null == contributor) {
						return null;
					} else {
						vo.setType(contributor.getType());
						vo.setContributorName(contributor.getName());
						voList.add(vo);
					}
				}
			}
			sortedList = voList.stream()
					.sorted(Comparator.comparing(SchoolProgramMappingVO::getSchoolProgrammappingId).reversed())
					.collect(Collectors.toList());
			return sortedList;
		}
	}

	@Override
	public List<SchoolProgramMappingVO> getAllProgramsByContributors(Integer id) {
		List<SchoolProgramMapping> schoolProgramMapping = schoolProgramMappingRepository.findByContributorid(id);
		List<SchoolProgramMappingVO> voList = new ArrayList<SchoolProgramMappingVO>();
		if (null == schoolProgramMapping) {
			return null;
		} else {
			for (SchoolProgramMapping schoolPrograms : schoolProgramMapping) {
				SchoolProgramMappingVO vo = new SchoolProgramMappingVO();
				ContributorVO contributor = contributorService.getContributorById(schoolPrograms.getContributorid());
				if (null == contributor) {
					return null;
				} else {
					vo.setType(contributor.getType());
					ProgramVO program = programService.getProgramById(schoolPrograms.getProgramid());
					if (null == program) {
						return null;
					} else {
						vo.setProgramName(program.getProgramName());
						vo.setProgramId(program.getProgramId());
						voList.add(vo);
					}
				}

			}

		}

		return voList;
	}

	private SchoolProgramMappingVO getVOFromModel(SchoolProgramMapping schoolProgramMapping) {
		if (null == schoolProgramMapping)
			return null;
		SchoolProgramMappingVO vo = new SchoolProgramMappingVO();
		vo.setContributorId(schoolProgramMapping.getContributorid());
		vo.setProgramId(schoolProgramMapping.getProgramid());
		vo.setSchoolProgrammappingId(schoolProgramMapping.getSchoolprogrammappingid());
		vo.setStartDate(schoolProgramMapping.getStartdate());
		vo.setEndDate(schoolProgramMapping.getEnddate());
		ContributorVO contributorVO = contributorService.getEntityById(schoolProgramMapping.getContributorid());
		if (null != contributorVO && null != contributorVO.getName()) {
			vo.setContributorName(contributorVO.getName());
		}
		ProgramVO programVO = programService.getProgramById(schoolProgramMapping.getProgramid());
		if (null != programVO && null != programVO.getProgramName()) {
			vo.setProgramName(programVO.getProgramName());
		}
		return vo;

	}

	private List<SchoolProgramMappingVO> getSchoolProgramMappingVOFromModel(
			List<SchoolProgramMapping> schoolProgramMappingList) {
		List<SchoolProgramMappingVO> list = new ArrayList<SchoolProgramMappingVO>();
		schoolProgramMappingList.forEach(schoolProgramMapping -> {
			list.add(getVOFromModel(schoolProgramMapping));
		});
		return list;

	}

	@Override
	public List<SchoolProgramMappingVO> getSchoolProgramMappingByContributorId(Integer contributorId) {
		return getSchoolProgramMappingVOFromModel(schoolProgramMappingRepository.findByContributorid(contributorId));
	}

	@Override
	public SchoolProgramMappingVO getSchoolProgramMappingByContributorIdAndProgramId(Integer contributorId,
			Integer programId) {
		return getVOFromModel(schoolProgramMappingRepository.findByProgramidAndContributorid(programId, contributorId));

	}

}
