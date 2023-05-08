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
import java.util.List;

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
    public List<UserChosenOneResponse> getAll() {
        User user = getAuthenticate();
        try {
            List<UserChosenOneResponse> chosenOne = jdbcTemplate.query(userCustomRepository.getAllChosenOne(),
                    (result,i) -> new UserChosenOneResponse(
                            result.getLong("userId"),
                            Collections.singletonList(result.getString("productImage")),
                            result.getString("productName"),
                            result.getDouble("productRating"),
                            result.getBigDecimal("productPrice")
                    ), user.getId()
            );
            userRepository.save(user);
            return chosenOne;
        }catch (NotFoundException notFoundException){
            throw new NotFoundException(String.format("This user has no favorites!"));
        }
    }
}
