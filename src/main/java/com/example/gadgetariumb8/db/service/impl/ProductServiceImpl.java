package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.ProductRequest;
import com.example.gadgetariumb8.db.dto.request.ProductUserRequest;
import com.example.gadgetariumb8.db.dto.request.SubProductRequest;
import com.example.gadgetariumb8.db.dto.response.ProductUserResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final SubCategoryRepository subCategoryRepository;
    private final BrandRepository brandRepository;
    private final SubProductRepository subProductRepository;
    private final ProductRepository productRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public SimpleResponse saveProduct(ProductRequest productRequest) {
        SubCategory subCategory = subCategoryRepository.findById(productRequest.subCategoryId())
                .orElseThrow(() -> new NotFoundException("Sub category with id:" + productRequest.subCategoryId() + " not found!!"));
        Brand brand = brandRepository.findById(productRequest.brandId())
                .orElseThrow(() -> new NotFoundException("Brand with id:" + productRequest.brandId() + " not found!!!"));
        Product product = new Product();
        product.setSubCategory(subCategory);
        product.setBrand(brand);
        product.setGuarantee(productRequest.guarantee());
        product.setName(productRequest.name());
        product.setDateOfIssue(productRequest.dateOfIssue());
        product.setVideo(productRequest.video());
        product.setPDF(productRequest.PDF());
        product.setDescription(productRequest.description());
        for (SubProductRequest s : productRequest.subProducts()) {
            SubProduct subProduct = new SubProduct();
            subProduct.addCharacteristics(s.characteristics());
            subProduct.setColour(s.colour());
            subProduct.setPrice(s.price());
            subProduct.setQuantity(s.quantity());
            subProduct.setImages(s.images());
            subProduct.setProduct(product);
            product.addSubProduct(subProduct);
            subProductRepository.save(subProduct);
        }
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Successfully saved!!").build();
    }

    @Override
    public ProductUserResponse getProductById(ProductUserRequest productUserRequest) {
        Product product = productRepository.findById(productUserRequest.productId())
                .orElseThrow(() -> new NotFoundException("Product with id:" + productUserRequest.productId() + " not found!!"));
        String sql = """
                select b.logo                                 as logo,
                       spi.images                             as images,
                       p.name                                 as product_name,
                       sp.quantity                            as quantity,
                       p.item_number                          as item_number,
                       p.rating                               as reating,
                       (select count(r.id)
                        from reviews r
                                 join products p on p.id = r.product_id
                        where p.id = 1)                       as countOfReviews,
                       sp.colour                              as color,
                       d.percent                              as percentOfDiscount,
                       ((sp.price - (sp.price * d.percent / 100)) * 1 ) as price,
                       sp.price                               as old_price,
                       p.date_of_issue                        as date_of_issue,
                       spc.characteristics                    as characteristic,
                       p.description                          as description,
                       p.video                                as video_link
                from products p
                         join brands b on b.id = p.brand_id
                         join sub_products sp on p.id = sp.product_id
                         join sub_product_images spi on sp.id = spi.sub_product_id
                         join reviews r on p.id = r.product_id
                         left join discounts d on d.id = p.discount_id
                         join sub_product_characteristics spc on sp.id = spc.sub_product_id
                                
                where p.id = 1 and sp.colour = 'Blue'
                """;


        return null;
    }
}
