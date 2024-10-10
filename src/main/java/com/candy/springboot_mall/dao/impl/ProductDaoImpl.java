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

import com.candy.springboot_mall.constant.ProductCategory;
import com.candy.springboot_mall.dao.ProductDao;
import com.candy.springboot_mall.dao.ProductQueryParams;
import com.candy.springboot_mall.dto.ProductRequest;
import com.candy.springboot_mall.model.Product;
import com.candy.springboot_mall.rowmapper.ProductRowMapper;
import com.candy.springboot_mall.service.ProductService;

@Component
public class ProductDaoImpl implements ProductDao {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public Integer countProduct(ProductQueryParams productQueryParams) {

		String sql = "SELECT count(*) FROM product where 1=1";

		Map<String, Object> map = new HashMap<>();

		// 查詢條件
		sql = addFilteringSql(sql, map, productQueryParams);
		
		//通常用在取count值
		Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);

		return total;
	}

	@Override
	public List<Product> getProducts(ProductQueryParams productQueryParams) {
		String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, "
				+ "created_date, last_modified_date FROM product where 1=1 ";

		Map<String, Object> map = new HashMap<>();

		// 查詢條件
		sql = addFilteringSql(sql, map, productQueryParams);

		// 排序
		sql = sql + " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();

		// 分頁
		sql = sql + " LIMIT :limit OFFSET :offset";
		map.put("limit", productQueryParams.getLimit());
		map.put("offset", productQueryParams.getOffset());

		List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

		return productList;
	}

	@Override
	public Product getProductById(Integer productId) {
		String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, "
				+ "created_date, last_modified_date FROM product WHERE product_id = :productId";

		Map<String, Object> map = new HashMap<>();
		map.put("productId", productId);

		List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

		if (productList.size() > 0) {
			return productList.get(0);
		} else {
			return null;
		}

	}

	@Override
	public Integer createProduct(ProductRequest productRequest) {

		// 新增商品的sql語法
		String sql = "Insert into product(product_name, category, image_url, price, stock,"
				+ " description, created_date, last_modified_date)" + "VALUES (:productName, "
				+ ":Category, :Image_url, :Price, :Stock, :Description, :Created_date, :Last_modified_date)";

		// 前端傳進來的productRequest參數一個一個加進map中
		Map<String, Object> map = new HashMap<>();
		map.put("productName", productRequest.getProduct_name());
		map.put("Category", productRequest.getCategory().toString());
		map.put("Image_url", productRequest.getImage_url());
		map.put("Price", productRequest.getPrice());
		map.put("Stock", productRequest.getStock());
		map.put("Description", productRequest.getDescription());

		// 記錄當下時間點，當下時間當成商品創建時間跟最後修改時間
		Date now = new Date();
		map.put("Created_date", now);
		map.put("Last_modified_date", now);

		// 使用keyHolder儲存資料庫生成的productId
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

		namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
		int productId = keyHolder.getKey().intValue();

		// 將productId回傳出去
		return productId;
	}

	@Override
	public void updateProduct(Integer productId, ProductRequest productRequest) {

		String sql = "update product set product_name = :productName , category = :Category , image_url = :Image_url , "
				+ "price = :Price , stock = :Stock , description = :Description , "
				+ "last_modified_date = :lastModifiedDate where product_id = :productId";

		Map<String, Object> map = new HashMap<>();
		map.put("productId", productId);

		map.put("productName", productRequest.getProduct_name());
		map.put("Category", productRequest.getCategory().toString());
		map.put("Image_url", productRequest.getImage_url());
		map.put("Price", productRequest.getPrice());
		map.put("Stock", productRequest.getStock());
		map.put("Description", productRequest.getDescription());

		map.put("lastModifiedDate", new Date());

		namedParameterJdbcTemplate.update(sql, map);

	}

	@Override
	public void deleteProductById(Integer productId) {

		String sql = "delete from product where product_id = :productId";

		Map<String, Object> map = new HashMap<>();

		map.put("productId", productId);

		namedParameterJdbcTemplate.update(sql, map);

	}
	
	private String addFilteringSql(String sql, Map<String, Object> map,ProductQueryParams productQueryParams) {
		// 查詢條件
				if (productQueryParams.getCategory() != null) {
					sql = sql + " AND category = :category";

					// 因為category是Enum類型，使用上要使用category的name()方法，將前端傳進來的Enum類型轉換成字串，再將字串加到map裡面
					map.put("category", productQueryParams.getCategory().name());
				}

				// 查詢條件
				if (productQueryParams.getSearch() != null) {
					sql = sql + " AND product_name Like :search";
					map.put("search", "%" + productQueryParams.getSearch() + "%");
				}
				return sql;
	}

	@Override
	public void updateStock(Integer productId, Integer stock) {
		
		String sql = "update product set stock = :stock, last_modified_date = :lastModifiedDate "
				+ "where product_id = :productId ";
		
		Map<String, Object> map = new HashMap<>();

		map.put("productId", productId);
		map.put("stock", stock);
		map.put("lastModifiedDate", new Date());

		namedParameterJdbcTemplate.update(sql, map);
	}
	
	

}
