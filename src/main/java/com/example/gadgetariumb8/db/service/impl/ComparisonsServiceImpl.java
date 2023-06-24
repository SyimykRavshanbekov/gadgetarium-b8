package com.example.gadgetariumb8.db.service.impl;


import com.example.gadgetariumb8.db.dto.response.CompareCountResponse;
import com.example.gadgetariumb8.db.dto.response.CompareProductResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.BadRequestException;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.SubProduct;
import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.repository.SubProductRepository;
import com.example.gadgetariumb8.db.repository.UserRepository;
import com.example.gadgetariumb8.db.repository.impl.CustomProductRepository;
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
        log.info("Токен взят!");
        return userRepository.findUserInfoByEmail(login).orElseThrow(() -> {
            log.error("Пользователь не найден с токеном пожалуйста войдите или зарегистрируйтесь!");
            return new NotFoundException("Пользователь не найден с токеном пожалуйста войдите или зарегистрируйтесь!!");
        }).getUser();
    }

    @Override
    @Transactional
    public SimpleResponse saveOrDeleteComparisons(Long id, boolean addOrDelete) {
        SubProduct subProduct = subProductRepository.findById(id).orElseThrow(() -> new NotFoundException("Этот идентификатор продукта: " + id + " не найден!"));
        User user = getAuthenticate();
        if (addOrDelete) {
            if (user.getComparisons().contains(subProduct))
                throw new BadRequestException("Продукт с идентификатором %s уже существует в сравнении!".formatted(id));
            user.addComparisons(subProduct);
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Сравнения успешно сохранены!").build();
        } else {
            userRepository.deleteComparisonsId(id, user.getId());
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Сравнения успешно удалены!").build();
        }
    }

    @Override
    public List<CompareProductResponse> compare(String categoryName) {
        log.info("Получение всех продуктов сравнения!");
        String sql = """
                SELECT p.id as productId,
                       sp.id as subProductId,
                      (SELECT sci.images FROM sub_product_images sci where sci.sub_product_id = sp.id LIMIT 1) as image,(p.name) as name,
                      p.description as description,sp.price as price ,b.name as brand_name,
                      (SELECT spc.characteristics from sub_product_characteristics spc where spc.characteristics_key='screen' and spc.sub_product_id = sp.id) as screen,
                      sp.colour as color,
                      (SELECT spc.characteristics from sub_product_characteristics spc where spc.characteristics_key='operatingSystem' and spc.sub_product_id = sp.id) as operatingSystem,
                      (SELECT spc.characteristics from sub_product_characteristics spc where spc.characteristics_key='память' and spc.sub_product_id = sp.id) as memory,
                      (SELECT spc.characteristics from sub_product_characteristics spc where spc.characteristics_key='RAM' and spc.sub_product_id = sp.id) as RAM,
                      (SELECT spc.characteristics from sub_product_characteristics spc where spc.characteristics_key='Кол-во SIM-карт' and spc.sub_product_id = sp.id) as simCard
                              
                FROM products p
                    JOIN sub_products sp on p.id = sp.product_id
                    JOIN users_comparisons uc on uc.comparisons_id = sp.id
                    JOIN users u on uc.user_id = u.id
                    JOIN brands b on p.brand_id = b.id
                    JOIN sub_categories sc on sc.id = p.sub_category_id
                    JOIN categories c on c.id = sc.category_id
                    where u.id = ? and c.name iLIKE ?
                 """;
        log.info("Продукция успешно приобретена!");
        return jdbcTemplate.query(sql, (resultSet, i) ->
                new CompareProductResponse(
                        resultSet.getLong("productId"),
                        resultSet.getLong("subProductId"),
                        resultSet.getString("image"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getString("brand_name"),
                        resultSet.getString("screen"),
                        resultSet.getString("color"),
                        resultSet.getString("operatingSystem"),
                        resultSet.getString("memory"),
                        resultSet.getString("RAM"),
                        resultSet.getString("simCard")
                ), getAuthenticate().getId(), categoryName
        );
    }

    @Override
    public CompareCountResponse countCompare() {
        User user = getAuthenticate();
        if (user.getComparisons().size() != 0) {
            return jdbcTemplate.query(customProductRepository.countCompare(), (resultSet) -> {
                Map<String, Integer> count = new LinkedHashMap<>();

                while (resultSet.next()) {
                    String categoryName = resultSet.getString("categoryName");
                    int countComparisons = resultSet.getInt("countComparisons");
                    count.put(categoryName, countComparisons);
                }

                return new CompareCountResponse(count);
            }, user.getId());
        } else {
            throw new NotFoundException("На этом Пользователе сравнения нет");
        }
    }

    @Override
    public SimpleResponse cleanCompare() {
        User user = getAuthenticate();
        user.getComparisons().clear();
        userRepository.save(user);
        log.error("Сравнение очищено!");
        return SimpleResponse.builder().message("Сравнение очищено!").httpStatus(HttpStatus.OK).build();
    }
}
