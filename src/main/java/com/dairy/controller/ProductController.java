package com.dairy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dairy.dto.ApiResponse;
import com.dairy.model.Product;
import com.dairy.service.ProductService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	// PUBLIC: Anyone can view products
	@GetMapping
	public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
		return ResponseEntity.ok(ApiResponse.success("Products fetched", productService.getAllProducts()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Product>> getProduct(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(ApiResponse.success("Product found", productService.getProductById(id)));
		} catch (Exception e) {
			return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
		}
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<List<Product>>> searchProducts(@RequestParam String name) {
		return ResponseEntity.ok(ApiResponse.success("Search results", productService.searchProducts(name)));
	}

	@GetMapping("/category/{category}")
	public ResponseEntity<ApiResponse<List<Product>>> getByCategory(@PathVariable String category) {
		return ResponseEntity
				.ok(ApiResponse.success("Products by category", productService.getProductsByCategory(category)));
	}

	// ADMIN ONLY: Add product
	@PostMapping
	public ResponseEntity<ApiResponse<Product>> addProduct(@RequestBody Product product, HttpSession session) {
		if (!"ADMIN".equals(session.getAttribute("userRole"))) {
			return ResponseEntity.status(403).body(ApiResponse.error("Access denied. Admins only."));
		}
		try {
			return ResponseEntity
					.ok(ApiResponse.success("Product added successfully", productService.addProduct(product)));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
		}
	}

	// ADMIN ONLY: Update product
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Long id, @RequestBody Product product,
			HttpSession session) {
		if (!"ADMIN".equals(session.getAttribute("userRole"))) {
			return ResponseEntity.status(403).body(ApiResponse.error("Access denied. Admins only."));
		}
		try {
			return ResponseEntity.ok(ApiResponse.success("Product updated", productService.updateProduct(id, product)));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
		}
	}

	// ADMIN ONLY: Delete product
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable Long id, HttpSession session) {
		if (!"ADMIN".equals(session.getAttribute("userRole"))) {
			return ResponseEntity.status(403).body(ApiResponse.error("Access denied. Admins only."));
		}
		try {
			productService.deleteProduct(id);
			return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
		}
	}
}