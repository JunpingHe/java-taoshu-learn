# 第二十五章：Docker容器化

> **小王的疑问**：项目开发完成了，怎么部署到服务器？不同环境配置不一样怎么办？如何保证开发、测试、生产环境一致？

---

## 🤔 为什么需要Docker？

### 真实场景：部署的痛苦

```
某电商平台传统部署方式：
┌─────────────────────────────────────────────────────────────┐
│  开发环境（小王的电脑）：                                   │
│  - Java 17                                                 │
│  - MySQL 8.0                                              │
│  - Redis 7.0                                              │
│  - Windows 10                                              │
│                                                             │
│  测试环境（测试服务器）：                                   │
│  - Java 11（版本不一致！）                                 │
│  - MySQL 5.7（版本不一致！）                               │
│  - Redis 6.0（版本不一致！）                               │
│  - Linux CentOS 7                                         │
│                                                             │
│  生产环境（线上服务器）：                                   │
│  - Java 8（版本又不一致！）                                │
│  - MySQL 8.0                                              │
│  - Redis 5.0（版本又不一致！）                             │
│  - Linux Ubuntu 20.04                                      │
│                                                             │
│  问题：                                                     │
│  - 开发没问题，测试报错                                  │
│  - 测试没问题，生产报错                                  │
│  - 每次部署都要重新配置环境                              │
│  - 环境不一致，排查困难                                  │
│                                                             │
│  有Docker：                                                │
│  - 一次构建，到处运行                                    │
│  - 环境完全一致                                          │
│  - 快速部署                                              │
│  - 资源隔离                                              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

**Docker的价值**：
1. **环境一致性**：开发、测试、生产完全相同
2. **快速部署**：几秒钟启动应用
3. **资源隔离**：应用间互不影响
4. **易于扩展**：快速横向扩展
5. **版本管理**：镜像版本化

---

## 🎯 本章目标

| 知识点 | 实现方式 | 难度 |
|--------|----------|------|
| Docker概念 | 容器/镜像 | ⭐⭐ |
| Dockerfile | 构建镜像 | ⭐⭐⭐ |
| Compose | 多容器编排 | ⭐⭐⭐ |
| 数据卷 | 数据持久化 | ⭐⭐⭐ |
| 网络 | 容器通信 | ⭐⭐⭐⭐ |
| 最佳实践 | 多阶段构建 | ⭐⭐⭐⭐ |

---

## 第一步：理解Docker

### 问题：Docker是什么？

```
┌─────────────────────────────────────────────────────────────┐
│                    Docker架构                               │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  [应用]                                                     │
│   ├── Java应用                                              │
│   ├── MySQL                                                 │
│   ├── Redis                                                 │
│   └── Nginx                                                 │
│      ↓                                                      │
│  [Docker引擎]                                              │
│   ├── 构建镜像                                              │
│   ├── 运行容器                                              │
│   ├── 管理网络                                              │
│   └── 管理存储                                              │
│      ↓                                                      │
│  [操作系统]                                                │
│   ├── Linux                                                 │
│   ├── Windows                                               │
│   └── macOS                                                 │
│                                                             │
└─────────────────────────────────────────────────────────────┘

核心概念：
1. 镜像（Image）：应用的模板（类）
2. 容器（Container）：镜像运行的实例（对象）
3. 仓库（Registry）：存储镜像的地方
4. Dockerfile：构建镜像的脚本
```

**类比理解**

```
传统方式：
在服务器上直接安装Java、MySQL、Redis...
→ 环境复杂，难以迁移

Docker方式：
把应用+依赖打包成镜像
→ 像集装箱一样，标准化、可移植

类比：
镜像 = 类（Class）
容器 = 对象（Object）
Dockerfile = 构造方法
```

---

## 第二步：编写Dockerfile

### 问题：如何构建Spring Boot镜像？

```dockerfile
# Dockerfile
# 多阶段构建（推荐！）

# ==================== 阶段1：构建 ====================
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# 复制pom.xml并下载依赖（利用Docker缓存）
COPY pom.xml .
RUN mvn dependency:go-offline

# 复制源码并构建
COPY src ./src
RUN mvn clean package -DskipTests

