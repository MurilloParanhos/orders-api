package com.murillo.orders.order;

import java.util.Set;

public enum OrderStatus {

    PENDING,
    STOCK_RESERVED,
    CONFIRMED,
    CANCELLED;

    /** Máquina de estados do pedido: define quais transições são permitidas a partir de cada status. */
    public boolean canTransitionTo(OrderStatus next) {
        return switch (this) {
            case PENDING -> Set.of(STOCK_RESERVED, CANCELLED).contains(next);
            case STOCK_RESERVED -> Set.of(CONFIRMED, CANCELLED).contains(next);
            case CONFIRMED, CANCELLED -> false;
        };
    }
}
