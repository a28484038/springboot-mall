package com.candy.springboot_mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.candy.springboot_mall.constant.ProductCategory;
import com.candy.springboot_mall.dao.ProductQueryParams;
import com.candy.springboot_mall.dto.ProductRequest;
import com.candy.springboot_mall.model.Product;
import com.candy.springboot_mall.service.ProductService;
import com.candy.springboot_mall.util.Page;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Validated
@RestController
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@GetMapping("/products")
	public ResponseEntity<Page<Product>> getProducts(
			
			//查詢條件Filtering
			@RequestParam(required = false) ProductCategory category,
			@RequestParam(required = false) String search,
			
			//排序Sorting
			@RequestParam(defaultValue = "created_date") String orderBy, 
			@RequestParam(defaultValue = "desc") String sort,
			
			//分頁Pagenation
			@RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
			@RequestParam(defaultValue = "0") @Min(0) Integer offset
			){
		
		ProductQueryParams productQueryParams = new ProductQueryParams();
		
		productQueryParams.setCategory(category);
		productQueryParams.setSearch(search);
		productQueryParams.setOrderBy(orderBy);
		productQueryParams.setSort(sort);
		productQueryParams.setLimit(limit);
		productQueryParams.setOffset(offset);
		
		//取得product List
		List<Product> productList = productService.getProducts(productQueryParams);
		
		//取得product總數
		Integer total = productService.countProduct(productQueryParams);
		
		//分頁
		Page<Product> page = new Page<>();
		page.setLimit(limit);
		page.setOffset(offset);
		page.setTotal(total);  //商品總數
		page.setResult(productList); //將查詢到的商品數據放到result裡面傳給前端
		
		return ResponseEntity.status(HttpStatus.OK).body(page);
	}

	@GetMapping("/products/{productId}")
	public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {

		Product product = productService.getProductById(productId);

		if (product != null) {
			return ResponseEntity.status(HttpStatus.OK).body(product);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

	}
	
	@PostMapping("/products")
	public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
		
		//從資料庫中創建商品，並返回資料庫生成的productId
		Integer productId = productService.createProduct(productRequest);
		
		//使用productId查詢商品數據
		Product product = productService.getProductById(productId);
		
		//回傳ResponseEntity給前端，狀態碼是201CREATED(一筆新的商品數據被創建出來)，創建出來的商品數據放在body
		return ResponseEntity.status(HttpStatus.CREATED).body(product);
	}
	
	@PutMapping("/products/{productId}")
	public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
			@RequestBody @Valid ProductRequest productRequest){
		
		//檢查Product是否存在
		Product product = productService.getProductById(productId);
		
		if(product == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		//修改商品數據
		productService.updateProduct(productId, productRequest);
		
		Product updateProduct = productService.getProductById(productId);
		
		return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
		
	}
	
	@DeleteMapping("/products/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable Integer productId){
		
		productService.deleteProductById(productId);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		
	}

}


