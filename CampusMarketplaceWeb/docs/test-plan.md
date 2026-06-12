# Test Plan

本文件定義 CampusMarketplaceWeb 的手動測試流程與 curl 範例。

Base URL: `http://localhost:8080/api`

---

## A. 測試前準備

1. 初始化 DB
```bash
mysql -u root -p < CampusMarketplaceWeb/database/schema.sql
mysql -u root -p < CampusMarketplaceWeb/database/demo_data.sql
```

2. 啟動後端
```bash
javac $(find CampusMarketplaceWeb/backend/src -name '*.java')
java -cp CampusMarketplaceWeb/backend/src MainServer
```

---

## B. 手動流程

1. 註冊新帳號
2. 使用新帳號登入
3. 進入首頁查看 ACTIVE 商品
4. 搜尋商品關鍵字
5. 進入商品詳情頁
6. 新增一筆商品
7. 對另一個使用者的 ACTIVE 商品下訂單
8. 確認該商品狀態變為 SOLD
9. 進入我的訂單頁確認出現剛剛購買的商品

---

## C. curl 測試範例

### 1) Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"tester01","email":"tester01@example.com","password":"123456"}'
```

### 2) Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"tester01","password":"123456"}'
```

### 3) List products
```bash
curl "http://localhost:8080/api/products"
```

### 4) Search products
```bash
curl "http://localhost:8080/api/products/search?keyword=book"
```

### 5) Product detail
```bash
curl "http://localhost:8080/api/products/1"
```

### 6) Add product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"sellerId":1,"categoryId":1,"title":"Test Item","price":120,"description":"for test"}'
```

### 7) Create order
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"buyerId":2,"productId":1}'
```

### 8) Check my orders
```bash
curl "http://localhost:8080/api/orders/my?buyerId=2"
```

### 9) Hot keywords
```bash
curl "http://localhost:8080/api/search/hot-keywords"
```

---

## D. 預期結果摘要

- API 皆回傳 JSON。
- 成功格式包含 `success=true`。
- 失敗格式包含 `success=false` 與 `message`。
- 下訂後商品不可再次以 ACTIVE 狀態被購買。
- 我的訂單 API 可查到新建立的訂單資料。
