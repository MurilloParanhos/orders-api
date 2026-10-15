package com.murillo.orders.shared;

/** Requisição válida no formato, mas viola uma regra de negócio (ex.: estoque insuficiente). Mapeada para HTTP 422. */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
