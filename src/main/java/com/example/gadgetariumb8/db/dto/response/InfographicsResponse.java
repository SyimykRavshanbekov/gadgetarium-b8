package com.example.gadgetariumb8.db.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class InfographicsResponse {
    private BigDecimal redeemedForTheAmount;
    private int countRedeemed;
    private BigDecimal orderedForTheAmount;
    private int countOrdered;
    private BigDecimal currentPeriod;
    private BigDecimal previousPeriod;
}
