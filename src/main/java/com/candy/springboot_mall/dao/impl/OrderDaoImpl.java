package com.candy.springboot_mall.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import com.candy.springboot_mall.dao.OrderDao;
import com.candy.springboot_mall.dao.ProductQueryParams;
import com.candy.springboot_mall.dto.OrderQueryParam;
import com.candy.springboot_mall.model.Order;
import com.candy.springboot_mall.model.OrderItem;
import com.candy.springboot_mall.model.Product;
import com.candy.springboot_mall.rowmapper.OrderRowMapper;
import com.candy.springboot_mall.rowmapper.ProductRowMapper;
import com.candy.springboot_mall.rowmapper.OrderItemRowMapper;

@Component
public class OrderDaoImpl implements OrderDao {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public Integer createOrder(Integer userId, Integer totalAmount) {
		String sql = "insert into `order` (user_id, total_amount, created_date, last_modified_date )"
				+ "values( :userId, :totalAmount, :createdDate, :lastModifiedDate)";

		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("totalAmount", totalAmount);

		Date now = new Date();
		map.put("createdDate", now);
		map.put("lastModifiedDate", now);

		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

		int orderId = keyHolder.getKey().intValue();

		return orderId;
	}

	@Override
	public void createOrderItems(Integer orderId, List<OrderItem> orderItemList) {

		String sql = "insert into order_item ( order_id, product_id, quantity, amount )"
				+ "values ( :orderId, :productId, :quantity, :amount)";

		MapSqlParameterSource[] parameterSource = new MapSqlParameterSource[orderItemList.size()];

		for (int i = 0; i < orderItemList.size(); i++) {
			OrderItem orderItem = orderItemList.get(i);

			parameterSource[i] = new MapSqlParameterSource();
			parameterSource[i].addValue("orderId", orderId);
			parameterSource[i].addValue("productId", orderItem.getProduct_id());
			parameterSource[i].addValue("quantity", orderItem.getQuantity());
			parameterSource[i].addValue("amount", orderItem.getAmount());
		}

		namedParameterJdbcTemplate.batchUpdate(sql, parameterSource);

	}

	@Override
	public Order getOrderById(Integer orderId) {

		String sql = "select order_id, user_id, total_amount, created_date, last_modified_date "
				+ "from `order` where order_id = :orderId";

		Map<String, Object> map = new HashMap<>();
		map.put("orderId", orderId);

		List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

		if (orderList.size() > 0) {

			return orderList.get(0);
		} else {

			return null;

		}

	}

	@Override
	public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {

		String sql = "select oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.amount, p.product_name, p.image_url "
				+ "from order_item as oi " + "left join product as p on oi.product_id = p.product_id "
				+ "where oi.order_id = :orderId ";

		Map<String, Object> map = new HashMap<>();
		map.put("orderId", orderId);

		List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());

		return orderItemList;
	}

	@Override
	public Integer countOrder(OrderQueryParam orderQueryParam) {

		String sql = "select count(*) from `order` where 1=1 ";

		Map<String, Object> map = new HashMap<>();

		// 查詢條件
		sql = addFilteringSql(sql, map, orderQueryParam);

		Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);

		return total;
	}

	@Override
	public List<Order> getOrders(OrderQueryParam orderQueryParam) {
		String sql = "select order_id, user_id, total_amount, created_date, last_modified_date from `order` where 1=1 ";

		Map<String, Object> map = new HashMap<>();

		// 查詢條件
		sql = addFilteringSql(sql, map, orderQueryParam);

		// 排序
		sql = sql + " ORDER BY created_date desc";

		// 分頁
		sql = sql + " LIMIT :limit OFFSET :offset";
		map.put("limit", orderQueryParam.getLimit());
		map.put("offset", orderQueryParam.getOffset());

		List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

		return orderList;
	}

	private String addFilteringSql(String sql, Map<String, Object> map, OrderQueryParam orderQueryParam) {
		// 查詢條件
		if (orderQueryParam.getUserId() != null) {
			sql = sql + " AND user_id = :userId";
			map.put("userId",orderQueryParam.getUserId());
			
		}

		return sql;
	}

}
