package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.DiscountRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.BadRequestException;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.Discount;
import com.example.gadgetariumb8.db.model.SubProduct;
import com.example.gadgetariumb8.db.repository.SubProductRepository;
import com.example.gadgetariumb8.db.service.DiscountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DiscountServiceImpl implements DiscountService {
    private final SubProductRepository subProductRepository;

    @Override
    public SimpleResponse addDiscount(DiscountRequest discountRequest) {
        log.info("Adding discount!");
        List<SubProduct> subProducts = discountRequest.productsId().stream()
                .map(id -> subProductRepository.findById(id).orElseThrow(
                        () -> {
                            log.error(String.format("Product with id %s is not found!", id));
                            throw new NotFoundException(String.format("Продукт с id: %s не найден!!!", id));
                        }))
                .toList();
        if (discountRequest.dateOfStart().isAfter(discountRequest.dateOfFinish())) {
            log.error("Дата начала не должна быть позже даты окончания!");
            throw new BadRequestException("Дата начала не должна быть позже даты окончания!");
        }
        Discount discount = new Discount();
        discount.setPercent(discountRequest.percentOfDiscount());
        discount.setDateOfStart(discountRequest.dateOfStart());
        discount.setDateOfFinish(discountRequest.dateOfFinish());

        for (SubProduct subProduct : subProducts) {
            subProduct.setDiscount(discount);
        }
        log.info("Скидка успешно сохранена!");
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Скидка успешно сохранена!")
                .build();
    }
}
