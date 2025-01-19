package com.companyx.prices.controller;

import com.companyx.prices.domain.dto.PriceResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:/sql/initial_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class PriceControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testCase1() {
        var query = "/prices?applicationTime=2020-06-14 10:00:00&productId=35455&brandId=1";
        var response = testRestTemplate.getForEntity(query, PriceResponseDto.class);

        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        var body = response.getBody();
        assertEquals(35455, body.productId());
        assertEquals(1, body.brandId());
        assertEquals(1, body.priceList());
        assertEquals(35.50, body.price());
    }

    @Test
    void testCase2() {
        var query = "/prices?applicationTime=2020-06-14 16:00:00&productId=35455&brandId=1";
        var response = testRestTemplate.getForEntity(query, PriceResponseDto.class);

        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        var body = response.getBody();
        assertEquals(35455, body.productId());
        assertEquals(1, body.brandId());
        assertEquals(2, body.priceList());
        assertEquals(25.45, body.price());
    }

    @Test
    void testCase3() {
        var query = "/prices?applicationTime=2020-06-14 21:00:00&productId=35455&brandId=1";
        var response = testRestTemplate.getForEntity(query, PriceResponseDto.class);

        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        var body = response.getBody();
        assertEquals(35455, body.productId());
        assertEquals(1, body.brandId());
        assertEquals(1, body.priceList());
        assertEquals(35.50, body.price());
    }

    @Test
    void testCase4() {
        var query = "/prices?applicationTime=2020-06-15 10:00:00&productId=35455&brandId=1";
        var response = testRestTemplate.getForEntity(query, PriceResponseDto.class);

        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        var body = response.getBody();
        assertEquals(35455, body.productId());
        assertEquals(1, body.brandId());
        assertEquals(3, body.priceList());
        assertEquals(30.50, body.price());
    }

    @Test
    void testCase5() {
        var query = "/prices?applicationTime=2020-06-16 21:00:00&productId=35455&brandId=1";
        var response = testRestTemplate.getForEntity(query, PriceResponseDto.class);

        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        var body = response.getBody();
        assertEquals(35455, body.productId());
        assertEquals(1, body.brandId());
        assertEquals(4, body.priceList());
        assertEquals(38.95, body.price());
    }
}
