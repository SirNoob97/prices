package com.companyx.prices.controller;

import com.companyx.prices.adapter.inbound.rest.PriceController;
import com.companyx.prices.domain.dto.PriceResponseDto;
import com.companyx.prices.domain.service.PriceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static com.companyx.prices.Constants.TESTING_PRODUCT_ID;
import static com.companyx.prices.Constants.TESTING_BRAND_ID;
import static com.companyx.prices.Constants.TESTING_APPLICATION_TIME;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PriceController.class)
public class PriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PriceService priceService;

    @Test
    public void whenValidParams_thenReturnsPrice() throws Exception {
        PriceResponseDto expectedPrice = new PriceResponseDto(
                TESTING_PRODUCT_ID, TESTING_BRAND_ID, 1, TESTING_APPLICATION_TIME, TESTING_APPLICATION_TIME, 35.50
        );

        when(priceService.getPrice(TESTING_PRODUCT_ID, TESTING_BRAND_ID, TESTING_APPLICATION_TIME)).thenReturn(expectedPrice);

        mockMvc.perform(get("/prices")
                        .param("applicationTime", "2020-06-14 14:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(TESTING_PRODUCT_ID))
                .andExpect(jsonPath("$.brandId").value(TESTING_BRAND_ID))
                .andExpect(jsonPath("$.price").value(35.50));
    }

    @Test
    public void whenMissingProductId_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationTime", "2020-06-14 15:00:00")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenFutureDate_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationTime","2120-06-14 15:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void whenNegativeProductId_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationTime", "2020-06-14 15:00:00")
                        .param("productId", "-1")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void whenNullBrandId_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationTime", "2020-06-14 15:00:00")
                        .param("productId", "35455"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPriceNotFound_thenReturnsNotFound() throws Exception {
        when(priceService.getPrice(TESTING_PRODUCT_ID, TESTING_BRAND_ID, TESTING_APPLICATION_TIME)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No applicable price found."));

        mockMvc.perform(get("/prices")
                        .param("applicationTime", "2020-06-14 14:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isNotFound());
    }
}