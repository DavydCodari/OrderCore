ALTER TABLE shopping_carts DROP CONSTRAINT fk_shopping_carts_customer;
ALTER TABLE cart_items DROP CONSTRAINT fk_cart_items_shopping_cart;
ALTER TABLE shopping_carts DROP CONSTRAINT pk_shopping_carts;

ALTER TABLE shopping_carts DROP COLUMN id;
ALTER TABLE shopping_carts DROP COLUMN customer_id;


ALTER TABLE shopping_carts ADD COLUMN shopping_cart_id BIGINT;

ALTER TABLE shopping_carts ADD CONSTRAINT pk_shopping_carts
    PRIMARY KEY (shopping_cart_id);
ALTER TABLE shopping_carts ADD CONSTRAINT fk_shopping_carts_customer
    FOREIGN KEY (shopping_cart_id) REFERENCES users(id);

ALTER TABLE cart_items ADD CONSTRAINT fk_cart_items_shopping_cart
    FOREIGN KEY (shopping_cart_id) REFERENCES shopping_carts(shopping_cart_id);