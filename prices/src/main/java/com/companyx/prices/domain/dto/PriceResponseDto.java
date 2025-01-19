package com.companyx.prices.domain.dto;

import java.time.LocalDateTime;

public record PriceResponseDto(
        Integer productId,
        Integer brandId,
        Integer priceList,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Double price
) {}