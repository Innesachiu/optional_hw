# Campus Marketplace (Java + Swing + MySQL)

## 專案簡介
校園二手交易系統，支援註冊、登入、瀏覽商品、搜尋、熱門關鍵字、上架、商品詳情、下訂單與商品售出。

## 資料夾架構
- `src/model`: 資料模型
- `src/dao`: SQL 與資料存取
- `src/service`: 商業邏輯
- `src/controller`: View 與 Service 橋接
- `src/view`: Swing 視窗
- `src/util`: DBConnection 與密碼工具
- `src/Main.java`: 入口

## 在 MySQL Workbench 執行 schema.sql
1. 開啟 Workbench 連線。
2. File -> Open SQL Script，選 `schema.sql`。
3. 按閃電執行全部 SQL。
4. 確認 `campus_marketplace` 與各資料表建立成功。

## 在 Eclipse 匯入專案
1. File -> Import -> Existing Projects into Workspace。
2. 選此專案資料夾。

## 加入 MySQL Connector/J
1. 右鍵專案 -> Build Path -> Configure Build Path。
2. Libraries -> Add External JARs。
3. 選 mysql-connector-j-x.x.x.jar。

## 修改 DBConnection 帳號密碼
編輯 `src/util/DBConnection.java` 的 `USER`、`PASSWORD`。

## 執行 Main.java
右鍵 `src/Main.java` -> Run As -> Java Application。

## 測試流程
註冊 -> 登入 -> 新增商品 -> 搜尋商品 -> 查看商品 -> 下訂單 -> 商品變 SOLD。
