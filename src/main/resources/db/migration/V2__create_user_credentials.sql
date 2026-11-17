CREATE TABLE user_credentials(
    customer_id BIGINT NOT NULL,
    hashed_password VARCHAR(255) NOT NULL,

    CONSTRAINT pk_user_credentials PRIMARY KEY (customer_id),
    CONSTRAINT fk_user_credentials_user FOREIGN KEY (customer_id) REFERENCES users(id)
);
