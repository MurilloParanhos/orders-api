package com.murillo.orders.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/** Versão resumida para listagem: sem itens e histórico, evitando consultas extras por pedido. */
public record OrderSummaryResponse(
        UUID id,
        String customerEmail,
        OrderStatus status,
        BigDecimal totalAmount,
        Instant createdAt
) {

    public static OrderSummaryResponse from(Order order) {
        return new OrderSummaryResponse(
                order.getId(),
                order.getCustomerEmail(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt()
        );
    }
}
