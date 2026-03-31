# 启动和调试指南

## 第一步: 配置IDEA(必须!)

### 1. 启用Lombok注解处理器

**重要**: 项目使用Lombok,必须启用注解处理器才能编译运行!

1. 打开IDEA设置: `File` → `Settings` (或按 `Ctrl` + `Alt` + `S`)
2. 导航到: `Build, Execution, Deployment` → `Compiler` → `Annotation Processors`
3. ✅ 勾选 `Enable annotation processing`
4. 点击 `Apply` 和 `OK`

### 2. 检查Lombok插件(通常已安装)

1. 打开设置: `Settings` → `Plugins`
2. 搜索 `Lombok`
3. 如果显示已安装,跳过;如果未安装,点击 `Install`
4. 安装后重启IDEA

### 3. 重新导入Maven项目

1. 右键点击项目根目录的 `pom.xml`
2. 选择 `Maven` → `Reload Project`
3. 或点击右侧Maven工具栏的刷新按钮 🔄

### 4. 清理并重新构建

1. 点击菜单 `Build` → `Rebuild Project`
2. 等待构建完成(查看底部进度条)

## 第二步: 启动项目

### 方式1: 直接运行主类

1. 打开文件: `src/main/java/org/example/TaoshuApplication.java`
2. 找到 `main` 方法
3. 点击行号旁边的绿色▶️按钮
4. 选择 `Run 'TaoshuApplication.main()'`

### 方式2: 使用Maven

1. 打开右侧Maven工具栏
2. 展开 `Plugins` → `spring-boot`
3. 双击 `spring-boot:run`

## 第三步: 验证启动成功

### 控制台应该显示:

```
=================================================
  淘书学习项目启动成功！
  访问地址: http://localhost:8083
  H2 控制台: http://localhost:8083/h2-console
=================================================
```

### 如果看到错误:

#### 错误1: 找不到符号 (getId, setId等方法)

**原因**: Lombok未生效

**解决**:
1. 确认已启用注解处理器(见第一步)
2. 重启IDEA
3. 清理并重新构建项目: `Build` → `Rebuild Project`

#### 错误2: 端口8083已被占用

**解决**:
1. 查找占用端口的进程:
   - Windows: 打开CMD,执行 `netstat -ano | findstr :8083`
   - 记录最后一列的PID
   - 执行 `taskkill /F /PID <PID>`
2. 或修改端口: 在 `application.yml` 中修改 `server.port`

#### 错误3: 依赖下载失败

**解决**:
1. 检查网络连接
2. 右键 `pom.xml` → `Maven` → `Reload Project`
3. 如果仍失败,删除本地仓库缓存: `C:\Users\你的用户名\.m2\repository`

## 第四步: 调试技巧

### 1. 打断点调试

**示例: 调试注册接口**

1. 打开 `src/main/java/org/example/controller/UserController.java`
2. 在 `register` 方法的第 `UserResponse user = userService.register(request);` 这行点击行号右侧
3. 出现红色圆点🔴表示断点已设置

4. 以调试模式启动:
   - 右键 `TaoshuApplication.java` → `Debug 'TaoshuApplication.main()'`
   - 或点击绿色虫子图标🐛

5. 发送测试请求(见下方测试文件)

6. 程序会停在断点处,你可以:
   - 查看变量值
   - 单步执行(F8)
   - 进入方法(F7)
   - 继续执行(F9)

### 2. 查看SQL日志

项目已配置显示SQL:
```yaml
spring:
  jpa:
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

控制台会显示执行的SQL语句,方便调试。

### 3. 使用IDEA内置HTTP Client测试

创建测试文件 `test-api.http`:

```http
### 变量定义
@baseUrl = http://localhost:8083
@token = 

### 1. 注册用户
POST {{baseUrl}}/api/user/register
Content-Type: application/json

{
  "phone": "13800138000",
  "password": "123456",
  "nickname": "小王"
}

### 2. 用户登录
# @name login
POST {{baseUrl}}/api/user/login
Content-Type: application/json

{
  "phone": "13800138000",
  "password": "123456"
}

### 3. 保存登录token(从上一步响应中复制)
@token = {{login.response.body.data.token}}

### 4. 获取用户信息(需要token)
GET {{baseUrl}}/api/user/info
Authorization: Bearer {{token}}

