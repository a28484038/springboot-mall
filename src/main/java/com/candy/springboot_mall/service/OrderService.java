package com.candy.springboot_mall.service;

import java.util.List;

import com.candy.springboot_mall.dto.CreatOrderRequest;
import com.candy.springboot_mall.dto.OrderQueryParam;
import com.candy.springboot_mall.model.Order;

public interface OrderService {
	
	Integer createOrder(Integer userId, CreatOrderRequest creatOrderRequest);
	
	Order getOrderById(Integer orderId);
	
	List<Order> getOrders(OrderQueryParam orderQueryParam);
	
	Integer countOrder(OrderQueryParam orderQueryParam);

}
