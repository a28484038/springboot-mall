package com.candy.springboot_mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.candy.springboot_mall.dto.CreatOrderRequest;
import com.candy.springboot_mall.dto.OrderQueryParam;
import com.candy.springboot_mall.model.Order;
import com.candy.springboot_mall.service.OrderService;
import com.candy.springboot_mall.util.Page;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@PostMapping("/users/{userId}/orders")
	public ResponseEntity<?> createOrder(@PathVariable Integer userId,
			@RequestBody @Valid CreatOrderRequest creatOrderRequest){
		
		Integer orderId = orderService.createOrder(userId,creatOrderRequest);
		
		Order order = orderService.getOrderById(orderId);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(order);
		
	}
	
	@GetMapping("/users/{userId}/orders")
	public ResponseEntity<Page<Order>> getOrders(
			@PathVariable Integer userId,
			//分頁Pagenation
			@RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,
			@RequestParam(defaultValue = "0") @Min(0) Integer offset
			){
		
		OrderQueryParam orderQueryParams = new OrderQueryParam();
		orderQueryParams.setUserId(userId);
		orderQueryParams.setLimit(limit);
		orderQueryParams.setOffset(offset);
		
		//取得order list
		List<Order> orderList = orderService.getOrders(orderQueryParams);
		
		//取得order 總數
		Integer total = orderService.countOrder(orderQueryParams);
		
		//分頁
		Page<Order> page = new Page<>();
		
		page.setLimit(limit);
		page.setOffset(offset);
		page.setTotal(total);  //訂單總數
		page.setResult(orderList); //將查詢到的訂單數據放到result裡面傳給前端
		
		return ResponseEntity.status(HttpStatus.OK).body(page);
	}
}
