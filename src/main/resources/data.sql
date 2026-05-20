INSERT INTO categories (name, description)
VALUES ('Electronics', 'Electronic devices and gadgets');

INSERT INTO categories (name, description)
VALUES ('Accessories', 'Computer and mobile accessories');

INSERT INTO categories (name, description)
VALUES ('Peripherals', 'Keyboards, mice, and input devices');

INSERT INTO categories (name, description)
VALUES ('Displays', 'Monitors and screens');

INSERT INTO categories (name, description)
VALUES ('Storage', 'Hard drives, SSDs, and memory cards');

INSERT INTO products (name, description, price, stock_quantity, category_id, created_at, updated_at)
VALUES ('Laptop Pro 15', 'High-performance laptop', 1499.99, 25, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO products (name, description, price, stock_quantity, category_id, created_at, updated_at)
VALUES ('Wireless Mouse', 'Bluetooth mouse', 29.50, 200, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO products (name, description, price, stock_quantity, category_id, created_at, updated_at)
VALUES ('Mechanical Keyboard', 'RGB backlit', 129.00, 75, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO products (name, description, price, stock_quantity, category_id, created_at, updated_at)
VALUES ('USB-C Hub', '7-in-1 adapter', 49.99, 150, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO products (name, description, price, stock_quantity, category_id, created_at, updated_at)
VALUES ('Monitor 27"', '4K IPS display', 399.00, 40, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

