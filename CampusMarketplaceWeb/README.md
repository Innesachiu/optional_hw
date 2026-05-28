# CampusMarketplaceWeb

CampusMarketplaceWeb 是本專案目前的**主架構**（網站版），採用：
- Backend: Java + JDBC + MySQL + `com.sun.net.httpserver.HttpServer`
- Frontend: 原生 HTML/CSS/JavaScript（靜態頁，透過 fetch 呼叫 API）
- 架構: Router + Controller + Service + DAO

> 注意：repository root 的 `src/` 是舊版 Swing desktop app（歷史版本），**不是**本網站主架構。

---

## 1) 專案架構總覽

```text
CampusMarketplaceWeb/
├── backend/
│   ├── src/
│   │   ├── MainServer.java
│   │   ├── router/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── dao/
│   │   ├── model/
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── filter/
│   │   └── util/
│   └── config/
├── frontend/
│   ├── pages/
│   ├── js/
│   └── css/
├── database/
│   ├── schema.sql
│   ├── demo_data.sql
│   └── reset.sql
└── docs/
```

---

## 2) 如何初始化 MySQL

1. 開啟 MySQL（MySQL Workbench 或 CLI）。
2. 執行 `CampusMarketplaceWeb/database/schema.sql` 建立資料表。
3. （選用）執行 `CampusMarketplaceWeb/database/demo_data.sql` 匯入測試資料。

CLI 範例：

```bash
mysql -u root -p < CampusMarketplaceWeb/database/schema.sql
mysql -u root -p < CampusMarketplaceWeb/database/demo_data.sql
```

---

## 3) 後端設定與啟動

1. 先確認 `backend/src/util/DBConnection.java` 的連線資訊是本機可用設定。
2. 不要提交真實密碼；請只在本機設定。
3. 編譯後端：

```bash
javac $(find CampusMarketplaceWeb/backend/src -name '*.java')
```

4. 啟動後端：

```bash
java -cp CampusMarketplaceWeb/backend/src MainServer
```

預設會啟動在 `localhost:8080`。

---

## 4) API Base URL

前端呼叫後端 API 的 base URL：

```text
http://localhost:2026/api
```

---

## 5) 前端說明

`CampusMarketplaceWeb/frontend` 目前提供基本頁面與 API 串接腳本。

前端畫面、樣式與互動細節將由同學接續開發；本次重點是完成專案架構與後端 API。

---

## 6) 舊 Swing 版本說明

Repository root 的 `src/`（如 `src/view/*.java`）為舊 Swing app，僅保留歷史與參考用途。

網站主架構請以 `CampusMarketplaceWeb/` 目錄為準。
