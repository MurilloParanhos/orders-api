package com.murillo.orders.shared;

/** Requisição conflita com o estado atual (ex.: SKU duplicado). Mapeada para HTTP 409. */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
