package com.candy.springboot_mall.dao;

import java.util.List;

import com.candy.springboot_mall.dto.CreatOrderRequest;
import com.candy.springboot_mall.dto.OrderQueryParam;
import com.candy.springboot_mall.model.Order;
import com.candy.springboot_mall.model.OrderItem;

public interface OrderDao {
	
	Integer countOrder(OrderQueryParam orderQueryParam);
	
	List<Order> getOrders(OrderQueryParam orderQueryParam);
	
	Integer createOrder(Integer userId, Integer totalAmount);
	
	void createOrderItems(Integer orderId, List<OrderItem> orderItemList);
	
	Order getOrderById(Integer orderId);
	
	List <OrderItem> getOrderItemsByOrderId(Integer orderId);

}