# ==================== 阶段2：运行 ====================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 从构建阶段复制jar包
COPY --from=builder /app/target/*.jar app.jar

# 设置时区
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    apk del tzdata

# 创建非root用户（安全）
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# 暴露端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1
```

**Dockerfile指令详解**

```dockerfile
# FROM：基础镜像
FROM eclipse-temurin:17-jre-alpine  # 带JRE的轻量级镜像

# WORKDIR：工作目录
WORKDIR /app  # 切换到/app目录

# COPY：复制文件
COPY app.jar /app/app.jar  # 复制jar包

# RUN：执行命令
RUN apt-get update && apt-get install -y curl  # 安装curl

# ENV：环境变量
ENV SPRING_PROFILES_ACTIVE=prod  # 设置环境变量

# EXPOSE：暴露端口
EXPOSE 8080  # 声明容器监听8080端口

# USER：切换用户
USER app  # 以app用户运行（安全）

# ENTRYPOINT/CMD：启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]  # 容器启动时执行
CMD ["--spring.profiles.active=prod"]     # 可以被覆盖
```

---

## 第三步：构建和运行

### 代码实现：操作命令

```bash
# 1. 构建镜像
docker build -t taoshu-web:1.0.0 .

# 查看镜像
docker images

# 2. 运行容器
docker run -d \
  --name taoshu-web \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=host.docker.internal \
  -e DB_PASSWORD=yourpassword \
  -v /data/taoshu/logs:/app/logs \
  taoshu-web:1.0.0

# 查看容器
docker ps

# 查看日志
docker logs -f taoshu-web

# 进入容器
docker exec -it taoshu-web sh

# 停止容器
docker stop taoshu-web

# 删除容器
docker rm taoshu-web

# 删除镜像
docker rmi taoshu-web:1.0.0
```

**参数说明**

```
-d：后台运行
--name：容器名称
-p：端口映射（主机端口:容器端口）
-e：环境变量
-v：数据卷（主机路径:容器路径）
--restart：重启策略（no/always/on-failure/unless-stopped）
```

---

## 第四步：Docker Compose

### 问题：如何管理多个容器？

```
淘书网需要运行：
1. Spring Boot应用（容器1）
2. MySQL（容器2）
3. Redis（容器3）
4. Nginx（容器4）

问题：
- 每个容器都要手动启动
- 网络配置复杂
- 启动顺序有依赖

解决：Docker Compose
```

### 代码实现：docker-compose.yml

```yaml
# docker-compose.yml
version: '3.8'

services:
  # Spring Boot应用
  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: taoshu-web
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_HOST=mysql
      - DB_PORT=3306
      - DB_NAME=taoshu
      - DB_USERNAME=root
      - DB_PASSWORD=123456
      - REDIS_HOST=redis
      - REDIS_PORT=6379
    depends_on:
      mysql:
        condition: service_healthy
      redis:
        condition: service_started
    volumes:
      - ./logs:/app/logs
    networks:
      - taoshu-network
    restart: unless-stopped

  # MySQL数据库
  mysql:
    image: mysql:8.0
    container_name: taoshu-mysql
    ports:
      - "3306:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=123456
      - MYSQL_DATABASE=taoshu
      - TZ=Asia/Shanghai
    volumes:
      - mysql-data:/var/lib/mysql
      - ./init:/docker-entrypoint-initdb.d  # 初始化脚本
    networks:
      - taoshu-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5
    restart: unless-stopped

  # Redis缓存
  redis:
    image: redis:7-alpine
    container_name: taoshu-redis
    ports:
      - "6379:6379"
    command: redis-server --appendonly yes
    volumes:
      - redis-data:/data
    networks:
      - taoshu-network
    restart: unless-stopped

  # Nginx反向代理
  nginx:
    image: nginx:alpine
    container_name: taoshu-nginx
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf:ro
      - ./nginx/conf.d:/etc/nginx/conf.d:ro
      - ./nginx/ssl:/etc/nginx/ssl:ro
      - ./static:/usr/share/nginx/html/static:ro
    depends_on:
      - app
    networks:
      - taoshu-network
    restart: unless-stopped

volumes:
  mysql-data:
  redis-data:

networks:
  taoshu-network:
    driver: bridge
```

### 操作命令

```bash
# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f app

# 重启服务
docker-compose restart app

# 停止所有服务
docker-compose down

# 停止并删除数据卷
docker-compose down -v

# 扩展服务（3个实例）
docker-compose up -d --scale app=3
```

---

## 第五步：数据持久化

### 问题：容器删除后数据怎么办？

```
问题：
容器是临时的，删除后数据丢失

解决方案：
数据卷（Volume）持久化数据
```

### 代码实现：数据卷

```yaml
# docker-compose.yml
services:
  mysql:
    image: mysql:8.0
    volumes:
      # 命名卷（推荐）
      - mysql-data:/var/lib/mysql

      # 绑定挂载（开发环境）
      - ./data/mysql:/var/lib/mysql

      # 初始化脚本
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql:ro

  app:
    volumes:
      # 日志持久化
      - ./logs:/app/logs

      # 静态资源
      - ./static:/app/static

# 定义命名卷
volumes:
  mysql-data:
    driver: local
```

**数据卷操作**

```bash
# 创建数据卷
docker volume create taoshu-mysql-data

# 查看数据卷
docker volume ls

# 查看数据卷详情
docker volume inspect taoshu-mysql-data

# 备份数据卷
docker run --rm -v taoshu-mysql-data:/data -v $(pwd):/backup alpine tar czf /backup/mysql-backup.tar.gz /data

# 恢复数据卷
docker run --rm -v taoshu-mysql-data:/data -v $(pwd):/backup alpine tar xzf /backup/mysql-backup.tar.gz -C /

# 删除数据卷
docker volume rm taoshu-mysql-data
```

---

## 第六步：多环境部署

### 问题：不同环境配置不同？

```
环境：
- 开发环境（dev）：本地开发
- 测试环境（test）：测试团队
- 预发布（staging）：生产前验证
- 生产环境（prod）：线上环境

配置差异：
- 数据库地址
- Redis地址
- 日志级别
- JVM参数
```

### 代码实现：多环境

```yaml
# docker-compose.dev.yml（开发环境）
version: '3.8'
services:
  app:
    build:
      context: .
      dockerfile: Dockerfile
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - DB_HOST=host.docker.internal
      - DB_USERNAME=root
      - DB_PASSWORD=root
    ports:
      - "8080:8080"
      - "5005:5005"  # 远程调试端口
    volumes:
      - ./src:/app/src  # 挂载源码，热更新
    command: ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar"]

# docker-compose.prod.yml（生产环境）
version: '3.8'
services:
  app:
    image: taoshu-web:latest
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_HOST=mysql
      - DB_PASSWORD=${DB_PASSWORD}  # 从环境变量读取
      - JVM_OPTS=-Xmx2g -Xms2g  # 生产JVM参数
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 2G
        reservations:
          cpus: '1'
          memory: 1G
    restart: always
```

**启动命令**

```bash
# 开发环境
docker-compose -f docker-compose.dev.yml up -d

# 生产环境
docker-compose -f docker-compose.prod.yml up -d
```

---

## ⚠️ Docker最佳实践

### 1. 镜像优化

```dockerfile
# 使用多阶段构建（减小镜像大小）
# 第一阶段：构建
FROM maven:3.9-openjdk-17-slim AS builder
# ... 构建

# 第二阶段：运行
FROM openjdk:17-jre-alpine
# 只复制必要的jar包
COPY --from=builder /app/target/*.jar app.jar

# 最终镜像大小：<200MB
```

### 2. 安全

```dockerfile
# 使用非root用户
RUN addgroup -S app && adduser -S app -G app
USER app

# 只读文件系统
RUN chmod -R 550 /app
```

### 3. 健康检查

```dockerfile
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
```

---

## 📊 传统部署 vs Docker

| 特性 | 传统部署 | Docker | 优势 |
|------|----------|--------|------|
| 环境配置 | 手动配置 | 镜像打包 | Docker一致 |
| 部署时间 | 30分钟 | 30秒 | Docker快 |
| 资源隔离 | 无 | 容器隔离 | Docker好 |
| 扩展性 | 手动扩展 | 快速扩展 | Docker好 |
| 可移植性 | 差 | 好 | Docker强 |

---

## 🎯 本章小结

| 知识点 | 为什么需要 | 怎么做 |
|--------|-----------|--------|
| **Docker** | 环境一致性 | 镜像+容器 |
| **Dockerfile** | 构建镜像 | 多阶段构建 |
| **Compose** | 多容器编排 | docker-compose.yml |
| **数据卷** | 数据持久化 | volume挂载 |
| **多环境** | 配置隔离 | 多个compose文件 |
| **最佳实践** | 小镜像+安全 | 非root用户+健康检查 |

**记忆口诀**：
- **Docker环境一**，**镜像到处运**
- **多阶段构建**，**镜像体积小**
- **Compose编排好**，**一键启动全**
- **数据持久化**，**删除不丢失**

---

## 📝 课后练习

1. **基础题**：为Spring Boot项目编写Dockerfile
2. **进阶题**：使用Docker Compose编排应用+MySQL+Redis
3. **挑战题**：
   - 实现多环境部署（dev/test/prod）
   - 配置Nginx负载均衡
   - 实现自动化部署（CI/CD）

---

**下一章预告**：数据库优化 - 如何优化SQL查询？如何配置连接池？如何进行分库分表？
