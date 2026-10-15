package com.murillo.orders.shared;

/** Recurso solicitado não existe. Mapeada para HTTP 404. */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
