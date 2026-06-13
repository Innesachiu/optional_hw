USE campus_marketplace;

-- =====================================================
-- Demo Data for CampusMarketplaceWeb
-- 所有 demo 帳號密碼都是 demo1234
-- =====================================================

-- =====================================================
-- 1. Users
-- =====================================================

INSERT INTO users(username, email, password_hash, avatar_url)
SELECT 'seller_alice', '[seller_alice@example.com](mailto:seller_alice@example.com)', SHA2('demo1234', 256), NULL
WHERE NOT EXISTS (
SELECT 1 FROM users WHERE username = 'seller_alice'
);

INSERT INTO users(username, email, password_hash, avatar_url)
SELECT 'seller_ben', '[seller_ben@example.com](mailto:seller_ben@example.com)', SHA2('demo1234', 256), NULL
WHERE NOT EXISTS (
SELECT 1 FROM users WHERE username = 'seller_ben'
);

INSERT INTO users(username, email, password_hash, avatar_url)
SELECT 'buyer_chloe', '[buyer_chloe@example.com](mailto:buyer_chloe@example.com)', SHA2('demo1234', 256), NULL
WHERE NOT EXISTS (
SELECT 1 FROM users WHERE username = 'buyer_chloe'
);

-- 確保 demo 帳號密碼都是 demo1234
UPDATE users
SET password_hash = SHA2('demo1234', 256)
WHERE username IN ('seller_alice', 'seller_ben', 'buyer_chloe');

-- =====================================================
-- 2. Categories
-- =====================================================

INSERT INTO categories(name)
SELECT '教科書'
WHERE NOT EXISTS (
SELECT 1 FROM categories WHERE name = '教科書'
);

INSERT INTO categories(name)
SELECT '電子產品'
WHERE NOT EXISTS (
SELECT 1 FROM categories WHERE name = '電子產品'
);

INSERT INTO categories(name)
SELECT '生活用品'
WHERE NOT EXISTS (
SELECT 1 FROM categories WHERE name = '生活用品'
);

INSERT INTO categories(name)
SELECT '文具'
WHERE NOT EXISTS (
SELECT 1 FROM categories WHERE name = '文具'
);

INSERT INTO categories(name)
SELECT '宿舍用品'
WHERE NOT EXISTS (
SELECT 1 FROM categories WHERE name = '宿舍用品'
);

INSERT INTO categories(name)
SELECT '其他'
WHERE NOT EXISTS (
SELECT 1 FROM categories WHERE name = '其他'
);

-- =====================================================
-- 3. Active Products
-- =====================================================

