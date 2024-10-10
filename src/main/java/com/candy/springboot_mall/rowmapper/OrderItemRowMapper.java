package com.candy.springboot_mall.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.candy.springboot_mall.model.OrderItem;

public class OrderItemRowMapper implements RowMapper<OrderItem>{

	@Override
	public OrderItem mapRow(ResultSet resultSet, int i) throws SQLException {
		
		OrderItem orderItem = new OrderItem();
		orderItem.setOrder_item_id(resultSet.getInt("order_item_id"));
		orderItem.setOrder_id(resultSet.getInt("order_id"));
		orderItem.setProduct_id(resultSet.getInt("product_id"));
		orderItem.setQuantity(resultSet.getInt("quantity"));
		orderItem.setAmount(resultSet.getInt("amount"));
		
		orderItem.setProduct_name(resultSet.getString("product_name"));
		orderItem.setImage_url(resultSet.getString("image_url"));
		
		return orderItem;
	}

	
}
