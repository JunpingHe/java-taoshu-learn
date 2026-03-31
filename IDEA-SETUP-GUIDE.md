# IDEA项目配置指南

## 问题说明

当前项目使用Lombok简化代码,需要在IDEA中正确配置才能编译运行。

## 解决方案

### 方法1: 在IDEA中启用Lombok注解处理器(推荐)

1. **打开IDEA设置**
   - Windows: `File` → `Settings` (或 `Ctrl` + `Alt` + `S`)
   - Mac: `IntelliJ IDEA` → `Preferences`

2. **启用注解处理器**
   - 导航到: `Build, Execution, Deployment` → `Compiler` → `Annotation Processors`
   - 勾选 `Enable annotation processing`
   - 点击 `Apply` 和 `OK`

3. **安装Lombok插件**(IDEA默认已安装,检查一下)
   - 导航到: `Settings` → `Plugins`
   - 搜索 `Lombok`
   - 如果未安装,点击 `Install`
   - 重启IDEA

4. **重新导入Maven项目**
   - 右键点击 `pom.xml`
   - 选择 `Maven` → `Reload Project`
   - 或点击右侧Maven工具栏的刷新按钮

5. **清理并重新构建**
   - 点击 `Build` → `Rebuild Project`
   - 或使用快捷键 `Ctrl` + `F9`

### 方法2: 使用IDEA运行项目

1. **找到主类**
   - 打开 `src/main/java/org/example/TaoshuApplication.java`

2. **运行项目**
   - 右键点击类文件
   - 选择 `Run 'TaoshuApplication.main()'`
   - 或点击类名或main方法旁边的绿色运行按钮

3. **访问应用**
   - 应用地址: http://localhost:8080
   - H2控制台: http://localhost:8080/h2-console

## 常见问题解决

### 问题1: Lombok生成的方法找不到

**症状**: 编译错误,提示找不到 `getId()`, `setId()` 等方法

**解决**:
1. 确认已启用注解处理器(见上方步骤2)
2. 确认已安装Lombok插件(见上方步骤3)
3. 重启IDEA
4. 清理并重新构建项目

### 问题2: Maven依赖下载失败

**症状**: pom.xml中依赖标红

**解决**:
1. 检查网络连接
2. 右键 `pom.xml` → `Maven` → `Reload Project`
3. 如果仍失败,删除本地仓库中的相关依赖,重新下载:
   - Windows: `C:\Users\用户名\.m2\repository`
   - Mac/Linux: `~/.m2/repository`

### 问题3: 端口被占用

**症状**: 启动时报错 `Port 8080 already in use`

**解决**:
修改 `application.yml` 中的端口:
```yaml
server:
  port: 8081  # 改为其他端口
```

### 问题4: Spring Security相关问题

**症状**: 自动跳转到登录页面

**说明**: 项目已禁用Spring Security的默认登录页,使用自定义JWT认证

## 验证项目配置

### 1. 检查Lombok是否生效

在任意实体类中,应该能看到Lombok生成的方法:
```java
@Data  // Lombok注解
public class User {
    private Long id;
    private String phone;
    // IDEA会自动识别Lombok生成的方法
    // 输入 user. 会自动提示 getId(), setId() 等方法
}
```

### 2. 检查Maven依赖

在IDEA右侧的Maven工具栏中:
- 展开 `Dependencies`
- 应该能看到所有依赖,包括 `lombok`

### 3. 检查项目结构

在 `Project Structure` (Ctrl + Alt + Shift + S) 中:
- `Project` → `SDK`: 应该选择 JDK 21
- `Modules` → `Sources` → `Language level`: 应该选择 21

## 测试项目

### 1. 启动项目

运行 `TaoshuApplication.main()`,应该看到:
```
=================================================
  淘书学习项目启动成功！
  访问地址: http://localhost:8080
  H2 控制台: http://localhost:8080/h2-console
=================================================
```

### 2. 测试API

使用IDEA内置的HTTP Client或Postman:

**创建http-test.http文件**:
```http
### 1. 注册用户
POST http://localhost:8080/api/user/register
Content-Type: application/json

{
  "phone": "13800138000",
  "password": "123456",
  "nickname": "小王"
}

### 2. 用户登录
POST http://localhost:8080/api/user/login
Content-Type: application/json

{
  "phone": "13800138000",
  "password": "123456"
}

### 3. 获取用户信息(需要先登录获取token)
GET http://localhost:8080/api/user/info
Authorization: Bearer YOUR_TOKEN_HERE

### 4. 创建商品
POST http://localhost:8080/api/book
Authorization: Bearer YOUR_TOKEN_HERE
Content-Type: application/json

{
  "title": "Java编程思想",
  "author": "Bruce Eckel",
  "price": 88.00,
  "stock": 10
}

### 5. 查询商品列表
GET http://localhost:8080/api/book/list?page=0&size=10
```

### 3. 查看数据库

访问 H2 控制台: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:taoshu-db`
- User: `sa`
- Password: (空)

可以执行SQL查看数据:
```sql
SELECT * FROM user;
SELECT * FROM book;
```

## 项目学习路径

1. **理解分层架构**
   - Entity: 数据实体
   - Repository: 数据访问
   - Service: 业务逻辑
   - Controller: 接口控制器
   - DTO: 数据传输对象

2. **对比Django学习**
   - 每个类都有注释说明与Django的对比
   - 理解Java和Python的设计差异

3. **调试技巧**
   - 在Controller方法打断点
   - 使用IDEA的HTTP Client测试
   - 查看控制台日志

## 需要帮助?

如果遇到其他问题:
1. 检查IDEA版本(建议使用最新版)
2. 检查JDK版本(需要JDK 21)
3. 清理IDEA缓存: `File` → `Invalidate Caches...`
4. 重新导入项目: 删除 `.idea` 文件夹,重新打开项目

## 下一步

项目配置成功后:
1. 阅读 `PROJECT-GUIDE.md` 了解项目功能
2. 参考 `book/` 目录下的教程深入学习
3. 尝试添加新功能(如购物车、订单等)
