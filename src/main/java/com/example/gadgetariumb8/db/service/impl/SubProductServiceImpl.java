package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.response.SubProductResponse;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.*;
import com.example.gadgetariumb8.db.repository.SubProductRepository;
import com.example.gadgetariumb8.db.repository.UserRepository;
import com.example.gadgetariumb8.db.service.SubProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class SubProductServiceImpl implements SubProductService {
    private final SubProductRepository subProductRepository;
    private final UserRepository userRepository;
    private User getAuthenticate(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        log.info("Token has been taken!");
        return userRepository.findUserInfoByEmail(login).orElseThrow(()-> {
            log.error("User not found!");
            return new NotFoundException("User not found!");
        }).getUser();
    }
    @Override
    public List<SubProductResponse> lastViews() {
        List<SubProductResponse>subProductResponseList = new LinkedList<>();
        List<SubProduct> subProductList = subProductRepository.getAllLastReviews(getAuthenticate().getId());
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
