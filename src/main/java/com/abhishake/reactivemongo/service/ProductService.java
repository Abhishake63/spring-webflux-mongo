package com.abhishake.reactivemongo.service;

import com.abhishake.reactivemongo.dto.ProductDto;
import com.abhishake.reactivemongo.entity.Product;
import com.abhishake.reactivemongo.repository.ProductRepository;
import com.abhishake.reactivemongo.util.AppUtils;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Flux<ProductDto> getAllProducts() {
        return productRepository.findAll().map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> getProductById(String id) {
        return productRepository.findById(id).map(AppUtils::entityToDto);
    }

    public Flux<ProductDto> getProductsInRange(double min, double max) {
        return productRepository.findByPriceBetween(Range.closed(min, max));
    }

    public Mono<ProductDto> createProduct(Mono<ProductDto> productDtoMono) {
        return productDtoMono
                .map(AppUtils::dtoToEntity)
                .flatMap(productRepository::insert)
                .map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono, String id) {
        return productRepository.findById(id)
                .flatMap(p -> productDtoMono.map(AppUtils::dtoToEntity)
                        .doOnNext(e -> e.setId(id)))
                .flatMap(productRepository::save)
                .map(AppUtils::entityToDto);
    }

    public Mono<Void> deleteProduct(String id) {
        return productRepository.deleteById(id);
    }
}
