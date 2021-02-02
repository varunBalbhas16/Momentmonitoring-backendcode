package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.CentreDepartmentUser;

public interface CentreDepartmentUserRepository extends JpaRepository<CentreDepartmentUser, Integer> {

		public CentreDepartmentUser findByCentredepartmentuserid(Integer centredepartmentuserid);
		
		public CentreDepartmentUser findByUserid(Integer userid);
		
		public List<CentreDepartmentUser> findByRoleid(Integer roleid);
		
		public CentreDepartmentUser findByCentredepartmentid(Integer centredepartmentid);
		
		public List<CentreDepartmentUser> findByUseridIn(List<Integer> userids);
	
}
