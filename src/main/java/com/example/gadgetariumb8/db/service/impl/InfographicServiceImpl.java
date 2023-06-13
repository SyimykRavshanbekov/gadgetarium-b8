package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.response.InfographicsResponse;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.service.InfographicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InfographicServiceImpl implements InfographicService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public InfographicsResponse getInfographics(String period) {
        log.info("Получаем инфографику!");
        String sql = """
                SELECT COALESCE(SUM(CASE WHEN o.delivery_type IS TRUE THEN total_price END), 0) AS redeemed_for_the_amount,
                       COALESCE(COUNT(CASE WHEN o.delivery_type IS TRUE THEN total_price END), 0) AS count_redeemed,
                       COALESCE( SUM(CASE WHEN o.delivery_type IS FALSE THEN total_price END), 0) AS ordered_for_the_amount,
                       COALESCE( COUNT(CASE WHEN o.delivery_type IS FALSE THEN total_price END), 0) AS count_ordered,
                       COALESCE(SUM(CASE
                               WHEN o.date = current_date and 'day' = ? THEN total_price
                               WHEN extract(year from o.date) = extract(year from current_date) and
                                 extract(month from o.date) = extract(month from current_date) and 'month' = ? THEN total_price
                               WHEN extract(year from o.date) = extract(year from current_date) and 'year' = ?
                                   THEN total_price END),0)
                       AS current_period,
                       COALESCE(SUM(CASE
                               WHEN o.date = (current_date - interval '1 day') and 'day' = ? THEN total_price
                               WHEN extract(year from o.date) = extract(year from current_date) and
                                 extract(month from o.date) = extract(month from current_date - interval '1 month') and
                                    'month' = ? THEN total_price
                               WHEN extract(year from o.date) = extract(year from current_date - interval '1 year') and 'year' = ?
                                   THEN total_price END),0)
                       AS previous_period
                FROM orders o;
                """;
        return jdbcTemplate.query(sql, (resulSet, i) -> new InfographicsResponse(
                        resulSet.getBigDecimal("redeemed_for_the_amount"),
                        resulSet.getInt("count_redeemed"),
                        resulSet.getBigDecimal("ordered_for_the_amount"),
                        resulSet.getInt("count_ordered"),
                        resulSet.getBigDecimal("current_period"),
                        resulSet.getBigDecimal("previous_period")
                ),
                period,
                period,
                period,
                period,
                period,
                period
        ).stream().findFirst().orElseThrow(() -> {
            log.error("Этот ответ не найден!!");
            throw new NotFoundException("Этот ответ не найден!!");
        });
    }
}
