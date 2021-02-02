package com.irctn.service;

import java.util.List;

import com.irctn.vo.MessageVO;
import com.irctn.vo.ZoneVO;

public interface ZoneService {

	public String saveZone(ZoneVO zoneVO);
	
	public List<ZoneVO> getAllZones();
	
	public String deleteZone(Integer zoneid);
	
	public List<ZoneVO> getZoneNameLike(String search);

	public ZoneVO getZonesByNames(String search);	//Zone name is unique

	public ZoneVO getZoneById(Integer zoneid);

	public MessageVO isZoneUnique(String name, String code, Integer id);
	
}