INSERT INTO products(seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
(SELECT user_id FROM users WHERE username = 'seller_alice' LIMIT 1),
(SELECT category_id FROM categories WHERE name = '教科書' LIMIT 1),
'工程數學課本',
350,
'工程數學用書，內頁有少量筆記，適合期中期末複習。',
'ACTIVE',
8
WHERE NOT EXISTS (
SELECT 1 FROM products WHERE title = '工程數學課本'
);

INSERT INTO products(seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
(SELECT user_id FROM users WHERE username = 'seller_alice' LIMIT 1),
(SELECT category_id FROM categories WHERE name = '教科書' LIMIT 1),
'Java 程式設計入門',
280,
'物件導向課程可用，附部分範例程式標記。',
'ACTIVE',
12
WHERE NOT EXISTS (
SELECT 1 FROM products WHERE title = 'Java 程式設計入門'
);

INSERT INTO products(seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
(SELECT user_id FROM users WHERE username = 'seller_ben' LIMIT 1),
(SELECT category_id FROM categories WHERE name = '電子產品' LIMIT 1),
'二手工程計算機',
450,
'功能正常，適合工程數學、電路學考試使用。',
'ACTIVE',
15
WHERE NOT EXISTS (
SELECT 1 FROM products WHERE title = '二手工程計算機'
);

INSERT INTO products(seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
(SELECT user_id FROM users WHERE username = 'seller_ben' LIMIT 1),
(SELECT category_id FROM categories WHERE name = '電子產品' LIMIT 1),
'無線滑鼠',
150,
'白色無線滑鼠，接收器還在，適合筆電使用。',
'ACTIVE',
5
WHERE NOT EXISTS (
SELECT 1 FROM products WHERE title = '無線滑鼠'
);

INSERT INTO products(seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
(SELECT user_id FROM users WHERE username = 'seller_alice' LIMIT 1),
(SELECT category_id FROM categories WHERE name = '宿舍用品' LIMIT 1),
'桌上檯燈',
180,
'亮度可調，適合宿舍讀書桌使用。',
'ACTIVE',
6
WHERE NOT EXISTS (
SELECT 1 FROM products WHERE title = '桌上檯燈'
);

INSERT INTO products(seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
(SELECT user_id FROM users WHERE username = 'seller_ben' LIMIT 1),
(SELECT category_id FROM categories WHERE name = '文具' LIMIT 1),
'A4 資料夾組',
60,
'共有 5 個資料夾，適合整理講義。',
'ACTIVE',
2
WHERE NOT EXISTS (
SELECT 1 FROM products WHERE title = 'A4 資料夾組'
);

INSERT INTO products(seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
(SELECT user_id FROM users WHERE username = 'seller_alice' LIMIT 1),
(SELECT category_id FROM categories WHERE name = '宿舍用品' LIMIT 1),
'宿舍小電風扇',
220,
'小型電風扇，風量正常，夏天宿舍很好用。',
'ACTIVE',
9
WHERE NOT EXISTS (
SELECT 1 FROM products WHERE title = '宿舍小電風扇'
);

INSERT INTO products(seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
(SELECT user_id FROM users WHERE username = 'seller_ben' LIMIT 1),
(SELECT category_id FROM categories WHERE name = '生活用品' LIMIT 1),
'保溫水壺',
80,
'容量 500ml，外觀有些刮痕但可正常使用。',
'ACTIVE',
4
WHERE NOT EXISTS (
SELECT 1 FROM products WHERE title = '保溫水壺'
);

INSERT INTO products(seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
(SELECT user_id FROM users WHERE username = 'seller_alice' LIMIT 1),
(SELECT category_id FROM categories WHERE name = '電子產品' LIMIT 1),
'USB-C 充電線',
100,
'Type-C 充電線，長度約 1 公尺。',
'ACTIVE',
7
WHERE NOT EXISTS (
SELECT 1 FROM products WHERE title = 'USB-C 充電線'
);

INSERT INTO products(seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
(SELECT user_id FROM users WHERE username = 'seller_ben' LIMIT 1),
(SELECT category_id FROM categories WHERE name = '文具' LIMIT 1),
'筆記本套組',
70,
'三本筆記本，空白頁很多，適合上課筆記。',
'ACTIVE',
3
WHERE NOT EXISTS (
SELECT 1 FROM products WHERE title = '筆記本套組'
);

INSERT INTO products(seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
(SELECT user_id FROM users WHERE username = 'seller_alice' LIMIT 1),
(SELECT category_id FROM categories WHERE name = '生活用品' LIMIT 1),
'透明收納盒',
120,
'宿舍整理用收納盒，狀況良好。',
'ACTIVE',
4
WHERE NOT EXISTS (
SELECT 1 FROM products WHERE title = '透明收納盒'
);

INSERT INTO products(seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
(SELECT user_id FROM users WHERE username = 'seller_ben' LIMIT 1),
(SELECT category_id FROM categories WHERE name = '其他' LIMIT 1),
'腳踏車鎖',
90,
'二手腳踏車鎖，鑰匙有兩把。',
'ACTIVE',
1
WHERE NOT EXISTS (
SELECT 1 FROM products WHERE title = '腳踏車鎖'
);

-- =====================================================
-- 4. Sold Products
-- =====================================================

INSERT INTO products(seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
(SELECT user_id FROM users WHERE username = 'seller_alice' LIMIT 1),
(SELECT category_id FROM categories WHERE name = '電子產品' LIMIT 1),
'已售出耳機',
300,
'這是一筆已售出的商品，用來展示訂單紀錄。',
'SOLD',
11
WHERE NOT EXISTS (
SELECT 1 FROM products WHERE title = '已售出耳機'
);

INSERT INTO products(seller_id, category_id, title, price, description, status, search_hit_count)
SELECT
(SELECT user_id FROM users WHERE username = 'seller_ben' LIMIT 1),
(SELECT category_id FROM categories WHERE name = '教科書' LIMIT 1),
'已售出普通物理課本',
250,
'這是一筆已售出的課本，用來展示交易紀錄。',
'SOLD',
6
WHERE NOT EXISTS (
SELECT 1 FROM products WHERE title = '已售出普通物理課本'
);

-- =====================================================
-- 5. Demo Orders
-- buyer_chloe 買了兩筆已售出商品
-- =====================================================

-- =====================================================
-- 5. Demo Orders
-- buyer_chloe 買了兩筆已售出商品
-- =====================================================

INSERT INTO orders(buyer_id, seller_id, product_id, amount, status)
SELECT
    (SELECT user_id FROM users WHERE username = 'buyer_chloe' LIMIT 1),
    (SELECT user_id FROM users WHERE username = 'seller_alice' LIMIT 1),
    (SELECT product_id FROM products WHERE title = '已售出耳機' LIMIT 1),
    (SELECT price FROM products WHERE title = '已售出耳機' LIMIT 1),
    'COMPLETED'
WHERE NOT EXISTS (
    SELECT 1
    FROM orders
    WHERE buyer_id = (SELECT user_id FROM users WHERE username = 'buyer_chloe' LIMIT 1)
      AND product_id = (SELECT product_id FROM products WHERE title = '已售出耳機' LIMIT 1)
);

INSERT INTO orders(buyer_id, seller_id, product_id, amount, status)
SELECT
    (SELECT user_id FROM users WHERE username = 'buyer_chloe' LIMIT 1),
    (SELECT user_id FROM users WHERE username = 'seller_ben' LIMIT 1),
    (SELECT product_id FROM products WHERE title = '已售出普通物理課本' LIMIT 1),
    (SELECT price FROM products WHERE title = '已售出普通物理課本' LIMIT 1),
    'COMPLETED'
WHERE NOT EXISTS (
    SELECT 1
    FROM orders
    WHERE buyer_id = (SELECT user_id FROM users WHERE username = 'buyer_chloe' LIMIT 1)
      AND product_id = (SELECT product_id FROM products WHERE title = '已售出普通物理課本' LIMIT 1)
);
-- =====================================================
-- 6. Check Data
-- =====================================================

SELECT user_id, username, email
FROM users
WHERE username IN ('seller_alice', 'seller_ben', 'buyer_chloe');

SELECT category_id, name
FROM categories
ORDER BY category_id;

SELECT product_id, title, price, status
FROM products
ORDER BY product_id DESC;

SELECT order_id, buyer_id, product_id, status, created_at
FROM orders
ORDER BY order_id DESC;
