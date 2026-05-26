# Campus Marketplace (Java + Swing + MySQL)

## 專案簡介
這是一個校園二手交易系統，使用 **Java + Swing + MySQL + JDBC**，採用 **MVC + Service + DAO** 架構。

功能包含：
- 註冊 / 登入
- 瀏覽 ACTIVE 商品
- 搜尋商品（同時記錄 `search_logs` 並增加 `products.search_hit_count`）
- 顯示最近 7 天熱門關鍵字
- 上架商品
- 查看商品詳情
- 下訂單後商品狀態改為 SOLD

## 資料夾架構
```text
src/
├── model/
├── dao/
├── service/
├── controller/
├── view/
├── util/
└── Main.java
```

## 一、在 MySQL Workbench 建立資料庫（schema.sql）
1. 開啟 MySQL Workbench，連到本機 MySQL。
2. 點選 `File -> Open SQL Script...`，選擇專案根目錄的 `schema.sql`。
3. 按執行（⚡）執行整份 SQL。
4. 確認 `campus_marketplace` 與資料表建立成功：
   - users
   - categories
   - products
   - product_images
   - orders
   - search_logs

## 二、匯入 demo 資料（demo_data.sql）
1. 在 Workbench 再次 `File -> Open SQL Script...`，選 `demo_data.sql`。
2. 按執行（⚡）匯入 demo users / products。
3. 匯入後可直接使用 demo 帳號測試登入與下單流程。

### Demo 帳號與密碼
- 帳號：`alice_demo` / `bob_demo` / `cindy_demo`
- 密碼（明文）：`demo1234`

> 程式會用 `PasswordUtil` 的 SHA-256 做比對，資料庫中存的是 hash，不是明文密碼。

## 三、在 Eclipse 匯入專案
1. Eclipse -> `File -> Import...`
2. 選 `General -> Existing Projects into Workspace`
3. 選擇此專案資料夾後完成匯入。

## 四、加入 MySQL Connector/J jar
1. 右鍵專案 -> `Build Path -> Configure Build Path`
2. 到 `Libraries` 頁籤
3. 點 `Add External JARs...`
4. 選擇你的 `mysql-connector-j-8.x.x.jar`

## 五、修改 DBConnection.java
請打開 `src/util/DBConnection.java`：
- `USER` 預設為 `root`
- `PASSWORD` 預設為 `請在本機自行填入`

請改成你本機 MySQL 的密碼後再執行。

## 六、執行程式
1. 在 Eclipse 右鍵 `src/Main.java`
2. 選 `Run As -> Java Application`
3. 出現登入畫面後，即可開始操作。

## 七、完整測試流程（建議）
1. 註冊新帳號
2. 登入
3. 新增商品
4. 搜尋商品
5. 查看商品詳情
6. 下訂單
7. 回到商品列表確認該商品變成 `SOLD`

