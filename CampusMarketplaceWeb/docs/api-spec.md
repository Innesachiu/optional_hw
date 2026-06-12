# API Specification

Base URL: `http://localhost:8080/api`

## Common Response Format

### Success (data)
```json
{"success": true, "data": ...}
```

### Success (message)
```json
{"success": true, "message": "..."}
```

### Error
```json
{"success": false, "message": "..."}
```

---

## 1) Register
- **Method**: `POST`
- **Path**: `/api/auth/register`
- **Request**:
```json
{"username":"alice","email":"alice@example.com","password":"123456"}
```
- **Response**:
```json
{"success":true,"message":"register success"}
```

## 2) Login
- **Method**: `POST`
- **Path**: `/api/auth/login`
- **Request**:
```json
{"username":"alice","password":"123456"}
```
- **Response**:
```json
{"success":true,"message":"login success"}
```

## 3) List Active Products
- **Method**: `GET`
- **Path**: `/api/products`
- **Response**:
```json
{"success":true,"data":[{"productId":1,"title":"Book","price":100,"status":"ACTIVE"}]}
```

## 4) Search Products
- **Method**: `GET`
- **Path**: `/api/products/search?keyword=xxx`
- **Response**:
```json
{"success":true,"data":[{"productId":1,"title":"Java Book","price":200,"status":"ACTIVE"}]}
```

## 5) Product Detail
- **Method**: `GET`
- **Path**: `/api/products/{id}`
- **Response**:
```json
{"success":true,"data":{"productId":1,"title":"Java Book","price":200,"status":"ACTIVE","sellerId":2,"categoryId":1,"description":"...","searchHitCount":3}}
```

## 6) Add Product
- **Method**: `POST`
- **Path**: `/api/products`
- **Request**:
```json
{"sellerId":2,"categoryId":1,"title":"Notebook","price":80,"description":"Good"}
```
- **Response**:
```json
{"success":true,"message":"product added"}
```

## 7) Categories
- **Method**: `GET`
- **Path**: `/api/categories`
- **Response**:
```json
{"success":true,"data":[{"categoryId":1,"name":"Books"}]}
```

## 8) Create Order
- **Method**: `POST`
- **Path**: `/api/orders`
- **Request**:
```json
{"buyerId":3,"productId":1}
```
- **Response**:
```json
{"success":true,"message":"order created"}
```

## 9) My Orders
- **Method**: `GET`
- **Path**: `/api/orders/my?buyerId=xxx`
- **Response**:
```json
{"success":true,"data":[{"orderId":10,"productId":1,"productTitle":"Java Book","price":200,"sellerId":2,"buyerId":3,"status":"COMPLETED","createdAt":"2026-05-27 10:00:00.0"}]}
```

## 10) Hot Keywords
- **Method**: `GET`
- **Path**: `/api/search/hot-keywords`
- **Response**:
```json
{"success":true,"data":["java","book"]}
```

---

## Error Cases

所有 API 錯誤回傳格式：
```json
{"success":false,"message":"error message"}
```

常見狀況：
- 驗證錯誤（參數缺漏、格式錯）
- 業務規則錯誤（例如購買非 ACTIVE 商品）
- 找不到資料
- 路由不存在 (`404`)
- 伺服器內部錯誤 (`500`)
