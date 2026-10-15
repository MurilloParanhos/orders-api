package com.murillo.orders.shared;

import org.springframework.data.domain.Page;

import java.util.List;

/** Formato estável de paginação para a API, independente da serialização interna do Spring Data. */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
