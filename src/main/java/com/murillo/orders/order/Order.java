package com.murillo.orders.order;

import com.murillo.orders.product.Product;
import com.murillo.orders.shared.AuditableEntity;
import com.murillo.orders.shared.BusinessRuleException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String customerEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    private String failureReason;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<OrderStatusHistory> statusHistory = new ArrayList<>();

    public Order(String customerEmail) {
        this.customerEmail = customerEmail;
        this.totalAmount = BigDecimal.ZERO;
        this.status = OrderStatus.PENDING;
        this.statusHistory.add(new OrderStatusHistory(this, null, OrderStatus.PENDING, null));
    }

    public void addItem(Product product, int quantity) {
        OrderItem item = new OrderItem(this, product, quantity);
        items.add(item);
        totalAmount = totalAmount.add(item.getSubtotal());
    }

    public void changeStatus(OrderStatus newStatus, String reason) {
        if (!status.canTransitionTo(newStatus)) {
            throw new BusinessRuleException("Transição de status inválida: %s -> %s".formatted(status, newStatus));
        }
        statusHistory.add(new OrderStatusHistory(this, status, newStatus, reason));
        this.status = newStatus;
        if (newStatus == OrderStatus.CANCELLED) {
            this.failureReason = reason;
        }
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<OrderStatusHistory> getStatusHistory() {
        return Collections.unmodifiableList(statusHistory);
    }
}
