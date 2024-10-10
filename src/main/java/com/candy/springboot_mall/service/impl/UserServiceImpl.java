package com.candy.springboot_mall.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import com.candy.springboot_mall.dao.UserDao;
import com.candy.springboot_mall.dto.UserLoginRequest;
import com.candy.springboot_mall.dto.UserRegisterRequest;
import com.candy.springboot_mall.model.User;
import com.candy.springboot_mall.service.UserService;

@Component
public class UserServiceImpl implements UserService{
	
	private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserDao userDao;

	@Override
	public Integer register(UserRegisterRequest userRegisterRequest) {
		User user = userDao.getUserByEmail(userRegisterRequest.getEmail());
		
		//檢查註冊的email
		if(user != null) {
			log.warn("該email {} 已經被註冊", userRegisterRequest.getEmail());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //回傳400http狀態碼，表示前端請求參數有問題
		}
		
		//使用MD5生成密碼雜湊值
		String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes()); //才能將字串轉換成Byte類型
		userRegisterRequest.setPassword(hashedPassword); //這樣userRegisterRequest的密碼的值，就會替換成Hash過後的值
		
		//創建帳號
		return userDao.createUser(userRegisterRequest);
	}

	@Override
	public User getUserById(Integer userId) {
		
		return userDao.getUserById(userId);
	}

	@Override
	public User login(UserLoginRequest userLoginRequest) {
		
		User user = userDao.getUserByEmail(userLoginRequest.getEmail());
		
		//除了判斷email是否一致，也要判斷當email為null的情形
		if(user == null) {
			log.warn("該email {} 尚未註冊", userLoginRequest.getEmail());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //強制停止此次請求
		}
		
		//使用MD5生成密碼雜湊值
		String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());
				
		//比較密碼是否一致
		if(user.getPassword().equals(hashedPassword)) {
			return user;
		}
		else {
			log.warn("email {} 的密碼不正確", userLoginRequest.getEmail());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //強制停止此次請求
		}
		
	}
	
	
	
	
	
	

}
