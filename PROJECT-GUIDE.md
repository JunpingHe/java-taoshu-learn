# 淘书网学习项目

基于Spring Boot的电商学习项目,对应《淘书网教程》完整实现。

## 项目特性

### 用户模块
- 用户注册(手机号+密码)
- 用户登录(JWT认证)
- 用户信息查询

### 商品模块
- 商品CRUD(增删改查)
- 商品列表分页
- 商品搜索
- 权限控制(只能修改自己的商品)

## 技术栈

- **框架**: Spring Boot 3.2.0
- **数据库**: H2(内存数据库,学习用)
- **ORM**: Spring Data JPA
- **认证**: JWT
- **密码加密**: BCrypt
- **工具**: Lombok

## 项目结构

```
src/main/java/org/example/
├── TaoshuApplication.java       # 启动类
├── entity/                      # 实体类
│   ├── User.java               # 用户实体
│   └── Book.java               # 商品实体
├── repository/                  # 数据访问层
│   ├── UserRepository.java
│   └── BookRepository.java
├── dto/                        # 数据传输对象
│   ├── request/               # 请求DTO
│   └── response/              # 响应DTO
├── service/                    # 业务逻辑层
│   ├── UserService.java
│   └── BookService.java
├── controller/                 # 控制器层
│   ├── UserController.java
│   └── BookController.java
├── config/                     # 配置类
│   ├── WebMvcConfig.java      # Web配置(拦截器、跨域)
│   ├── JwtAuthInterceptor.java # JWT拦截器
│   ├── JwtProperties.java     # JWT配置
│   ├── SecurityConfig.java    # 密码加密配置
│   └── SecurityFilterConfig.java # Spring Security配置
├── common/                     # 通用类
│   ├── Result.java            # 统一响应格式
│   └── BusinessException.java # 业务异常
├── exception/                  # 异常处理
│   └── GlobalExceptionHandler.java
└── util/                       # 工具类
    ├── JwtUtil.java           # JWT工具类
    └── UserContext.java       # 用户上下文
```

## 快速开始

### 1. 启动项目

```bash
# 方式1: 使用Maven
mvn spring-boot:run

# 方式2: 在IDEA中运行TaoshuApplication.main()
```

### 2. 访问地址

- 应用地址: http://localhost:8080
- H2控制台: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:taoshu-db`
  - User: `sa`
  - Password: (空)

## API文档

### 用户接口

#### 1. 用户注册
```http
POST /api/user/register
Content-Type: application/json

{
  "phone": "13800138000",
  "password": "123456",
  "nickname": "小王"
}
```

**响应示例:**
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "id": 1,
    "phone": "13800138000",
    "nickname": "小王",
    "status": 1,
    "createdAt": "2024-01-01T12:00:00"
  }
}
```

#### 2. 用户登录
```http
POST /api/user/login
Content-Type: application/json

{
  "phone": "13800138000",
  "password": "123456"
}
```

**响应示例:**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "user": {
      "id": 1,
      "phone": "13800138000",
      "nickname": "小王"
    }
  }
}
```

#### 3. 获取用户信息
```http
GET /api/user/info
Authorization: Bearer {token}
```

### 商品接口

#### 1. 创建商品(需要登录)
```http
POST /api/book
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Java编程思想",
  "author": "Bruce Eckel",
  "publisher": "机械工业出版社",
  "isbn": "9787111213826",
  "price": 88.00,
  "stock": 10,
  "description": "Java经典书籍"
}
```

#### 2. 更新商品(需要登录)
```http
PUT /api/book/1
Authorization: Bearer {token}
Content-Type: application/json

