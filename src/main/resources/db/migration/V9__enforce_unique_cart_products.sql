ALTER TABLE cart_items
    ADD CONSTRAINT uk_cart_items_cart_product UNIQUE (shopping_cart_id, product_id);

ALTER TABLE cart_items ADD COLUMN version BIGINT NOT NULL DEFAULT 0;
ALTER TABLE cart_items ALTER COLUMN version DROP DEFAULT;
