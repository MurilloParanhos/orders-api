package com.murillo.orders.product;

import com.murillo.orders.shared.AuditableEntity;
import com.murillo.orders.shared.BusinessRuleException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String sku;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int stockQuantity;

    @Column(nullable = false)
    private boolean active;

    public Product(String sku, String name, String description, BigDecimal price, int stockQuantity) {
        update(sku, name, description, price, stockQuantity);
        this.active = true;
    }

    public void update(String sku, String name, String description, BigDecimal price, int stockQuantity) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public void deactivate() {
        this.active = false;
    }

    public void decreaseStock(int quantity) {
        if (quantity > stockQuantity) {
            throw new BusinessRuleException("Estoque insuficiente para o produto %s (disponível: %d, solicitado: %d)"
                    .formatted(sku, stockQuantity, quantity));
        }
        this.stockQuantity -= quantity;
    }
}