### 5. 创建商品(需要token)
POST {{baseUrl}}/api/book
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "title": "Java编程思想",
  "author": "Bruce Eckel",
  "publisher": "机械工业出版社",
  "isbn": "9787111213826",
  "price": 88.00,
  "stock": 10,
  "description": "Java经典书籍,适合初学者和进阶者"
}

### 6. 查询商品列表(公开)
GET {{baseUrl}}/api/book/list?page=0&size=10

### 7. 搜索商品(公开)
GET {{baseUrl}}/api/book/search?keyword=Java&page=0&size=10

### 8. 查询商品详情(公开)
GET {{baseUrl}}/api/book/1

### 9. 更新商品(需要token)
PUT {{baseUrl}}/api/book/1
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "price": 78.00,
  "stock": 20
}

### 10. 查询我的商品(需要token)
GET {{baseUrl}}/api/book/my
Authorization: Bearer {{token}}

### 11. 删除商品(需要token)
DELETE {{baseUrl}}/api/book/1
Authorization: Bearer {{token}}
```

**使用方法**:
1. 在IDEA中创建文件 `test-api.http`
2. 点击每个请求上方的绿色▶️按钮执行
3. 响应会显示在右侧面板

## 第五步: 查看数据库

### 访问H2控制台

1. 浏览器访问: http://localhost:8083/h2-console
2. 填写连接信息:
   - JDBC URL: `jdbc:h2:mem:taoshu-db`
   - User Name: `sa`
   - Password: (留空)
3. 点击 `Connect`

### 执行SQL查询

```sql
-- 查看所有用户
SELECT * FROM user;

-- 查看所有商品
SELECT * FROM book;

-- 查看表结构
SHOW TABLES;
```

## 常见调试场景

### 场景1: 验证密码加密

**断点位置**: `UserService.java` 的 `register` 方法

```java
// 在这里打断点
user.setPassword(passwordEncoder.encode(request.getPassword()));
```

**调试步骤**:
1. 在断点处暂停
2. 查看 `request.getPassword()` (明文密码)
3. 单步执行
4. 查看 `user.getPassword()` (加密后的密码)
5. 每次加密结果都不同(BCrypt特性)

### 场景2: 调试JWT生成

**断点位置**: `JwtUtil.java` 的 `generateToken` 方法

**调试步骤**:
1. 查看 `userId` 和 `phone` 参数
2. 单步执行查看Token生成过程
3. 复制生成的Token
4. 访问 https://jwt.io/ 解码查看内容

### 场景3: 调试JWT验证

**断点位置**: `JwtAuthInterceptor.java` 的 `preHandle` 方法

**调试步骤**:
1. 发送需要认证的请求(带Token)
2. 在拦截器中暂停
3. 查看Token提取过程
4. 查看用户信息存入Request的过程

### 场景4: 调试商品创建

**断点位置**: `BookService.java` 的 `createBook` 方法

**调试步骤**:
1. 发送创建商品请求
2. 查看 `sellerId` 是否正确(应该是当前登录用户ID)
3. 查看 `Book` 对象的各个字段
4. 单步执行到 `save` 方法
5. 查看保存后的 `book` (应该有ID了)

## 性能调优

### 1. 查看SQL执行时间

在 `application.yml` 中添加:
```yaml
logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

### 2. 启用慢查询日志

```yaml
spring:
  jpa:
    properties:
      hibernate:
        session:
          events:
            log: true
```

## 故障排查清单

- [ ] 已启用注解处理器
- [ ] 已安装Lombok插件
- [ ] 已重新导入Maven项目
- [ ] 项目已成功构建(无红色波浪线)
- [ ] 端口8083未被占用
- [ ] JDK版本正确(21或更高)
- [ ] Maven依赖全部下载成功

## 需要帮助?

如果遇到其他问题:
1. 查看控制台完整错误信息
2. 检查IDEA的Event Log(右下角)
3. 尝试: `File` → `Invalidate Caches...` → `Invalidate and Restart`
4. 检查项目SDK设置: `File` → `Project Structure` → `Project` → `SDK`

## 成功标志

看到以下输出表示启动成功:
```
Started TaoshuApplication in X.XX seconds
=================================================
  淘书学习项目启动成功！
  访问地址: http://localhost:8083
  H2 控制台: http://localhost:8083/h2-console
=================================================
```

现在可以开始测试API了! 🎉
