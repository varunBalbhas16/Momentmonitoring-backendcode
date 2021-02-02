package com.irctn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.irctn.model.User;
import com.irctn.service.MobileService;
import com.irctn.service.UserService;
import com.irctn.util.AppConstants;
import com.irctn.vo.UserVO;
import com.irctn.vo.mobile.UserVOMobile;

@RestController
@RequestMapping(value = "/api/home/")
public class LoginController {

	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.controller.LoginController");

	@Autowired
	UserService userService;
	
	@Autowired
	MobileService mobileService;

	@CrossOrigin
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public UserVO login(@RequestBody User user) {
		System.out.println("Login Page");
		UserVO email = userService.verifyEmail(user.getEmail());
		if ("failure".equalsIgnoreCase(email.getResult())) {
			System.out.println("-----------------------------> Email is not Exist Please Check");
			return new UserVO("User does not exist");
			
		} else {
			
			UserVO loginUser = userService.verifyLogin(user.getEmail(), user.getPassword());
			if(AppConstants.SUCCESS.equals(loginUser.getResult())) {
				
				System.out.println("The role of this user is : " + loginUser.getRoleName());
				if(!( AppConstants.ROLE_ADMIN.equalsIgnoreCase(loginUser.getRoleName())
						|| AppConstants.ROLE_CENTRE_HEAD.equalsIgnoreCase(loginUser.getRoleName())
						|| AppConstants.ROLE_TICKET_ADMIN.equalsIgnoreCase(loginUser.getRoleName()))					
					) {
					
					return new UserVO("Sorry. You do not have access rights to access this website.");
				}
				loginUser.setResult(null);
				return loginUser;
				
			} else {
				System.out.println("User do not Exist");
				return new UserVO("Invalid credentials");
			}			
		}
	}
	
	@CrossOrigin
	@RequestMapping(value = "/mlogin", method = RequestMethod.POST)
	public UserVOMobile mlogin(@RequestBody User user) {
		if(null == user) {
			return new UserVOMobile("Please enter valid user credentials.");
		}
		System.out.println("Mobile Login Page " + user.getEmail());
		UserVO email = userService.verifyEmail(user.getEmail());
		if ("failure".equalsIgnoreCase(email.getResult())) {
			System.out.println("-----------------------------> Email is not Exist Please Check");
			return new UserVOMobile("User does not exist");
		} else {
			UserVO loginUser = userService.verifyLogin(user.getEmail(), user.getPassword());
			System.out.println("Verified Successfully");
			if (null != loginUser) {
				System.out.println("User Login successful");
				// return userService.findByUsername(loginUser.getUsername());
				return mobileService.getUser(loginUser);
			} else {
				System.out.println("User is not Exist");
				return new UserVOMobile("Invalid credentials");
			}
		}
	}

}
