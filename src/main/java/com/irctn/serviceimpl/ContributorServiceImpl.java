package com.irctn.serviceimpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.irctn.model.Contributor;
import com.irctn.repository.ContributorRepository;
import com.irctn.service.ContributorService;
import com.irctn.util.AppConstants;
import com.irctn.vo.ContributorVO;

@Service
public class ContributorServiceImpl implements ContributorService {

	@Autowired
	ContributorRepository contributorRepository;

	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	
	@Override
	public String saveSchoolAndRetailerEntity(ContributorVO contributorVO) {

		Contributor contributor = contributorRepository.findByContributorid(contributorVO.getContributorid());
		String success = "success";
		if(null == contributor) {
			contributor = new Contributor();
		}
		else {
			redisTemplate.opsForHash().delete("CONTRIBUTOR_BY_ID", contributorVO.getContributorid());
			success = "updatesuccess";
		}
		
		contributor.setEmail(contributorVO.getEmail());
		contributor.setContactperson(contributorVO.getContactperson());
		contributor.setAddress(contributorVO.getAddress());
		contributor.setLandmark(contributorVO.getLandmark());
		contributor.setMobilenumber(contributorVO.getMobilenumber());
		contributor.setName(contributorVO.getName());
		contributor.setStatus(contributorVO.getStatus());
		contributor.setType(contributorVO.getType());
		contributor.setStatus(AppConstants.STATUS_ACTIVE);
		contributorRepository.save(contributor);
		
		redisTemplate.opsForHash().delete("CONTRIBUTORS", "CONTRIBUTORS_ALL");
		
		return success;
	}

	@Override
	public List<ContributorVO> getAllEntity() {
		Object object = redisTemplate.opsForHash().get("CONTRIBUTORS", "CONTRIBUTORS_ALL");
		if(null != object) {
			return (List<ContributorVO>) object;
			
		} else {
			List<Contributor> contributors = contributorRepository.findAll();
			if(null == contributors || contributors.isEmpty()) {
				return null;
			} else {
				List<ContributorVO> contributorList = new ArrayList<ContributorVO>();
				List<ContributorVO> sortedList = null;
				for (Contributor contributor : contributors) {
					ContributorVO vo = new ContributorVO();
					vo.setContributorid(contributor.getContributorid());
					vo.setName(contributor.getName());
					vo.setType(contributor.getType());
					vo.setEmail(contributor.getEmail());
					vo.setMobilenumber(contributor.getMobilenumber());
					vo.setContactperson(contributor.getContactperson());
					vo.setAddress(contributor.getAddress());
					vo.setLandmark(contributor.getLandmark());
					vo.setStatus(contributor.getStatus());
					contributorList.add(vo);
				}
				sortedList = contributorList.stream()
						.sorted(Comparator.comparing(ContributorVO::getContributorid).reversed())
						.collect(Collectors.toList());
				redisTemplate.opsForHash().put("CONTRIBUTORS", "CONTRIBUTORS_ALL", sortedList);
				return sortedList; 
			}
		}
	}

	@Override
	public ContributorVO getEntityById(Integer contributorid) {
		Object object = redisTemplate.opsForHash().get("CONTRIBUTOR_BY_ID", contributorid);
		if(null != object) {
			return (ContributorVO) object;
			
		} else {
			Contributor contributor = contributorRepository.findByContributorid(contributorid);
			if(null == contributor) { 
				return null;
				
			} else {
				ContributorVO vo = new ContributorVO();
				vo.setContributorid(contributor.getContributorid());
				vo.setName(contributor.getName());
				vo.setType(contributor.getType());
				vo.setEmail(contributor.getEmail());
				vo.setMobilenumber(contributor.getMobilenumber());
				vo.setContactperson(contributor.getContactperson());
				vo.setAddress(contributor.getAddress());
				vo.setLandmark(contributor.getLandmark());
				vo.setStatus(contributor.getStatus());
				redisTemplate.opsForHash().put("CONTRIBUTOR_BY_ID", contributorid, vo);
				redisTemplate.opsForHash().delete("CONTRIBUTORS", "CONTRIBUTORS_ALL");
				return vo;
			}
		}
	}

	
	@Override
	public String deleteEntity(Integer contributorid) {
		contributorRepository.delete(contributorid);
		redisTemplate.opsForHash().delete("CONTRIBUTOR_BY_ID", contributorid);
		redisTemplate.opsForHash().delete("CONTRIBUTORS", "CONTRIBUTORS_ALL");
		return "success";
	}

	@Override
	public long countByStatusAndType(String type) {
		return  contributorRepository.countByStatusAndType(AppConstants.STATUS_ACTIVE, type);
	}

	@Override
	public List<Contributor> getContributorNameLike(String search) {
		return contributorRepository.findDistinctByNameLike(AppConstants.qualifySearchString(search));
	}

	@Override
	public List<ContributorVO> getAllContributorByNames(String search) {
		List<Contributor> contributorNamesList = contributorRepository.findByNameLike(AppConstants.qualifySearchString(search));
		List<ContributorVO> voList = new ArrayList<ContributorVO>();
		if (null == contributorNamesList) {
			return null;
		} else {
			for (Contributor contributor : contributorNamesList) {
				ContributorVO vo = new ContributorVO();
				vo.setName(contributor.getName());
				vo.setContributorid(contributor.getContributorid());
				voList.add(vo);
			}
		}
		return voList;
	}

	@Override
	public List<ContributorVO> getAllContributorsByType(String type) {
		List<Contributor> contributors = contributorRepository.findByType(type);
		return getContributorVOList(contributors);
	}

	private List<ContributorVO> getContributorVOList(List<Contributor> contributorList){
		if(null == contributorList) return null;
		List<ContributorVO> list = new ArrayList<ContributorVO>(contributorList.size());
		for(Contributor contributor : contributorList) {
			list.add(getContributorVOFromModel(contributor));
		}
		return list;
	}
	
	private ContributorVO getContributorVOFromModel(Contributor contributor) {
		if(null == contributor) return null;
		ContributorVO vo = new ContributorVO();
		vo.setContributorid(contributor.getContributorid());
		vo.setName(contributor.getName());
		vo.setType(contributor.getType());
		vo.setEmail(contributor.getEmail());
		vo.setMobilenumber(contributor.getMobilenumber());
		vo.setContactperson(contributor.getContactperson());
		vo.setAddress(contributor.getAddress());
		vo.setLandmark(contributor.getLandmark());
		vo.setStatus(contributor.getStatus());
		return vo;
	}

	@Override
	public List<ContributorVO> getAllContributorsByTypeAndNameLike(String type, String name) {
		List<Contributor> contributors = contributorRepository.findByTypeAndNameLike(type, name);
		return getContributorVOList(contributors);
	}

	@Override
	public ContributorVO getContributorById(Integer contributorId) {
		return getContributorVOFromModel(contributorRepository.findByContributorid(contributorId));
		
	}

	@Override
	public ContributorVO getContributorByName(String contributorName) {
	
		return getContributorVOFromModel(contributorRepository.findByName(contributorName));
	}
}
