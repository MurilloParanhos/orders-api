CREATE TABLE order_status_history (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_id    UUID         NOT NULL,
    from_status VARCHAR(30),
    to_status   VARCHAR(30)  NOT NULL,
    reason      VARCHAR(255),
    changed_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),

    CONSTRAINT fk_order_status_history_order FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    CONSTRAINT ck_order_status_history_from_status
        CHECK (from_status IN ('PENDING', 'STOCK_RESERVED', 'CONFIRMED', 'CANCELLED')),
    CONSTRAINT ck_order_status_history_to_status
        CHECK (to_status IN ('PENDING', 'STOCK_RESERVED', 'CONFIRMED', 'CANCELLED'))
);

CREATE INDEX idx_order_status_history_order_id ON order_status_history (order_id, changed_at);
