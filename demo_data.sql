USE campus_marketplace;

-- Demo users (password hash is SHA-256 for plain password: demo1234)
INSERT IGNORE INTO users (user_id, username, email, password_hash, avatar_url) VALUES
(1, 'alice_demo', 'alice_demo@example.com', '8f61ad5cfa0c471c8cbf82049836681f904b2f4e5e759b9f7f5d85f2f3b8d4a9', NULL),
(2, 'bob_demo', 'bob_demo@example.com', '8f61ad5cfa0c471c8cbf82049836681f904b2f4e5e759b9f7f5d85f2f3b8d4a9', NULL),
(3, 'cindy_demo', 'cindy_demo@example.com', '8f61ad5cfa0c471c8cbf82049836681f904b2f4e5e759b9f7f5d85f2f3b8d4a9', NULL);

-- Demo products with different categories and statuses
INSERT IGNORE INTO products (product_id, seller_id, category_id, title, price, description, status, search_hit_count) VALUES
(101, 1, 1, 'Calculus Textbook', 350, 'Used textbook, clean pages, suitable for freshmen.', 'ACTIVE', 0),
(102, 2, 2, 'Wireless Mouse', 280, 'Bluetooth mouse, works well with laptop.', 'ACTIVE', 0),
(103, 3, 3, 'Campus Hoodie', 500, 'Medium size hoodie, only worn a few times.', 'ACTIVE', 0),
(104, 1, 4, 'Desk Lamp', 220, 'LED desk lamp for dorm room.', 'SOLD', 0),
(105, 2, 5, 'Yoga Mat', 300, 'Lightweight yoga mat, easy to carry.', 'ACTIVE', 0),
(106, 3, 6, 'Storage Box Set', 180, 'Set of 3 storage boxes for dorm organization.', 'ACTIVE', 0);
