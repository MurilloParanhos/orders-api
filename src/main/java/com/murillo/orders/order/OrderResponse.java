package com.murillo.orders.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/** Detalhe completo do pedido, com itens e histórico de status. */
public record OrderResponse(
        UUID id,
        String customerEmail,
        OrderStatus status,
        BigDecimal totalAmount,
        String failureReason,
        List<Item> items,
        List<StatusChange> statusHistory,
        Instant createdAt,
        Instant updatedAt
) {

    public record Item(Long productId, String productName, int quantity, BigDecimal unitPrice, BigDecimal subtotal) {

        static Item from(OrderItem item) {
            return new Item(
                    item.getProduct().getId(),
                    item.getProductName(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getSubtotal()
            );
        }
    }

    public record StatusChange(OrderStatus from, OrderStatus to, String reason, Instant changedAt) {

        static StatusChange from(OrderStatusHistory history) {
            return new StatusChange(
                    history.getFromStatus(),
                    history.getToStatus(),
                    history.getReason(),
                    history.getChangedAt()
            );
        }
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerEmail(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getFailureReason(),
                order.getItems().stream().map(Item::from).toList(),
                order.getStatusHistory().stream().map(StatusChange::from).toList(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
