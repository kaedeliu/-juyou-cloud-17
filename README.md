
# 巨友(Juyou Cloud) 云平台

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Cloud-2020-blue" alt="Spring Cloud">
  <img src="https://img.shields.io/badge/Spring%20Boot-2.5-green" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Nacos-2.0-orange" alt="Nacos">
  <img src="https://img.shields.io/badge/license-Apache%202.0-yellow" alt="License">
</p>

## 项目简介

巨友云是一套基于 Spring Cloud + Nacos + MyBatis-Plus 开发的微服务云平台系统，支持多租户、权限管理、代码生成、支付集成、短信服务、文件存储等企业级功能。

## 系统架构

```
┌─────────────────────────────────────────────────────────────────┐
│                        API Gateway (juyou-gateway)              │
│                              8184                               │
└─────────────────────────────────────────────────────────────────┘
                                │
        ┌───────────────────────┼───────────────────────┐
        │                       │                       │
        ▼                       ▼                       ▼
┌───────────────┐      ┌───────────────┐      ┌───────────────┐
│  Client       │      │  Manage       │      │  Common       │
│  (juyou-client)│     │  (juyou-manage)│     │  (juyou-common)│
│                │     │                │     │                │
│  - App        │      │  - Admin       │     │   - Base       │
│  - CMS        │      │  - Basics      │     │   - Gen        │
│  - User       │      │  - Common      │     │   - Pay        │
│               │      │  - UserAuth    │     │   - Redis      │
│               │      │                │     │   - Security   │
│               │      │                │     │   - Shiro      │
│               │      │                │     │   - SMS        │
│               │      │                │     │   - Storage    │
└───────────────┘      └───────────────┘      └───────────────┘
```

## 核心功能

### 🔐 权限认证
- 基于 JWT 的身份认证
- 支持 Spring Security + Shiro 双模式
- 细粒度的角色权限控制 (RBAC)
- 多租户 (Tenants) 支持

### 📦 模块介绍

| 模块 | 说明 |
|------|------|
| **juyou-gateway** | API 网关，支持路由、限流、熔断 |
| **juyou-mg-admin** | 管理后台主应用 |
| **juyou-mg-basics** | 基础系统：用户、角色、菜单、字典、配置、租户 |
| **juyou-mg-cms** | 内容管理系统：文章、分类、属性 |
| **juyou-mg-userauth** | 用户认证服务 |
| **juyou-gen** | 代码生成器，支持生成后端 + 前端代码 |
| **juyou-pay** | 支付模块：支付宝、微信支付 |
| **juyou-sms** | 短信服务：阿里云、腾讯云 |
| **juyou-storage** | 文件存储：阿里云OSS、腾讯云COS、本地存储 |
| **juyou-redis** | Redis 工具类、分布式锁 |
| **juyou-security** | WebFlux 安全模块 |
| **juyou-shiro** | Shiro 认证授权 |

### 🛠 技术栈

- **核心框架**: Spring Boot 2.5.x, Spring Cloud 2020.x
- **服务注册/配置**: Nacos 2.0.x
- **数据库**: MySQL 8.0+
- **ORM**: MyBatis-Plus
- **缓存**: Redis (Lettuce)
- **API文档**: Knife4j (Swagger)
- **权限**: Spring Security, Shiro
- **网关**: Spring Cloud Gateway
- **模板引擎**: Freemarker
- **工具**: Hutool, FastJSON, Jackson

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 5.0+
- Nacos 2.0+

### 初始化数据库

```bash
# 导入 SQL 文件
mysql -u root -p < juyou.sql
```

### 配置 Nacos

将 `nacos/` 目录下的配置文件导入到 Nacos 配置中心：

- `datasource.yml` - 数据源配置
- `redis.yml` - Redis 配置
- `common.yml` - 公共配置
- `admin.yml` - 管理端配置
- `gateway.yml` - 网关配置
- `security.yml` - 安全配置
- `gen.yml` - 代码生成配置

### 编译项目

```bash
# 编译所有模块
mvn clean install -DskipTests

# 启动顺序：
# 1. Nacos (端口: 8848)
# 2. juyou-gateway (端口: 8184)
# 3. juyou-mg-basics (基础服务)
# 4. juyou-mg-admin (管理后台)
```

## 项目结构

```
juyou-cloud/
├── juyou-client/              # 客户端模块
│   ├── juyou-cl-app/         # 应用服务
│   ├── juyou-cl-cms/         # CMS服务
│   └── juyou-cl-user/        # 用户服务
├── juyou-common/             # 公共模块
│   ├── juyou-base/           # 基础工具
│   ├── juyou-gen/            # 代码生成
│   ├── juyou-mg-cms/         # CMS管理
│   ├── juyou-pay/            # 支付模块
│   ├── juyou-redis/          # Redis工具
│   ├── juyou-security/       # 安全模块
│   ├── juyou-service/        # 公共服务
│   ├── juyou-shiro/          # Shiro认证
│   ├── juyou-sms/            # 短信服务
│   └── juyou-storage/        # 文件存储
├── juyou-gateway/            # 网关服务
├── juyou-manage/             # 管理端
│   ├── juyou-mg-admin/       # 后台应用
│   ├── juyou-mg-basics/      # 基础系统
│   ├── juyou-mg-common/      # 公共模块
│   ├── juyou-mg-common-service/
│   └── juyou-mg-userauth/    # 用户认证
├── nacos/                    # Nacos配置
└── juyou.sql                 # 数据库脚本
```

## API 文档

启动服务后，访问 Knife4j API 文档：

- 网关: `http://localhost:8184/doc.html`
- 管理端: `http://localhost:8080/doc.html`

## 核心特性

### 🔄 多租户支持
通过 `@Tenants` 注解实现数据隔离，支持租户级别的权限控制和数据过滤。

### 📝 代码生成
可视化配置数据库表，生成完整的增删改查代码，包括：
- Entity 实体类
- Mapper 接口
- Service 服务层
- Controller 控制器
- Vue 前端页面

### 💳 支付集成
- 支付宝支付 (App、H5)
- 微信支付 (小程序、App、H5)

### 📱 短信服务
- 阿里云短信
- 腾讯云短信
- 验证码自动生成与验证

### ☁️ 文件存储
- 阿里云 OSS
- 腾讯云 COS
- 本地存储
- 支持签名 URL

### 🔒 安全特性
- JWT Token 认证
- 接口权限细粒度控制
- 数据权限控制
- SQL 注入防护
- 密码加密存储 (PBE)

## 许可证

本项目基于 Apache License 2.0 开源协议。

---

<p align="center">欢迎使用巨友云平台！</p>