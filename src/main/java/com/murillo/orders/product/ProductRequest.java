package com.murillo.orders.product;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/** Corpo de criação (POST) e atualização completa (PUT) de produto. */
public record ProductRequest(
        @NotBlank @Size(max = 50) String sku,
        @NotBlank @Size(max = 150) String name,
        @Size(max = 2000) String description,
        @NotNull @PositiveOrZero @Digits(integer = 17, fraction = 2) BigDecimal price,
        @NotNull @PositiveOrZero Integer stockQuantity
) {
}
