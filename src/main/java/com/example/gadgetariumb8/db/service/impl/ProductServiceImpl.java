package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.Brand;
import com.example.gadgetariumb8.db.model.Product;
import com.example.gadgetariumb8.db.model.SubCategory;
import com.example.gadgetariumb8.db.model.SubProduct;
import com.example.gadgetariumb8.db.repository.BrandRepository;
import com.example.gadgetariumb8.db.repository.ProductRepository;
import com.example.gadgetariumb8.db.repository.SubCategoryRepository;
import com.example.gadgetariumb8.db.service.ProductService;
import com.example.gadgetariumb8.dto.request.ProductRequest;
import com.example.gadgetariumb8.dto.response.SimpleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private final SubCategoryRepository subCategoryRepository;
    private final BrandRepository brandRepository;

    public ProductServiceImpl(ProductRepository productRepository, SubCategoryRepository subCategoryRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;

        this.subCategoryRepository = subCategoryRepository;
        this.brandRepository = brandRepository;
    }

    @Override
    public SimpleResponse saveProduct(ProductRequest productRequest) {
        Product product = new Product();
        SubCategory subCategory = subCategoryRepository.findById(productRequest.subCategoryId()).orElseThrow(()->new NotFoundException("This id:" + productRequest.subCategoryId() + " does not exist"));
        //To do find subCategory by id. If subCategory not found, throw NotFoundException.

        Brand brand = brandRepository.findById(productRequest.brandId()).orElseThrow(()->new NotFoundException("This id:" + productRequest.brandId() + " does not exist"));
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
