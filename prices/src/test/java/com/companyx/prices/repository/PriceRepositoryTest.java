package com.companyx.prices.repository;

import com.companyx.prices.adapter.outbound.persistence.PriceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static com.companyx.prices.Constants.TESTING_PRODUCT_ID;
import static com.companyx.prices.Constants.TESTING_BRAND_ID;
import static com.companyx.prices.Constants.TESTING_APPLICATION_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = "classpath:/sql/initial_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class PriceRepositoryTest {

    @Autowired
    private PriceRepository priceRepository;

    @Test
    void testFindApplicablePrice() {
        var result = priceRepository.findTopApplicablePrices(TESTING_PRODUCT_ID, TESTING_BRAND_ID, TESTING_APPLICATION_TIME);

        assertThat(result).isPresent();
        var price = result.get();
        assertEquals(0, price.getPriority());
        assertEquals(1, price.getPriceList());
        assertEquals(35.50, price.getPrice());
        assertEquals(LocalDateTime.of(2020, 6, 14, 0, 0, 0), price.getStartDate());
        assertEquals(LocalDateTime.of(2020, 12, 31, 23, 59, 59), price.getEndDate());
    }

    @Test
    void testFindApplicablePriceBaseWithHighestPriority() {
        var result = priceRepository.findTopApplicablePrices(TESTING_PRODUCT_ID, TESTING_BRAND_ID, LocalDateTime.of(2020, 6, 14, 16, 0, 0));

        assertThat(result).isPresent();
        var price = result.get();
        assertEquals(1, price.getPriority());
        assertEquals(2, price.getPriceList());
        assertEquals(25.45, price.getPrice());
        assertEquals(LocalDateTime.of(2020, 6, 14, 15, 0, 0), price.getStartDate());
        assertEquals(LocalDateTime.of(2020, 6, 14, 18, 30, 0), price.getEndDate());
    }

    @Test
    void testNoApplicablePriceFound() {
        var result = priceRepository.findTopApplicablePrices(TESTING_PRODUCT_ID, TESTING_BRAND_ID, LocalDateTime.of(2000, 6, 14, 23, 0, 0));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testNoApplicablePriceFoundWhenProductIdIsNull() {
        var result = assertDoesNotThrow(() -> priceRepository.findTopApplicablePrices(null, TESTING_BRAND_ID, TESTING_APPLICATION_TIME));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testNoApplicablePriceFoundWhenBrandIdIsNull() {
        var result = assertDoesNotThrow(() -> priceRepository.findTopApplicablePrices(TESTING_PRODUCT_ID, null, TESTING_APPLICATION_TIME));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testNoApplicablePriceFoundWhenApplicationTimeIsNull() {
        var result = assertDoesNotThrow(() -> priceRepository.findTopApplicablePrices(TESTING_PRODUCT_ID, TESTING_BRAND_ID, null));
        assertTrue(result.isEmpty());
    }
}
