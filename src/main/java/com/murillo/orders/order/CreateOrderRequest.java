package com.murillo.orders.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateOrderRequest(
        @NotBlank @Email @Size(max = 255) String customerEmail,
        @NotEmpty @Size(max = 50) List<@Valid @NotNull Item> items
) {

    public record Item(
            @NotNull Long productId,
            @NotNull @Positive Integer quantity
    ) {
    }
}
