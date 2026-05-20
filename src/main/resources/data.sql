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

-- Users (passwords: admin123, manager123, user123)
INSERT INTO users (username, password, role, enabled, created_at)
VALUES ('admin', '$2b$10$o9Ol4GFbjo2Yoed9.lq3/uTTDLVLeNBs2xUVri4BbxMsiaOPY8QQO', 'ADMIN', TRUE, CURRENT_TIMESTAMP);

INSERT INTO users (username, password, role, enabled, created_at)
VALUES ('manager', '$2b$10$kWWQOXG0Fw1UApr3hjpngut80BjowEPDXQOjxWnM/CgW2sqXLZmBy', 'MANAGER', TRUE, CURRENT_TIMESTAMP);

INSERT INTO users (username, password, role, enabled, created_at)
VALUES ('user', '$2b$10$Gvwsl3VKd7vSeE96bMX4MeS4yGNkeXXnJFE25YZ5YdHbsxJ8gzs3K', 'USER', TRUE, CURRENT_TIMESTAMP);