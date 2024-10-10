package com.candy.springboot_mall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.candy.springboot_mall.constant.ProductCategory;
import com.candy.springboot_mall.dao.ProductDao;
import com.candy.springboot_mall.dao.ProductQueryParams;
import com.candy.springboot_mall.dto.ProductRequest;
import com.candy.springboot_mall.model.Product;
import com.candy.springboot_mall.service.ProductService;

@Component
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private  ProductDao productDao;
	
	@Override
	public Integer countProduct(ProductQueryParams productQueryParams) {
		
		return productDao.countProduct(productQueryParams);
	}
	
	@Override
	public List<Product> getProducts(ProductQueryParams productQueryParams) {
		
		return productDao.getProducts(productQueryParams);
	}

	@Override
	public Product getProductById(Integer productId) {

		return productDao.getProductById(productId);
	}

	@Override
	public Integer createProduct(ProductRequest productRequest) {
		//實作就直接呼叫ProductDao的createProduct方法
		return productDao.createProduct(productRequest);
	}

	@Override
	public void updateProduct(Integer productId, ProductRequest productRequest) {
		
		productDao.updateProduct(productId, productRequest);
		
	}

	@Override
	public void deleteProductById(Integer productId) {
		
		productDao.deleteProductById(productId);
	}

	
	
	
	
	
}
