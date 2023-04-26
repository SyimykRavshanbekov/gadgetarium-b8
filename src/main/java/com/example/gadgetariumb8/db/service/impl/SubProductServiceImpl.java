package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.response.SubProductResponse;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.*;
import com.example.gadgetariumb8.db.repository.SubProductRepository;
import com.example.gadgetariumb8.db.repository.UserRepository;
import com.example.gadgetariumb8.db.service.SubProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubProductServiceImpl implements SubProductService {
    private final SubProductRepository subProductRepository;
    private final UserRepository userRepository;
    @Override
    public List<SubProductResponse> lastViews(Long userId) {
        List<SubProductResponse>subProductResponseList = new LinkedList<>();
        List<SubProduct> subProductList = subProductRepository.getAllLastReviews(userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s does not exists", userId)));
        if(subProductList.size()>7) {
            user.getLastViews().remove(subProductRepository.getAllLastReviews(userId).get(0));
            userRepository.save(user);
        }
        for (SubProduct subProduct : subProductList) {
            subProductResponseList.add(new SubProductResponse(
                    subProduct.getImages().stream().findFirst().orElse("Sub product images"),
                    subProduct.getProduct().getName(),
                    subProduct.getProduct().getBrand().getName(),
                    (subProduct.getProduct().getReviews().stream().mapToDouble(Review::getGrade).sum()/ (long) subProduct.getProduct().getReviews().size()),
                    subProduct.getProduct().getReviews().size(),
                    subProduct.getPrice()
            ));
        }
        return subProductResponseList;
    }
}
