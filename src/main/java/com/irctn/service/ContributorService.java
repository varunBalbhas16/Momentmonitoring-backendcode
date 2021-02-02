package com.irctn.service;

import java.util.List;

import com.irctn.model.Contributor;
import com.irctn.vo.ContributorVO;

public interface ContributorService {

	public String saveSchoolAndRetailerEntity(ContributorVO contributorVO);

	public List<ContributorVO> getAllEntity();

	public ContributorVO getEntityById(Integer contributorid);

	public String deleteEntity(Integer contributorid);

	public long countByStatusAndType(String type);

	public List<Contributor> getContributorNameLike(String search);
	
	public List<ContributorVO> getAllContributorsByTypeAndNameLike(String type, String name);

	public List<ContributorVO> getAllContributorByNames(String search);

	public List<ContributorVO> getAllContributorsByType(String type);

	public ContributorVO getContributorById(Integer contributorId);

	public ContributorVO getContributorByName(String contributorName);
	
}