{
  "price": 78.00,
  "stock": 20
}
```

#### 3. 删除商品(需要登录)
```http
DELETE /api/book/1
Authorization: Bearer {token}
```

#### 4. 查询商品详情(公开)
```http
GET /api/book/1
```

#### 5. 分页查询商品列表(公开)
```http
GET /api/book/list?page=0&size=10
```

#### 6. 搜索商品(公开)
```http
GET /api/book/search?keyword=Java&page=0&size=10
```

#### 7. 查询我的商品(需要登录)
```http
GET /api/book/my
Authorization: Bearer {token}
```

## 分层架构说明

### 对比Django

| 层次 | Django | Spring Boot | 职责 |
|------|--------|-------------|------|
| Controller | View/APIView | @RestController | 接收请求、返回响应 |
| Service | Service/Manager | @Service | 业务逻辑、事务管理 |
| Repository | ORM Manager | JpaRepository | 数据访问 |

### 各层职责

1. **Controller层**: 
   - 接收HTTP请求
   - 参数验证(@Valid)
   - 调用Service
   - 返回响应

2. **Service层**:
   - 业务逻辑处理
   - 事务管理(@Transactional)
   - 调用Repository
   - 异常处理

3. **Repository层**:
   - 数据访问
   - 方法名即查询
   - 自动实现CRUD

## 核心功能详解

### 1. JWT认证

**流程:**
```
用户登录 → 生成Token → 返回给客户端
    ↓
客户端存储Token
    ↓
请求携带Token → 拦截器验证 → 提取用户信息 → Controller
```

**对比Session:**
- Session: 有状态,服务器存储,需要Session共享
- JWT: 无状态,客户端存储,天然支持分布式

### 2. 参数验证

使用Spring Validation注解:
- `@NotBlank`: 不能为空
- `@Size`: 长度限制
- `@Pattern`: 正则匹配
- `@Min/@Max`: 数值范围

### 3. 密码加密

使用BCrypt算法:
- 自动加盐
- 每次加密结果不同
- 防暴力破解

### 4. 全局异常处理

统一异常处理,避免每个方法写try-catch:
- 参数验证异常 → 400 Bad Request
- 业务异常 → 400 Bad Request
- 系统异常 → 500 Internal Server Error

## 测试流程

### 完整测试流程

1. **注册用户**
```bash
curl -X POST http://localhost:8080/api/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "13800138000",
    "password": "123456",
    "nickname": "小王"
  }'
```

2. **登录获取Token**
```bash
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "13800138000",
    "password": "123456"
  }'
```

3. **创建商品(使用Token)**
```bash
curl -X POST http://localhost:8080/api/book \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "title": "Java编程思想",
    "author": "Bruce Eckel",
    "price": 88.00,
    "stock": 10
  }'
```

4. **查询商品列表**
```bash
curl http://localhost:8080/api/book/list?page=0&size=10
```

5. **更新商品(需要Token)**
```bash
curl -X PUT http://localhost:8080/api/book/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "price": 78.00
  }'
```

6. **删除商品(需要Token)**
```bash
curl -X DELETE http://localhost:8080/api/book/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## 常见问题

### 1. 为什么不直接返回Entity?

返回Entity会暴露所有字段,包括敏感信息(如密码)。使用DTO可以:
- 隐藏敏感字段
- 控制返回数据结构
- 添加计算字段

### 2. 为什么需要Service层?

- 分离关注点:Controller处理请求,Service处理业务
- 代码复用:多个Controller可以调用同一个Service
- 易于测试:可以单独测试Service层

### 3. JWT Token存储在哪里?

客户端存储:
- localStorage(常用)
- sessionStorage
- HttpOnly Cookie(更安全)

### 4. 如何切换到MySQL数据库?

修改`application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/taoshu
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: your_password
  jpa:
    database-platform: org.hibernate.dialect.MySQLDialect
```

添加MySQL依赖到`pom.xml`:
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```

## 学习建议

1. **对比学习**: 对照Django的实现方式,理解Java的设计思路
2. **动手实践**: 运行项目,使用API测试工具(Postman)测试接口
3. **阅读代码**: 每个类都有详细注释,说明与Django的对比
4. **调试运行**: 在IDEA中打断点,跟踪请求流程
5. **修改实验**: 尝试添加新字段、新接口,加深理解

## 下一步

- 添加购物车功能(使用Redis)
- 实现订单系统
- 添加支付功能
- 实现文件上传
- 添加定时任务
- 编写单元测试

## 参考资料

- Spring Boot官方文档: https://spring.io/projects/spring-boot
- Spring Data JPA: https://spring.io/projects/spring-data-jpa
- JWT: https://jwt.io/
- 对应教程: `book/` 目录下的教程文档
