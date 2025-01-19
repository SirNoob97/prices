package com.companyx.prices.adapter.inbound.rest;

import com.companyx.prices.domain.service.PriceService;
import com.companyx.prices.domain.dto.PriceResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequestMapping("/prices")
@RestController
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @Operation(summary = "Retrieve the applicable price.",
            description = "Get the price for a product at a specific timestamp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of price information"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters", content = @Content(examples = @ExampleObject(value = "The application time is required"))),
            @ApiResponse(responseCode = "404", description = "Price not found", content = @Content(examples = @ExampleObject(value = "404 Price not found")))
    })
    @Validated
    @GetMapping
    public ResponseEntity<PriceResponseDto> applicablePrice(@Parameter(description = "The application timestamp to check the price validity")
                                                            @RequestParam("applicationTime")
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                            @NotNull(message = "The application time is required")
                                                            @Past(message = "The application time cannot be now or in a future time")
                                                                LocalDateTime applicationTime,
                                                            @Parameter(description = "The product ID to get the price")
                                                            @RequestParam("productId")
                                                            @NotNull(message = "Product ID is required")
                                                            @Positive(message = "Product ID must be a positive number")
                                                                Integer productId,
                                                            @Parameter(description = "The brand ID to get the price")
                                                            @RequestParam("brandId")
                                                            @NotNull(message = "Brand ID is required")
                                                            @Positive(message = "Brand ID must be a positive number")
                                                                Integer brandId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(this.priceService.getPrice(productId, brandId, applicationTime));
    }
}
