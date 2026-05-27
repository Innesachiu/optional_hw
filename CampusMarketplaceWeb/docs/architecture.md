# Architecture

## Layered Flow

系統請求流程如下：

`frontend` → `backend MainServer` → `router` → `controller` → `service` → `dao` → `MySQL`

- Frontend 使用 `fetch` 呼叫 HTTP API。
- `MainServer` 啟動 HTTP server 並將 `/api/*` 交給 router。
- Router 根據 method/path 分派到對應 controller。
- Controller 負責 request/response 轉換與錯誤回應格式。
- Service 實作商業邏輯與規則判斷。
- DAO 專責 SQL 與資料存取。

---

## Package Responsibilities

### `backend/src/router`
- API 路由分派。
- 將 URL + HTTP method 導向正確 controller。

### `backend/src/controller`
- 接收 DTO / query parameter。
- 呼叫 service。
- 回傳統一 JSON 格式。

### `backend/src/service`
- 實作商業規則（例如：不可購買自己的商品、商品狀態檢查）。
- 不直接寫 SQL。

### `backend/src/dao`
- 負責所有 SQL 查詢與更新。
- 使用 JDBC + `PreparedStatement`。

### `backend/src/model`
- 對應資料表的 domain model。

### `backend/src/dto`
- API request/response 物件。

### `backend/src/util`
- DB connection、JSON、HTTP/request/response helper 等共用工具。

### `backend/src/exception`
- 例外層級封裝（validation、not found、database…）。

### `backend/src/filter`
- 例如 CORS 處理。

---

## Why Controller must not call DAO directly

Controller 直接呼叫 DAO 會造成：
1. **商業邏輯分散**：規則容易散落在多個 endpoint。
2. **耦合過高**：API 層與資料層綁死，不利維護與測試。
3. **重用性降低**：同一規則無法由不同 controller 重用。

因此本專案採用：
- Controller → Service（流程與規則入口）
- Service → DAO（資料讀寫）

---

## SQL Placement Rule

- SQL 只允許放在 DAO。
- 所有 SQL 使用 `PreparedStatement`，避免字串拼接造成 SQL injection 風險。
- Service/Controller 不可直接操作 SQL。
