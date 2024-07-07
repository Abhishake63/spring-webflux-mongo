package com.abhishake.reactivemongo.controller;

import com.abhishake.reactivemongo.dto.ProductDto;
import com.abhishake.reactivemongo.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class ProductControllerTest {

    private Mono<ProductDto> productDtoMono;
    private Flux<ProductDto> productDtoFlux;

    @MockBean
    private ProductService productService;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        productDtoMono = Mono.just(new ProductDto("101", "Mobile", 1, 10000));
        productDtoFlux = Flux.just(
                new ProductDto("102", "Mobile", 2, 10000),
                new ProductDto("103", "TV", 2, 50000));
    }

    @Test
    void getAllProducts() {
        when(productService.getAllProducts()).thenReturn(productDtoFlux);
        Flux<ProductDto> responseBody = webTestClient.get().uri("/products")
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(new ProductDto("102", "Mobile", 2, 10000))
                .expectNext(new ProductDto("103", "TV", 2, 50000))
                .verifyComplete();

    }

    @Test
    void getProductById() {
        when(productService.getProductById(any())).thenReturn(productDtoMono);

        Flux<ProductDto> responseBody = webTestClient.get().uri("/products/101")
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNextMatches(productDto -> productDto.getName().equals("Mobile"))
                .verifyComplete();

    }

    @Test
    void getProductByPriceRange() {
    }

    @Test
    void createProduct() {
        when(productService.createProduct(productDtoMono)).thenReturn(productDtoMono);
        webTestClient.post().uri("/products")
                .body(Mono.just(productDtoMono), ProductDto.class)
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    void updateProduct() {
        when(productService.updateProduct(productDtoMono, "101")).thenReturn(productDtoMono);
        webTestClient.put().uri("/products/101")
                .body(Mono.just(productDtoMono), ProductDto.class)
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    void deleteProductById() {
        when(productService.deleteProduct(any())).thenReturn(Mono.empty());
        webTestClient.delete().uri("/products/101")
                .exchange()
                .expectStatus().isNoContent();
    }
}