package com.abhishake.reactivemongo.repository;

import com.abhishake.reactivemongo.dto.ProductDto;
import com.abhishake.reactivemongo.entity.Product;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

    Flux<ProductDto> findByPriceBetween(Range<Double> priceRange);
}
