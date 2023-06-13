package com.example.gadgetariumb8.db.service.impl;


import com.example.gadgetariumb8.db.dto.response.CompareCountResponse;
import com.example.gadgetariumb8.db.dto.response.CompareProductResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.BadRequestException;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.SubProduct;
import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.repository.CustomProductRepository;
import com.example.gadgetariumb8.db.repository.SubProductRepository;
import com.example.gadgetariumb8.db.repository.UserRepository;
import com.example.gadgetariumb8.db.service.ComparisonsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ComparisonsServiceImpl implements ComparisonsService {
    private final SubProductRepository subProductRepository;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;
    private final CustomProductRepository customProductRepository;

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
    @Transactional
    public SimpleResponse saveOrDeleteComparisons(Long id, boolean addOrDelete) {
        SubProduct subProduct = subProductRepository.findById(id).orElseThrow(() -> new NotFoundException("This sub product id:" + id + " is not found!!!"));
        User user = getAuthenticate();
        if (addOrDelete) {
            if (user.getComparisons().contains(subProduct))
                throw new BadRequestException("Sub products with id %s is already exists on comparisons".formatted(id));
            user.addComparisons(subProduct);
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Comparisons successfully saved!!").build();
        } else {
            userRepository.deleteComparisonsId(id, user.getId());
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Comparisons successfully deleted!!").build();
        }
    }

    @Override
    public List<CompareProductResponse> compare() {
        log.info("Getting all compare products!");
        String sql = """
                SELECT (SELECT sci FROM sub_product_images sci where sci.sub_product_id = sp.id LIMIT 1) as image,(p.name) as name
                     ,p.description as description,sp.price as price ,b.name as brand_name,
                      (SELECT spc.characteristics from sub_product_characteristics spc where spc.characteristics_key='screen' and spc.sub_product_id = sp.id) as screen,
                      sp.colour as color,
                      (SELECT spc.characteristics from sub_product_characteristics spc where spc.characteristics_key='operatingSystem' and spc.sub_product_id = sp.id) as operatingSystem,
                      (SELECT spc.characteristics from sub_product_characteristics spc where spc.characteristics_key='memory' and spc.sub_product_id = sp.id) as memory,
                      (SELECT spc.characteristics from sub_product_characteristics spc where spc.characteristics_key='weight' and spc.sub_product_id = sp.id) as weight,
                      (SELECT spc.characteristics from sub_product_characteristics spc where spc.characteristics_key='simCard' and spc.sub_product_id = sp.id) as simCard
                              
                FROM products p JOIN sub_products sp on p.id = sp.product_id
                    JOIN users_comparisons uc on uc.comparisons_id = sp.id
                    JOIN users u on uc.user_id = u.id JOIN brands b on p.brand_id = b.id where u.id = ?
                 """;
        log.info("Products are successfully got!");
        return jdbcTemplate.query(sql, (resultSet, i) ->
                new CompareProductResponse(
                        resultSet.getString("image"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getString("brand_name"),
                        resultSet.getString("screen"),
                        resultSet.getString("color"),
                        resultSet.getString("operatingSystem"),
                        resultSet.getString("memory"),
                        resultSet.getString("weight"),
                        resultSet.getString("simCard")
                ), getAuthenticate().getId()
        );
    }

    @Override
    public CompareCountResponse countCompare() {
        User user = getAuthenticate();
        if (user.getComparisons().size() != 0) {
            return jdbcTemplate.query(customProductRepository.countCompare(), (result, i) -> {
                        Map<String, Integer> count = new LinkedHashMap<>();
                        count.put(result.getString("categoryName"),
                                result.getInt("countComparisons"));
                        return new CompareCountResponse(count);
                    },
                    user.getId()
            ).stream().findFirst().orElseThrow(() -> {
                log.error("Not found!");
                throw new NotFoundException("Not found");
            });
        } else {
            throw new NotFoundException(String.format("There is no comparison on this User"));
        }
    }

    @Override
    public SimpleResponse cleanCompare() {
        User user = getAuthenticate();
        user.getComparisons().clear();
        userRepository.save(user);
        log.error(String.format("Clean Compare!"));
        return SimpleResponse.builder().message(String.format("Clean Compare!")).httpStatus(HttpStatus.OK).build();
    }
}
