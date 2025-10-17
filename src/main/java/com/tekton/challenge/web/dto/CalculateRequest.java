package com.tekton.challenge.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CalculateRequest(
    @NotNull @Schema(example = "10") BigDecimal num1,
    @NotNull @Schema(example = "20") BigDecimal num2
) {
    @Override
    public String toString() {
        return "{" +
                "num1=" + num1 +
                ", num2=" + num2 +
                "}";
    }
}
