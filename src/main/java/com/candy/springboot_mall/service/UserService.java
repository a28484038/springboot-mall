package com.candy.springboot_mall.service;

import java.util.List;

import com.candy.springboot_mall.dto.UserLoginRequest;
import com.candy.springboot_mall.dto.UserRegisterRequest;
import com.candy.springboot_mall.model.User;

public interface UserService {
	
	Integer register(UserRegisterRequest userRegisterRequest);
	
	User getUserById(Integer userId);
	
	User login(UserLoginRequest userLoginRequest);

}
