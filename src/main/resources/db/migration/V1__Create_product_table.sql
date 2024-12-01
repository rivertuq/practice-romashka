CREATE TABLE product (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description VARCHAR(4096),
                         price DECIMAL(10,2) DEFAULT 0.00,
                         in_stock BOOLEAN DEFAULT FALSE
);