package com.satyik.productservice.service;

import com.satyik.productservice.dto.ProductRequest;
import com.satyik.productservice.dto.ProductResponse;
import com.satyik.productservice.model.Product;
import com.satyik.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    public void createProduct (ProductRequest ProductRequest){
        Product product = Product.builder()
                .name(ProductRequest.getName())
                .description(ProductRequest.getDescription())
                .price(ProductRequest.getPrice())
                .build();
        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    public List<ProductResponse> getallProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::maptoProductResponse).toList();
    }

    private ProductResponse maptoProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
