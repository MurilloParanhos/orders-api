package com.murillo.orders.order;

import com.murillo.orders.product.Product;
import com.murillo.orders.product.ProductRepository;
import com.murillo.orders.shared.BusinessRuleException;
import com.murillo.orders.shared.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    /**
     * Fase 1 (síncrona): cria o pedido e reserva o estoque na mesma transação.
     * Qualquer falha (produto inexistente, estoque insuficiente) desfaz tudo e nenhum pedido é gravado.
     * Na Fase 2 a reserva sai daqui e passa a ser feita por um consumidor da fila.
     */
    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        ensureNoDuplicateProducts(request);

        Order order = new Order(request.customerEmail());
        for (CreateOrderRequest.Item item : request.items()) {
            Product product = productRepository.findByIdAndActiveTrue(item.productId())
                    .orElseThrow(() -> new BusinessRuleException(
                            "Produto %d não existe ou está inativo".formatted(item.productId())));
            product.decreaseStock(item.quantity());
            order.addItem(product, item.quantity());
        }
        order.changeStatus(OrderStatus.STOCK_RESERVED, null);

        // flush para gravar agora e preencher createdAt/updatedAt (via @PrePersist) antes de montar a resposta
        return OrderResponse.from(orderRepository.saveAndFlush(order));
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(UUID id) {
        // O mapeamento acontece dentro da transação, pois itens e histórico são carregados sob demanda (LAZY)
        return orderRepository.findById(id)
                .map(OrderResponse::from)
                .orElseThrow(() -> new NotFoundException("Pedido %s não encontrado".formatted(id)));
    }

    @Transactional(readOnly = true)
    public Page<OrderSummaryResponse> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable).map(OrderSummaryResponse::from);
    }

    private static void ensureNoDuplicateProducts(CreateOrderRequest request) {
        Set<Long> seen = new HashSet<>();
        for (CreateOrderRequest.Item item : request.items()) {
            if (!seen.add(item.productId())) {
                throw new BusinessRuleException(
                        "Produto %d aparece mais de uma vez no pedido; some as quantidades em um único item"
                                .formatted(item.productId()));
            }
        }
    }
}
