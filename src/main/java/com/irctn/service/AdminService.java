package com.irctn.service;

import com.irctn.vo.AdminDashboardVO;
import com.irctn.vo.MessageVO;
import com.irctn.vo.RetailerContributedClothesVO;
import com.irctn.vo.SchoolContributedClothesVO;

public interface AdminService {

	public AdminDashboardVO getAdminDashboard();
	
	public MessageVO addStudentsToSchool(String schoolKey, String excelFilePath);

	public SchoolContributedClothesVO getSchoolContributedClothes();

	public RetailerContributedClothesVO getRetailerContributedClothes();

	
}
