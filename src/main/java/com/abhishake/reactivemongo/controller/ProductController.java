package com.abhishake.reactivemongo.controller;

import com.abhishake.reactivemongo.dto.ProductDto;
import com.abhishake.reactivemongo.entity.Product;
import com.abhishake.reactivemongo.service.ProductService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Flux<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Mono<ProductDto> getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @GetMapping("/price-range")
    public Flux<ProductDto> getProductByPriceRange(@RequestParam double min, @RequestParam double max) {
        return productService.getProductsInRange(min, max);
    }

    @PostMapping
    public Mono<ProductDto> createProduct(@RequestBody Mono<ProductDto> productDto) {
        return productService.createProduct(productDto);
    }

    @PutMapping("/{id}")
    public Mono<ProductDto> updateProduct(@RequestBody Mono<ProductDto> productDto, @PathVariable String id) {
        return productService.updateProduct(productDto, id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteProductById(@PathVariable String id) {
        return productService.deleteProduct(id);
    }
}
