package com.companyx.prices.service;

import com.companyx.prices.adapter.outbound.persistence.PriceEntity;
import com.companyx.prices.adapter.outbound.persistence.PriceRepository;
import com.companyx.prices.domain.service.PriceService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.companyx.prices.Constants.TESTING_PRODUCT_ID;
import static com.companyx.prices.Constants.TESTING_BRAND_ID;
import static com.companyx.prices.Constants.TESTING_APPLICATION_TIME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PriceServiceTest {

    @Mock
    private PriceRepository priceRepository;

    @InjectMocks
    private PriceService priceService;

    @Test
    public void testGetPrice_Success() {
        PriceEntity validPriceEntity = new PriceEntity(
                null, TESTING_PRODUCT_ID, TESTING_BRAND_ID,
                LocalDateTime.of(2020, 6, 14, 0, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                1, 0, 35.50, "EUR"
        );

        when(priceRepository.findTopApplicablePrices(TESTING_PRODUCT_ID, TESTING_BRAND_ID, TESTING_APPLICATION_TIME))
                .thenReturn(Optional.of(validPriceEntity));

        var response = priceService.getPrice(TESTING_PRODUCT_ID, TESTING_BRAND_ID, TESTING_APPLICATION_TIME);

        assertNotNull(response);
        assertEquals(validPriceEntity.getProductId(), response.productId());
        assertEquals(validPriceEntity.getBrandId(), response.brandId());
        assertEquals(validPriceEntity.getPrice(), response.price());

        verify(priceRepository, times(1))
                .findTopApplicablePrices(TESTING_PRODUCT_ID, TESTING_BRAND_ID, TESTING_APPLICATION_TIME);
    }

    @Test
    public void testGetPrice_NotFound() {
        when(priceRepository.findTopApplicablePrices(TESTING_PRODUCT_ID, TESTING_BRAND_ID, TESTING_APPLICATION_TIME))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            priceService.getPrice(TESTING_PRODUCT_ID, TESTING_BRAND_ID, TESTING_APPLICATION_TIME);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(priceRepository, times(1))
                .findTopApplicablePrices(TESTING_PRODUCT_ID, TESTING_BRAND_ID, TESTING_APPLICATION_TIME);
    }

    @Test
    public void testGetPrice_NullProductId() {
        assertThrows(IllegalArgumentException.class, () -> {
            priceService.getPrice(null, TESTING_BRAND_ID, TESTING_APPLICATION_TIME);
        });

        verify(priceRepository, times(0))
                .findTopApplicablePrices(any(), any(), any());
    }

    @Test
    public void testGetPrice_NullBrandId() {
        assertThrows(IllegalArgumentException.class, () -> {
            priceService.getPrice(TESTING_PRODUCT_ID, null, TESTING_APPLICATION_TIME);
        });

        verify(priceRepository, times(0))
                .findTopApplicablePrices(any(), any(), any());
    }

    @Test
    public void testGetPrice_NullApplicationTime() {
        assertThrows(IllegalArgumentException.class, () -> {
            priceService.getPrice(TESTING_PRODUCT_ID, TESTING_BRAND_ID, null);
        });

        verify(priceRepository, times(0))
                .findTopApplicablePrices(any(), any(), any());
    }
}