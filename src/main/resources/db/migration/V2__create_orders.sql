CREATE TABLE orders (
    id             UUID PRIMARY KEY,
    customer_email VARCHAR(255)   NOT NULL,
    status         VARCHAR(30)    NOT NULL,
    total_amount   NUMERIC(19, 2) NOT NULL,
    failure_reason VARCHAR(255),
    created_at     TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ    NOT NULL DEFAULT now(),

    CONSTRAINT ck_orders_status CHECK (status IN ('PENDING', 'STOCK_RESERVED', 'CONFIRMED', 'CANCELLED')),
    CONSTRAINT ck_orders_total_non_negative CHECK (total_amount >= 0)
);
