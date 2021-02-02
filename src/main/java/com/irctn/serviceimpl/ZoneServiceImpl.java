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

import com.irctn.model.Zone;
import com.irctn.repository.ZoneRepository;
import com.irctn.service.ZoneService;
import com.irctn.util.AppConstants;
import com.irctn.vo.MessageVO;
import com.irctn.vo.ZoneVO;

@Service
public class ZoneServiceImpl implements ZoneService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.serviceimpl.ZoneServiceImpl");
	
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	ZoneRepository zoneRepository;

	@Override
	public String saveZone(ZoneVO zoneVO) {
		
		Zone zone = zoneRepository.findByZoneid(zoneVO.getZoneId());
		String success = "success";
		if (null == zone) {
			zone = new Zone();

		} else {
			redisTemplate.opsForHash().delete("ZONE_BY_ID", zoneVO.getZoneId());			
			success = "updatesuccess";
		}		
		zone.setZone(zoneVO.getZone());
		zone.setZoneid(zoneVO.getZoneId());
		zone.setDescription(zoneVO.getDescription());
		zone.setCode(zoneVO.getCode());
		
		Zone savedZone = zoneRepository.save(zone);
		LOGGER.debug("The Zone is saved with id : " + savedZone.getZoneid() + " and zone " + savedZone.getZone());
		redisTemplate.opsForHash().delete("ZONELIST", "ZONES");
		return success;
	}

	@Override
	public List<ZoneVO> getAllZones() {
		
		Object object = redisTemplate.opsForHash().get("ZONELIST", "ZONES");
		List<ZoneVO> voList = new ArrayList<ZoneVO>();
		List<Zone> zoneList = null;
		List<ZoneVO> sortedZoneList = null;
		if (null == object) {
			zoneList = zoneRepository.findAll();
		} else {
			return (List<ZoneVO>) object;
		}
		
		if (null != zoneList) {
			for (Zone zone : zoneList) {
				ZoneVO vo = new ZoneVO();
				vo.setZoneId(zone.getZoneid());
				vo.setZone(zone.getZone());
				vo.setCode(zone.getCode());
				vo.setDescription(zone.getDescription());
				voList.add(vo);
			}
			sortedZoneList = voList.stream().sorted(Comparator.comparing(ZoneVO::getZoneId).reversed())
					.collect(Collectors.toList());
			redisTemplate.opsForHash().put("ZONELIST", "ZONES", sortedZoneList);
			return sortedZoneList;

		} else {
			return null;
		}		
	}

	@Override
	public String deleteZone(Integer zoneid) {
		zoneRepository.delete(zoneid);
		redisTemplate.opsForHash().delete("ZONE_BY_ID", zoneid);
		redisTemplate.opsForHash().delete("ZONELIST", "ZONES");
		return "success";
	}

	@Override
	public List<ZoneVO> getZoneNameLike(String search) {
		//List<Zone> zoneList = zoneRepository.findByZoneLike(search);
		List<Zone> zoneList = zoneRepository.findByZoneContaining(search);
		if(null != zoneList) System.out.println("zones mathcing the search are : " + zoneList.size());
		return getVOListFromModel(zoneList);
		
	}

	@Override
	public ZoneVO getZonesByNames(String zonename) {
		Zone zone = zoneRepository.findByZone(zonename);
		return getVOFromModel(zone);
	}

	@Override
	public ZoneVO getZoneById(Integer zoneid) {
		Object object = redisTemplate.opsForHash().get("ZONE_BY_ID", zoneid);
		Zone zone = null;
		if (null == object) {
			zone = zoneRepository.findByZoneid(zoneid);
		} else {
			return (ZoneVO) object;
		}
		if (null != zone) {
			ZoneVO vo = new ZoneVO();
			vo.setZoneId(zone.getZoneid());
			vo.setZone(zone.getZone());
			vo.setCode(zone.getCode());
			vo.setDescription(zone.getDescription());

			redisTemplate.opsForHash().put("ZONE_BY_ID", vo.getZoneId(), vo);
			redisTemplate.opsForHash().delete("ZONELIST", "ZONES");
			return vo;

		} else {
			return null;
		}
	}

	private ZoneVO getVOFromModel(Zone zone){	
		if(null == zone) return null;
		ZoneVO vo = new ZoneVO();
		vo.setZoneId(zone.getZoneid());
		vo.setZone(zone.getZone());
		vo.setCode(zone.getCode());
		vo.setDescription(zone.getDescription());		
		return vo;		
	}
	
	private List<ZoneVO> getVOListFromModel(List<Zone> zones){
		if(null == zones)return null;
		List<ZoneVO> list = new ArrayList<ZoneVO>();
		zones.forEach(zone -> {			
			list.add(getVOFromModel(zone));
		});
		return list;		
	}
	
	public ZoneVO getZoneByCode(String code) {
		if(null == code) return null;
		return getVOFromModel(zoneRepository.findByCode(code));
	}

	@Override
	public MessageVO isZoneUnique(String name, String code, Integer id) {
		ZoneVO zoneToBeUpdated = null;
		ZoneVO zoneFromDB = null;
		if(null == id) return new MessageVO(AppConstants.FAILURE, "Server Error. Please refresh and retry.");
		else 
			zoneToBeUpdated = getZoneById(id);
		
		if(null == name) return new MessageVO(AppConstants.FAILURE, "Please enter a Zone name.");
		else if(null == code) return new MessageVO(AppConstants.FAILURE, "Please enter a valid Zone Code.");
		else if(null != (zoneFromDB = getZonesByNames(name)) && zoneFromDB.getZoneId() != id) {
			return new MessageVO(AppConstants.FAILURE, "Zone already exists. Please check.");
		} else if(null != (zoneFromDB = getZoneByCode(code)) && zoneFromDB.getZoneId() != id) {
			return new MessageVO(AppConstants.FAILURE, "Zone Code already exists. Please check.");
		}
		else {
			return new MessageVO(AppConstants.SUCCESS, "Please add the zone.");
		}
	}
}
