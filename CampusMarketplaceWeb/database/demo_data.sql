-- CampusMarketplaceWeb demo data
-- Demo account password for every user: demo1234
-- Password hash expression: SHA2('demo1234', 256)
-- This script is designed to be idempotent and safe to rerun.

USE campus_marketplace;

-- 1) Demo users
INSERT INTO users (username, email, password_hash, avatar_url)
SELECT 'seller_alice', 'seller_alice@example.com', SHA2('demo1234', 256), NULL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'seller_alice');

INSERT INTO users (username, email, password_hash, avatar_url)
SELECT 'seller_ben', 'seller_ben@example.com', SHA2('demo1234', 256), NULL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'seller_ben');

INSERT INTO users (username, email, password_hash, avatar_url)
SELECT 'buyer_chloe', 'buyer_chloe@example.com', SHA2('demo1234', 256), NULL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'buyer_chloe');

INSERT INTO users (username, email, password_hash, avatar_url)
SELECT 'buyer_david', 'buyer_david@example.com', SHA2('demo1234', 256), NULL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'buyer_david');

-- 2) Demo categories
INSERT IGNORE INTO categories (name) VALUES
('教科書'),
('電子產品'),
('生活用品'),
('文具'),
('宿舍用品'),
('其他');

-- 3) Demo products: mostly ACTIVE, with a few SOLD products for order history.
INSERT INTO products (seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
  (SELECT user_id FROM users WHERE username = 'seller_alice'),
  (SELECT category_id FROM categories WHERE name = '教科書'),
  '資料庫系統概論課本',
  450,
  '資料庫課程用書，內頁有少量筆記，適合資管與資工課程。',
  'ACTIVE',
  12
WHERE NOT EXISTS (SELECT 1 FROM products WHERE title = '資料庫系統概論課本');

INSERT INTO products (seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
  (SELECT user_id FROM users WHERE username = 'seller_alice'),
  (SELECT category_id FROM categories WHERE name = '教科書'),
  'Java 程式設計二手書',
  380,
  'Java 入門與物件導向教材，適合期中期末複習。',
  'ACTIVE',
  18
WHERE NOT EXISTS (SELECT 1 FROM products WHERE title = 'Java 程式設計二手書');

INSERT INTO products (seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
  (SELECT user_id FROM users WHERE username = 'seller_ben'),
  (SELECT category_id FROM categories WHERE name = '電子產品'),
  'Logitech 無線滑鼠',
  300,
  '功能正常，附 USB 接收器，適合宿舍與圖書館使用。',
  'ACTIVE',
  25
WHERE NOT EXISTS (SELECT 1 FROM products WHERE title = 'Logitech 無線滑鼠');

INSERT INTO products (seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
  (SELECT user_id FROM users WHERE username = 'seller_ben'),
  (SELECT category_id FROM categories WHERE name = '電子產品'),
  'Type-C 充電線 1.5m',
  120,
  '備用充電線，外觀良好，可支援一般手機充電。',
  'ACTIVE',
  9
WHERE NOT EXISTS (SELECT 1 FROM products WHERE title = 'Type-C 充電線 1.5m');

INSERT INTO products (seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
  (SELECT user_id FROM users WHERE username = 'seller_alice'),
  (SELECT category_id FROM categories WHERE name = '生活用品'),
  '不鏽鋼保溫杯',
  180,
  '容量 500ml，已清潔，適合上課帶水。',
  'ACTIVE',
  6
WHERE NOT EXISTS (SELECT 1 FROM products WHERE title = '不鏽鋼保溫杯');

INSERT INTO products (seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
  (SELECT user_id FROM users WHERE username = 'seller_ben'),
  (SELECT category_id FROM categories WHERE name = '生活用品'),
  '折疊雨傘',
  150,
  '輕便折疊傘，放包包剛好，校園通勤實用。',
  'ACTIVE',
  5
WHERE NOT EXISTS (SELECT 1 FROM products WHERE title = '折疊雨傘');

INSERT INTO products (seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
  (SELECT user_id FROM users WHERE username = 'seller_alice'),
  (SELECT category_id FROM categories WHERE name = '文具'),
  'A4 活頁夾與筆記紙組',
  90,
  '含 A4 活頁夾與一包筆記紙，適合整理課堂講義。',
  'ACTIVE',
  4
WHERE NOT EXISTS (SELECT 1 FROM products WHERE title = 'A4 活頁夾與筆記紙組');

INSERT INTO products (seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
  (SELECT user_id FROM users WHERE username = 'seller_ben'),
  (SELECT category_id FROM categories WHERE name = '文具'),
  '工程計算機',
  520,
  '工程數學與統計課可用，按鍵正常。',
  'ACTIVE',
  21
WHERE NOT EXISTS (SELECT 1 FROM products WHERE title = '工程計算機');

INSERT INTO products (seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
  (SELECT user_id FROM users WHERE username = 'seller_alice'),
  (SELECT category_id FROM categories WHERE name = '宿舍用品'),
  '宿舍 LED 檯燈',
  260,
  '三段亮度，可 USB 供電，適合書桌使用。',
  'ACTIVE',
  16
WHERE NOT EXISTS (SELECT 1 FROM products WHERE title = '宿舍 LED 檯燈');

INSERT INTO products (seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
  (SELECT user_id FROM users WHERE username = 'seller_ben'),
  (SELECT category_id FROM categories WHERE name = '宿舍用品'),
  '床邊收納掛袋',
  130,
  '可掛在宿舍床架旁，放手機、眼鏡、充電線。',
  'ACTIVE',
  7
WHERE NOT EXISTS (SELECT 1 FROM products WHERE title = '床邊收納掛袋');

INSERT INTO products (seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
  (SELECT user_id FROM users WHERE username = 'seller_alice'),
  (SELECT category_id FROM categories WHERE name = '其他'),
  '校園社團帆布袋',
  100,
  '乾淨少用，可裝課本與筆電配件。',
  'ACTIVE',
  3
WHERE NOT EXISTS (SELECT 1 FROM products WHERE title = '校園社團帆布袋');

INSERT INTO products (seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
  (SELECT user_id FROM users WHERE username = 'seller_ben'),
  (SELECT category_id FROM categories WHERE name = '其他'),
  '二手腳踏車車燈',
  200,
  '夜間校園騎車可用，USB 充電。',
  'ACTIVE',
  8
WHERE NOT EXISTS (SELECT 1 FROM products WHERE title = '二手腳踏車車燈');

INSERT INTO products (seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
  (SELECT user_id FROM users WHERE username = 'seller_alice'),
  (SELECT category_id FROM categories WHERE name = '電子產品'),
  '二手藍牙鍵盤',
  420,
  '可連筆電與平板，已售出示範資料。',
  'SOLD',
  14
WHERE NOT EXISTS (SELECT 1 FROM products WHERE title = '二手藍牙鍵盤');

INSERT INTO products (seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
  (SELECT user_id FROM users WHERE username = 'seller_ben'),
  (SELECT category_id FROM categories WHERE name = '宿舍用品'),
  '小型收納抽屜',
  250,
  '宿舍桌下可放，已售出示範資料。',
  'SOLD',
  10
WHERE NOT EXISTS (SELECT 1 FROM products WHERE title = '小型收納抽屜');

INSERT INTO products (seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
  (SELECT user_id FROM users WHERE username = 'seller_alice'),
  (SELECT category_id FROM categories WHERE name = '教科書'),
  '英文閱讀課本',
  220,
  '通識英文閱讀課指定書，已售出示範資料。',
  'SOLD',
  11
WHERE NOT EXISTS (SELECT 1 FROM products WHERE title = '英文閱讀課本');

-- 4) Optional product images for frontend/detail placeholders.
INSERT INTO product_images (product_id, image_url, sort_order)
SELECT (SELECT product_id FROM products WHERE title = '資料庫系統概論課本'), 'assets/images/database-book-demo.jpg', 1
WHERE NOT EXISTS (
  SELECT 1 FROM product_images
  WHERE product_id = (SELECT product_id FROM products WHERE title = '資料庫系統概論課本')
    AND image_url = 'assets/images/database-book-demo.jpg'
);

INSERT INTO product_images (product_id, image_url, sort_order)
SELECT (SELECT product_id FROM products WHERE title = 'Logitech 無線滑鼠'), 'assets/images/wireless-mouse-demo.jpg', 1
WHERE NOT EXISTS (
  SELECT 1 FROM product_images
  WHERE product_id = (SELECT product_id FROM products WHERE title = 'Logitech 無線滑鼠')
    AND image_url = 'assets/images/wireless-mouse-demo.jpg'
);

INSERT INTO product_images (product_id, image_url, sort_order)
SELECT (SELECT product_id FROM products WHERE title = '宿舍 LED 檯燈'), 'assets/images/desk-lamp-demo.jpg', 1
WHERE NOT EXISTS (
  SELECT 1 FROM product_images
  WHERE product_id = (SELECT product_id FROM products WHERE title = '宿舍 LED 檯燈')
    AND image_url = 'assets/images/desk-lamp-demo.jpg'
);

-- 5) Demo orders for My Orders page. Orders point to SOLD products.
INSERT INTO orders (buyer_id, seller_id, product_id, amount, status, created_at)
SELECT
  (SELECT user_id FROM users WHERE username = 'buyer_chloe'),
  (SELECT seller_id FROM products WHERE title = '二手藍牙鍵盤'),
  (SELECT product_id FROM products WHERE title = '二手藍牙鍵盤'),
  (SELECT price FROM products WHERE title = '二手藍牙鍵盤'),
  'COMPLETED',
  NOW() - INTERVAL 2 DAY
WHERE NOT EXISTS (
  SELECT 1 FROM orders
  WHERE buyer_id = (SELECT user_id FROM users WHERE username = 'buyer_chloe')
    AND product_id = (SELECT product_id FROM products WHERE title = '二手藍牙鍵盤')
);

INSERT INTO orders (buyer_id, seller_id, product_id, amount, status, created_at)
SELECT
  (SELECT user_id FROM users WHERE username = 'buyer_chloe'),
  (SELECT seller_id FROM products WHERE title = '小型收納抽屜'),
  (SELECT product_id FROM products WHERE title = '小型收納抽屜'),
  (SELECT price FROM products WHERE title = '小型收納抽屜'),
  'COMPLETED',
  NOW() - INTERVAL 1 DAY
WHERE NOT EXISTS (
  SELECT 1 FROM orders
  WHERE buyer_id = (SELECT user_id FROM users WHERE username = 'buyer_chloe')
    AND product_id = (SELECT product_id FROM products WHERE title = '小型收納抽屜')
);

INSERT INTO orders (buyer_id, seller_id, product_id, amount, status, created_at)
SELECT
  (SELECT user_id FROM users WHERE username = 'buyer_david'),
  (SELECT seller_id FROM products WHERE title = '英文閱讀課本'),
  (SELECT product_id FROM products WHERE title = '英文閱讀課本'),
  (SELECT price FROM products WHERE title = '英文閱讀課本'),
  'COMPLETED',
  NOW() - INTERVAL 3 DAY
WHERE NOT EXISTS (
  SELECT 1 FROM orders
  WHERE buyer_id = (SELECT user_id FROM users WHERE username = 'buyer_david')
    AND product_id = (SELECT product_id FROM products WHERE title = '英文閱讀課本')
);

-- Keep order demo products marked as SOLD even after reruns.
UPDATE products
SET status = 'SOLD'
WHERE title IN ('二手藍牙鍵盤', '小型收納抽屜', '英文閱讀課本');

-- 6) Demo search logs for Hot Keywords page.
INSERT INTO search_logs (user_id, keyword, created_at)
SELECT (SELECT user_id FROM users WHERE username = 'buyer_chloe'), '教科書', NOW() - INTERVAL 1 DAY
WHERE NOT EXISTS (
  SELECT 1 FROM search_logs
  WHERE user_id = (SELECT user_id FROM users WHERE username = 'buyer_chloe')
    AND keyword = '教科書'
);

INSERT INTO search_logs (user_id, keyword, created_at)
SELECT (SELECT user_id FROM users WHERE username = 'buyer_chloe'), '滑鼠', NOW() - INTERVAL 2 DAY
WHERE NOT EXISTS (
  SELECT 1 FROM search_logs
  WHERE user_id = (SELECT user_id FROM users WHERE username = 'buyer_chloe')
    AND keyword = '滑鼠'
);

INSERT INTO search_logs (user_id, keyword, created_at)
SELECT (SELECT user_id FROM users WHERE username = 'buyer_david'), '宿舍', NOW() - INTERVAL 2 DAY
WHERE NOT EXISTS (
  SELECT 1 FROM search_logs
  WHERE user_id = (SELECT user_id FROM users WHERE username = 'buyer_david')
    AND keyword = '宿舍'
);

INSERT INTO search_logs (user_id, keyword, created_at)
SELECT (SELECT user_id FROM users WHERE username = 'seller_alice'), '計算機', NOW() - INTERVAL 3 DAY
WHERE NOT EXISTS (
  SELECT 1 FROM search_logs
  WHERE user_id = (SELECT user_id FROM users WHERE username = 'seller_alice')
    AND keyword = '計算機'
);

INSERT INTO search_logs (user_id, keyword, created_at)
SELECT (SELECT user_id FROM users WHERE username = 'seller_ben'), '教科書', NOW() - INTERVAL 4 DAY
WHERE NOT EXISTS (
  SELECT 1 FROM search_logs
  WHERE user_id = (SELECT user_id FROM users WHERE username = 'seller_ben')
    AND keyword = '教科書'
);

INSERT INTO search_logs (user_id, keyword, created_at)
SELECT NULL, '檯燈', NOW() - INTERVAL 5 DAY
WHERE NOT EXISTS (
  SELECT 1 FROM search_logs
  WHERE user_id IS NULL AND keyword = '檯燈'
);

-- 7) Verification queries for MySQL Workbench.
SELECT user_id, username, email FROM users;
SELECT category_id, name FROM categories;
SELECT product_id, title, price, status FROM products ORDER BY product_id DESC;
SELECT order_id, buyer_id, product_id, status, created_at FROM orders ORDER BY order_id DESC;
SELECT keyword, COUNT(*) AS search_count FROM search_logs WHERE created_at >= NOW() - INTERVAL 7 DAY GROUP BY keyword ORDER BY search_count DESC, keyword ASC;
