package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.Brand;
import com.example.gadgetariumb8.db.model.Product;
import com.example.gadgetariumb8.db.model.SubCategory;
import com.example.gadgetariumb8.db.model.SubProduct;
import com.example.gadgetariumb8.db.repository.BrandRepository;
import com.example.gadgetariumb8.db.repository.ProductRepository;
import com.example.gadgetariumb8.db.repository.SubCategoryRepository;
import com.example.gadgetariumb8.db.repository.SubProductRepository;
import com.example.gadgetariumb8.db.service.ProductService;
import com.example.gadgetariumb8.dto.request.ProductRequest;
import com.example.gadgetariumb8.dto.request.SubProductRequest;
import com.example.gadgetariumb8.dto.response.SimpleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public  class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private final SubCategoryRepository subCategoryRepository;
    private final BrandRepository brandRepository;
    private final SubProductRepository subProductRepository;

    public ProductServiceImpl(ProductRepository productRepository, SubCategoryRepository subCategoryRepository, BrandRepository brandRepository,
                              SubProductRepository subProductRepository) {
        this.productRepository = productRepository;

        this.subCategoryRepository = subCategoryRepository;
        this.brandRepository = brandRepository;
        this.subProductRepository = subProductRepository;
    }

    @Override
    public SimpleResponse saveProduct(ProductRequest productRequest) {
        Product product = new Product();
        SubCategory subCategory = subCategoryRepository.findById(productRequest.subCategoryId()).orElseThrow(() -> new NotFoundException("Sub category with id:" + productRequest.subCategoryId() + " not found!!"));

        Brand brand = brandRepository.findById(productRequest.brandId()).orElseThrow(() -> new NotFoundException("Brand with id:" + productRequest.brandId() + " not found!!!"));

        product.setSubCategory(subCategory);
        product.setBrand(brand);
        product.setGuarantee(productRequest.guarantee());
        product.setName(productRequest.name());
        product.setDateOfIssue(productRequest.dateOfIssue());
        product.setVideo(productRequest.video());
        product.setPDF(productRequest.PDF());
        product.setDescription(productRequest.description());

        for (SubProductRequest s : productRequest.subProducts()) {
            SubProduct sp = new SubProduct();
            sp.addCharacteristics(s.characteristics());
            sp.setColour(s.colour());
            sp.setPrice(s.price());
            sp.setQuantity(s.quantity());
            sp.setImages(s.images());
            sp.setProduct(product);
            subProductRepository.save(sp);
        }
        productRepository.save(product);

        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Successfully saved!!").build();
    }
}
