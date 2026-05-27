# Database Design

資料庫名稱：`campus_marketplace`

## Tables Overview

### 1) `users`
- 使用者主檔。
- 主要欄位：
  - `user_id` (PK)
  - `username` (UNIQUE)
  - `email` (UNIQUE)
  - `password_hash`
  - `avatar_url`
  - `created_at`

### 2) `categories`
- 商品分類主檔。
- 主要欄位：
  - `category_id` (PK)
  - `name` (UNIQUE)

### 3) `products`
- 商品主檔。
- 主要欄位：
  - `product_id` (PK)
  - `seller_id` (FK → `users.user_id`)
  - `category_id` (FK → `categories.category_id`)
  - `title`
  - `price`
  - `description`
  - `status` (`ACTIVE` / `SOLD` / `DELETED`)
  - `search_hit_count`
  - `created_at`

### 4) `product_images`
- 商品圖片表（一商品可多圖）。
- 主要欄位：
  - `image_id` (PK)
  - `product_id` (FK → `products.product_id`)
  - `image_url`
  - `sort_order`

### 5) `orders`
- 訂單主檔。
- 主要欄位：
  - `order_id` (PK)
  - `buyer_id` (FK → `users.user_id`)
  - `seller_id` (FK → `users.user_id`)
  - `product_id` (FK → `products.product_id`)
  - `amount`
  - `status` (`PENDING` / `COMPLETED` / `CANCELED`)
  - `created_at`

### 6) `search_logs`
- 搜尋關鍵字紀錄。
- 主要欄位：
  - `search_id` (PK)
  - `user_id` (FK → `users.user_id`, nullable)
  - `keyword`
  - `created_at`

---

## Main Relationships

- `users (1) -> (N) products` 透過 `products.seller_id`
- `categories (1) -> (N) products` 透過 `products.category_id`
- `products (1) -> (N) product_images` 透過 `product_images.product_id`
- `users (1) -> (N) orders`（buyer）透過 `orders.buyer_id`
- `users (1) -> (N) orders`（seller）透過 `orders.seller_id`
- `products (1) -> (N) orders` 透過 `orders.product_id`
- `users (1) -> (N) search_logs` 透過 `search_logs.user_id`

---

## Business Notes

- 建立訂單後，對應商品應更新為 `SOLD`。
- 商品搜尋會寫入 `search_logs`，並可統計近 7 天熱門關鍵字。
