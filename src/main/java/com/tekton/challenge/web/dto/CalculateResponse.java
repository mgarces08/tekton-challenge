package com.tekton.challenge.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Calculation result with percentage applied")
public record CalculateResponse(
        @Schema(description = "Direct sum of num1 + num2", example = "30.0")
        BigDecimal sum,
        @Schema(description = "Percentage applied to the total", example = "15")
        BigDecimal percentage,
        @Schema(description = "Total with percentage included", example = "34.5")
        BigDecimal total
) {
    @Override
    public String toString() {
        return "{" +
                "sum=" + sum +
                ", percentage=" + percentage +
                ", total=" + total +
                "}";
    }
}
