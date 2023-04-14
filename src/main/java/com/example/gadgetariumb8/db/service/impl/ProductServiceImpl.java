package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.model.*;
import com.example.gadgetariumb8.db.repository.*;
import com.example.gadgetariumb8.db.service.ProductService;
import com.example.gadgetariumb8.dto.request.ProductRequest;
import com.example.gadgetariumb8.dto.request.SubProductRequest;
import com.example.gadgetariumb8.dto.response.SimpleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final SubProductRepository subProductRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final BrandRepository brandRepository;

    public ProductServiceImpl(ProductRepository productRepository, SubProductRepository subProductRepository, SubCategoryRepository subCategoryRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.subProductRepository = subProductRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.brandRepository = brandRepository;
    }

    @Override
    public SimpleResponse saveProduct(ProductRequest productRequest) {
        Product product = new Product();
        SubCategory subCategory = subCategoryRepository.findById(productRequest.subCategoryId()).get();
        //To do find subCategory by id. If subCategory not found, throw NotFoundException.

        Brand brand = brandRepository.findById(productRequest.brandId()).get();
        //To do find brand by id. If brand not found, throw NotFoundException.

        product.setSubCategory(subCategory);
        product.setBrand(brand);
        product.setGuarantee(productRequest.guarantee());
        product.setName(productRequest.name());
        product.setDateOfIssue(productRequest.dateOfIssue());
        product.setVideo(productRequest.video());
        product.setPDF(productRequest.PDF());
        product.setDescription(productRequest.description());

        productRequest.subProducts().forEach(s -> product.addSubProduct(SubProduct.builder()
                .colour(s.colour())
                .characteristics(s.characteristics())
                .price(s.price())
                .quantity(s.quantity())
                .images(s.images())
                .build()));

        productRepository.save(product);

        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Successfully saved!!").build();
    }
}
