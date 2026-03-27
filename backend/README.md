# 校园二手交易平台 - 后端

Spring Boot 3.2 + JPA + H2（内存数据库）。

## 环境要求

- **JDK 17**（必须）
- 本目录已包含 **Maven Wrapper**（`mvnw.cmd`），无需单独安装 Maven。

## 1. 安装并配置 JDK 17

1. 下载 JDK 17（任选其一）：
   - [Oracle JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
   - [Eclipse Temurin 17](https://adoptium.net/zh-CN/temurin/releases/?version=17)
2. 安装后，设置环境变量 **JAVA_HOME**（指向 JDK 安装目录，例如 `C:\Program Files\Java\jdk-17`）。
3. 在**新的**命令行窗口执行 `java -version`，确认显示 17。

## 2. 运行后端

在项目里的 **backend** 目录打开终端，执行：

```bash
cd backend
.\mvnw.cmd spring-boot:run
```

首次运行会下载 Maven 和依赖，可能需要几分钟。看到日志里出现 **Started SecondhandApplication** 即表示启动成功。

- 服务地址：`http://localhost:8080`
- 订单接口：`POST /api/orders`、`GET /api/orders/user?userId=xxx`

## 前端对接

前端默认请求 `http://localhost:8080/api`（见 `node/.env` 或 `node/src/api/order.js` 中的 `VITE_API_BASE`）。  
先启动本后端，再启动前端，即可完成下单与订单列表联调。
