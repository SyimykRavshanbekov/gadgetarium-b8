package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.response.UserChosenOneResponse;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.repository.UserCustomRepository;
import com.example.gadgetariumb8.db.repository.UserRepository;
import com.example.gadgetariumb8.db.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final JdbcTemplate jdbcTemplate;
    private final UserCustomRepository userCustomRepository;
    private final UserRepository userRepository;

    private User getAuthenticate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        log.info("Token has been taken!");
        return userRepository.findUserInfoByEmail(login).orElseThrow(() -> {
            log.error("User not found!");
            return new NotFoundException("User not found!");
        }).getUser();
    }

    @Override
    public UserChosenOneResponse getAll() {
        User user = getAuthenticate();
        return jdbcTemplate.query(userCustomRepository.getAllChosenOne(),
                (result,i) -> new UserChosenOneResponse(result.getLong("id"),
                        Collections.singletonList(result.getString("imageProduct")),
                        result.getString("productName"),
                        result.getDouble("rating"),
                        result.getBigDecimal("priceProduct")
                        ), user.getId()
        ).stream().findFirst().orElseThrow(()->new NotFoundException("This find index is not found!!!"));
    }
}
