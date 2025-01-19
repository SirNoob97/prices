package com.companyx.prices.domain.service;

import com.companyx.prices.adapter.outbound.persistence.PriceRepository;
import com.companyx.prices.adapter.outbound.persistence.PriceEntity;
import com.companyx.prices.domain.dto.PriceResponseDto;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Log
@Service
public class PriceService {

    private final PriceRepository priceRepository;

    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public PriceResponseDto getPrice(Integer productId, Integer brandId, LocalDateTime applicationTime) {
        validateInputs(productId, brandId, applicationTime);
        var price = this.priceRepository.findTopApplicablePrices(productId, brandId, applicationTime)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No applicable price found."));
        return mapToDto(price);
    }

    private void validateInputs(Integer productId, Integer brandId, LocalDateTime applicationTime) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product ID must be a positive number");
        }

        if (brandId == null || brandId <= 0) {
            throw new IllegalArgumentException("Brand ID must be a positive number");
        }

        if (applicationTime == null) {
            throw new IllegalArgumentException("Application time cannot be null");
        }
    }

    private PriceResponseDto mapToDto(PriceEntity priceEntity) {
        return new PriceResponseDto(
                priceEntity.getProductId(),
                priceEntity.getBrandId(),
                priceEntity.getPriceList(),
                priceEntity.getStartDate(),
                priceEntity.getEndDate(),
                priceEntity.getPrice()
        );
    }
}