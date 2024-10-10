package com.candy.springboot_mall.dao;

import com.candy.springboot_mall.dto.UserRegisterRequest;
import com.candy.springboot_mall.model.User;

public interface UserDao {
	
	Integer createUser(UserRegisterRequest userRegisterRequest);
	
	User getUserById(Integer userId);
	
	User getUserByEmail(String email);

}
