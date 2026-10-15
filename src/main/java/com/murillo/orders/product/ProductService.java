package com.murillo.orders.product;

import com.murillo.orders.shared.ConflictException;
import com.murillo.orders.shared.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse create(ProductRequest request) {
        if (productRepository.existsBySku(request.sku())) {
            throw new ConflictException("Já existe um produto com o SKU " + request.sku());
        }
        Product product = new Product(
                request.sku(),
                request.name(),
                request.description(),
                request.price(),
                request.stockQuantity()
        );
        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(Pageable pageable) {
        return productRepository.findAllByActiveTrue(pageable).map(ProductResponse::from);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return ProductResponse.from(getActiveProduct(id));
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = getActiveProduct(id);
        if (productRepository.existsBySkuAndIdNot(request.sku(), id)) {
            throw new ConflictException("Já existe um produto com o SKU " + request.sku());
        }
        product.update(
                request.sku(),
                request.name(),
                request.description(),
                request.price(),
                request.stockQuantity()
        );
        // flush força o UPDATE agora, disparando o @PreUpdate antes de montar a resposta (updatedAt atualizado)
        return ProductResponse.from(productRepository.saveAndFlush(product));
    }

    @Transactional
    public void delete(Long id) {
        getActiveProduct(id).deactivate();
    }

    private Product getActiveProduct(Long id) {
        return productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Produto %d não encontrado".formatted(id)));
    }
}
