package com.irctn.service;

import java.util.List;

import com.irctn.vo.CentreVO;
import com.irctn.vo.MessageVO;

public interface CentreService {
	
	public String saveCentre(CentreVO centreVO);
	
	public List<CentreVO> getAllCentres();
	
	public String deleteCentre(Integer centreid);
	
	public List<CentreVO> getCentreNameLike(String search);

	public CentreVO getCentresByNames(String search);	//Centre name is unique

	public CentreVO getCentreById(Integer centreid);

	public MessageVO isCentreUnique(String name, String code, Integer centreid);
	
	public MessageVO isCentreNameCodeUnique(String name, String code);
	
	public CentreVO getCentreByHeadId(Integer headId);
	
}
