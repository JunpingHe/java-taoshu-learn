# 🚀 快速启动清单

## ⚡ 立即启动(3步完成)

### 1️⃣ 配置IDEA(第一次运行必须做!)

```
File → Settings (Ctrl+Alt+S)
→ Build, Execution, Deployment
→ Compiler
→ Annotation Processors
→ ✅ Enable annotation processing
→ Apply & OK
```

### 2️⃣ 重新加载项目

```
右键 pom.xml
→ Maven
→ Reload Project
```

### 3️⃣ 启动项目

```
打开 TaoshuApplication.java
点击 main 方法旁边的绿色▶️按钮
选择 Run 'TaoshuApplication.main()'
```

## ✅ 启动成功标志

控制台显示:
```
=================================================
  淘书学习项目启动成功！
  访问地址: http://localhost:8083
  H2 控制台: http://localhost:8083/h2-console
=================================================
```

## 🧪 快速测试

### 使用 test-api.http 文件

1. 在IDEA中打开 `test-api.http`
2. 点击第一个请求(注册用户)旁边的绿色▶️按钮
3. 查看响应结果

### 测试流程

1. **注册用户** → 执行第1个请求
2. **用户登录** → 执行第2个请求,复制返回的token
3. **创建商品** → 执行第4个请求,替换`YOUR_TOKEN_HERE`
4. **查询列表** → 执行第6个请求

## 🔧 如果遇到问题

### 问题1: 找不到符号 (getId, setId等)

**解决**: 没有启用注解处理器,请按上面第1步操作

### 问题2: 端口占用

**解决**: 
```bash
# Windows CMD
netstat -ano | findstr :8083
taskkill /F /PID <PID>
```

### 问题3: 依赖下载失败

**解决**: 
```
右键 pom.xml → Maven → Reload Project
```

## 📚 详细文档

- **启动调试**: `START-DEBUG-GUIDE.md` (调试技巧、断点设置等)
- **项目说明**: `PROJECT-GUIDE.md` (API文档、架构说明等)
- **IDEA配置**: `IDEA-SETUP-GUIDE.md` (详细配置步骤)

## 🎯 访问地址

- **应用**: http://localhost:8083
- **H2数据库**: http://localhost:8083/h2-console
  - JDBC URL: `jdbc:h2:mem:taoshu-db`
  - User: `sa`
  - Password: (空)

## 🐛 调试技巧

### 打断点

1. 打开 `UserController.java`
2. 在第 `UserResponse user = userService.register(request);` 点击行号
3. 出现红色圆点🔴
4. 右键 `TaoshuApplication.java` → `Debug`
5. 发送请求,程序会在断点暂停

### 查看变量

- 鼠标悬停在变量上查看值
- 底部Debug面板查看所有变量
- F8单步执行,F7进入方法,F9继续

## ✨ 成功启动后

1. **测试注册**: 执行 `test-api.http` 第1个请求
2. **测试登录**: 执行第2个请求,获取token
3. **测试商品**: 使用token测试商品CRUD
4. **查看数据库**: 访问H2控制台查看数据

## 🎉 开始学习吧!

项目已经完全准备好,现在可以:
- 对照教程 `book/` 目录学习
- 测试每个API接口
- 打断点调试理解流程
- 尝试修改代码添加功能

祝你学习愉快! 🚀
