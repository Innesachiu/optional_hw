CREATE DATABASE IF NOT EXISTS campus_marketplace
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_unicode_ci;

USE campus_marketplace;

CREATE TABLE IF NOT EXISTS users (
user_id INT AUTO_INCREMENT PRIMARY KEY,
username VARCHAR(50) UNIQUE NOT NULL,
email VARCHAR(100) UNIQUE NOT NULL,
password_hash VARCHAR(255) NOT NULL,
avatar_url VARCHAR(255),
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS categories (
category_id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
product_id INT AUTO_INCREMENT PRIMARY KEY,
seller_id INT NOT NULL,
category_id INT,
title VARCHAR(255) NOT NULL,
price INT NOT NULL,
description TEXT,
status ENUM('ACTIVE', 'SOLD', 'DELETED') DEFAULT 'ACTIVE',
search_hit_count INT DEFAULT 0,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (seller_id) REFERENCES users(user_id),
FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

CREATE TABLE IF NOT EXISTS product_images (
image_id INT AUTO_INCREMENT PRIMARY KEY,
product_id INT NOT NULL,
image_url VARCHAR(255) NOT NULL,
sort_order INT DEFAULT 0,
FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders (
order_id INT AUTO_INCREMENT PRIMARY KEY,
buyer_id INT NOT NULL,
seller_id INT NOT NULL,
product_id INT NOT NULL,
amount INT NOT NULL,
status ENUM('PENDING', 'COMPLETED', 'CANCELED') DEFAULT 'PENDING',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (buyer_id) REFERENCES users(user_id),
FOREIGN KEY (seller_id) REFERENCES users(user_id),
FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE TABLE IF NOT EXISTS search_logs (
search_id INT AUTO_INCREMENT PRIMARY KEY,
user_id INT,
keyword VARCHAR(100) NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (user_id) REFERENCES users(user_id)
);

INSERT IGNORE INTO categories (name) VALUES
('教科書'),
('電子產品'),
('生活用品'),
('文具'),
('宿舍用品'),
('其他');
