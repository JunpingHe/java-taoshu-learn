# 第二部分：项目启动与用户模块（Day 4-7）

## 第4章：创建第一个Spring Boot项目

## 🤔 为什么需要Spring Boot这样的框架？

> **小王的疑惑**：Django一个命令`startproject`就搞定了，Spring Boot为什么要这么复杂？

### 从"配置地狱"说起

**场景：小王刚入行时的Java Web开发（2015年前）**

```xml
<!-- 那个时代的Spring配置（仅供参考，已过时） -->
<!-- web.xml -->
<servlet>
    <servlet-name>dispatcher</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/spring-mvc.xml</param-value>
    </init-param>
</servlet>
<servlet-mapping>
    <servlet-name>dispatcher</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>

<!-- applicationContext.xml -->
<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource">
    <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
    <property name="url" value="jdbc:mysql://localhost:3306/mydb"/>
    <property name="username" value="root"/>
    <property name="password" value="password"/>
</bean>

<!-- spring-mvc.xml -->
<mvc:annotation-driven/>
<context:component-scan base-package="com.example"/>
```

**小王崩溃了**：
- 配置文件：10个+
- 依赖jar包：30个+
- 配置顺序：不能错
- 某个版本不兼容：整个项目起不来

**对比Django**：

```python
# Django：一条命令搞定
$ django-admin startproject mysite
$ cd mysite
$ python manage.py runserver

# 搞定！
```

---

### 🎯 Spring Boot的诞生：解决"配置地狱"

**Spring Boot的核心理念**：

```
约定 > 配置

传统Spring：什么都配置
→ 灵活但复杂
→ 配置错误难排查

Spring Boot：约定好，按约定来
→ 开箱即用
→ 少配置，多约定
```

**对比**：

| 方面 | 传统Spring | Spring Boot | Django |
|------|-----------|-----------|--------|
| **项目创建** | 手动配置 | 自动生成 | 命令生成 |
| **依赖管理** | 手动添加jar包 | 自动配置 | pip install |
| **Web服务器** | 手动配置Tomcat | 内置Tomcat | 内置开发服务器 |
| **配置文件** | 多个XML文件 | 一个application.yml | 一个settings.py |

---

### 4.1 理解Spring Boot的"自动配置"

#### 🤔 自动配置是怎么工作的？

**生活类比**：

```
传统Spring = 组装电脑
→ 选主板、选CPU、选显卡
→ 自己组装
→ 可能不兼容
→ 需要专业知识

Spring Boot = 买笔记本
→ 厂家配好的
→ 开箱即用
→ 有标准配置
→ 也能自己升级
```

---

**技术原理**：

```java
// Spring Boot的"魔法"背后
@SpringBootApplication  // 这个注解做了什么？
= @SpringBootConfiguration      // 这是一个配置类
+ @ComponentScan               // 自动扫描组件
+ @EnableAutoConfiguration     // 自动配置（核心！）

// @EnableAutoConfiguration 做了什么？
1. 扫描classpath上的jar包
2. 根据条件判断是否需要配置
3. 自动创建默认的Bean

// 例如：发现MySQL驱动在classpath
→ 自动配置DataSource
→ 自动配置JPA
→ 自动配置事务管理器
```

**"条件注解"机制**：

```java
// 举个例子：Redis自动配置
@Configuration
@ConditionalOnClass(RedisTemplate.class)  // 只有这个类存在才配置
@ConditionalOnMissingBean(RedisTemplate.class)  // 没有自定义Bean才配置
class RedisAutoConfiguration {
    @Bean
    public RedisTemplate redisTemplate() {
        // 自动创建RedisTemplate
    }
}

// 结果：
// 添加spring-boot-starter-data-redis依赖
// → 自动配置RedisTemplate
// → 不需要你写任何配置代码！
```

---

### 4.2 创建第一个Spring Boot项目

#### 🎯 方式对比

**Django**：
```bash
$ pip install django
$ django-admin startproject mysite
$ cd mysite
$ python manage.py runserver
# 完成！
```

**Spring Boot（三种方式）**：

```
方式1：网站生成器（最简单）
├─ 访问 https://start.spring.io/
├─ 选择配置
├─ 点击Generate
└─ 下载解压

方式2：IDEA创建（最方便）
├─ New Project
├─ 选择Spring Initializr
├─ 选择依赖
└─ Create

方式3：Maven命令（最灵活）
└─ mvn archetype:generate ...
```

---

#### 📝 详细步骤：IDEA创建（推荐）

**Step 1: 新建项目**

```
1. 打开IDEA
2. File → New → Project
3. 选择：Spring Initializr
4. 点击Next
```

**Step 2: 配置项目**

```
Group: com.taoshu       # 类似Python的包名
Artifact: taoshu-web      # 项目名
Name: taoshu-web         # 显示名称
Location: D:\workspace\taoshu-web
Package: com.taoshu      # 默认包名
```

**为什么需要这些信息？**

| 字段 | 作用 | Python对应 |
|------|------|----------|
| Group | 公司/组织域名倒写 | package名 |
| Artifact | 项目名 | 项目文件夹名 |
| Package | 类的包路径 | Python的目录结构 |

**类比**：
```
Python:
taoshu_web/
├── __init__.py
└── views.py  # from taoshu_web.views import xxx

Java:
com.taoshu.web/
└── UserController.java  # import com.taoshu.web.UserController
```

---

**Step 3: 选择依赖**

```
必选：
☑ Spring Web              # Web开发（必选）
☑ Spring Data JPA         # 数据库访问
☑ MySQL Driver            # MySQL驱动
☑ Lombok                 # 简化代码（强烈推荐）

可选：
☑ Spring Boot DevTools    # 热重载（开发用）
☑ Validation             # 参数验证
☑ SpringDoc OpenAPI      # API文档
```

**为什么需要选择依赖？**

```
Django: pip install django
→ Django自带所有功能

Spring Boot: 按需选择
→ 你需要Web？加Web依赖
→ 你需要数据库？加JPA依赖
→ 你需要Redis？加Redis依赖
→ 轻量级，只加载需要的
```

---

**Step 4: 等待项目生成**

```
IDEA会自动：
1. 下载parent pom.xml
2. 下载依赖jar包（第一次较慢）
3. 创建项目结构
4. 生成启动类

完成后看到：
taoshu-web/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/taoshu/
│   │   │   └── TaoshuWebApplication.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
```

---

### 4.3 理解项目结构

#### 🤔 为什么Java的目录结构这么深？

**小王的疑惑**：

```
Django:
mysite/
├── models.py
├── views.py
└── urls.py

Java:
com/
└── taoshu/
    └── web/
        ├── controller/
        ├── service/
        ├── repository/
        └── entity/

为什么不能简单点？
```

---

**原因1：Java的包机制**

```
Python:
用目录+__init__.py表示包
相对导入：from .models import User

Java:
用.分隔表示包
com.taoshu.web.UserController
→ 完全限定名
→ 避免命名冲突
```

**原因2：分层架构（重要！）**

```
Django也可以分层，但不是强制的：
myapp/
├── models.py      # 数据层
├── views.py       # 视图层
├── forms.py       # 表单层
└── services.py    # 业务层（可选，很多人不写）

Spring Boot强制分层：
controller/  # 控制层（接收请求）
    ↓
service/     # 业务层（处理逻辑）
    ↓
repository/  # 数据层（访问数据库）
    ↓
entity/      # 实体层（数据模型）

好处：
→ 职责清晰
→ 易于维护
→ 便于测试
→ 团队协作友好
```

**实际对比**：

```python
# Django：容易写成"面条代码"
def create_order(request):
    # 直接在视图里写所有逻辑
    user = request.user
    cart = get_cart(user.id)
    order = Order.objects.create(user=user)
    for item in cart.items:
        OrderItem.objects.create(...)
        book = Book.objects.get(id=item.book_id)
        book.stock -= item.quantity
        book.save()
    # ...100行代码都在这里
```

```java
// Spring Boot：强制分层
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public Result<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        // 控制器只负责：接收请求、返回响应
        return Result.success(orderService.createOrder(request));
        // 业务逻辑在Service层
    }
}

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        // Service层负责：业务逻辑、事务控制
        // 调用Repository层完成数据操作
    }
}
```

---

### 4.4 运行第一个Spring Boot应用

#### 🚀 三种运行方式

**方式1：IDEA中运行（最常用）**

```
1. 找到启动类：TaoshuWebApplication.java
2. 右键 → Run 'TaoshuWebApplication'
3. 看到控制台输出：
   .   ____          _            __ _ _
  /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
 ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
  \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
   '  |____| .__|_| |_|_| |_\__, | / / / /
  =========|_|==============|___/=/_/_/_/
  :: Spring Boot ::                (v3.2.0)

2024-02-03 10:30:00.123  INFO 12345 --- [main] c.t.TaoshuWebApplication : Started TaoshuWebApplication in 2.5 seconds
```

**方式2：Maven命令**

```bash
$ mvn spring-boot:run

# 看到同样的启动日志
```

**方式3：打包后运行**

```bash
$ mvn clean package
$ java -jar target/taoshu-web-0.0.1-SNAPSHOT.jar

# 优点：可以部署到服务器
# 缺点：每次修改都要重新打包
```

---

### 4.5 验证：Hello World

#### 📝 创建第一个Controller

```java
// HelloController.java
@RestController  # 声明：这是一个REST控制器
@RequestMapping("/api")   # 所有路由以/api开头
public class HelloController {

    @GetMapping("/hello")  # GET /api/hello
    public Map<String, Object> hello() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Hello, Spring Boot!");
        result.put("project", "淘书网");
        result.put("status", "running");
        return result;
    }
}
```

#### 🧪 测试接口

```
浏览器访问：
http://localhost:8080/api/hello

返回：
{
  "message": "Hello, Spring Boot!",
  "project": "淘书网",
  "status": "running"
}
```

#### 🎯 对比Django

```python
# Django: urls.py + views.py
# urls.py
from django.urls import path
from . import views

urlpatterns = [
    path('api/hello/', views.hello),
]

# views.py
from django.http import JsonResponse

def hello(request):
    return JsonResponse({
        'message': 'Hello, Django!',
        'project': '淘书网',
        'status': 'running'
    })

# Spring Boot更简洁：
# 只需要一个类，一个注解，一个方法
```

---

### 4.6 热重载配置

#### 🤔 为什么需要热重载？

**场景：小王开发时频繁修改代码**

```
没有热重载：
1. 修改代码
2. Ctrl+C 停止应用
3. 重新运行应用
4. 等待启动（30秒）
5. 浏览器刷新
→ 改个小bug要等1分钟！

有热重载：
1. 修改代码
2. Ctrl+F9 构建
3. 浏览器刷新（5秒）
→ 开发效率提升6倍！
```

---

#### ⚙️ 配置步骤

**Step 1: 确认DevTools依赖**

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

**Step 2: IDEA设置**

```
1. File → Settings
2. Build, Execution, Deployment → Compiler
3. ✅ Build project automatically
4. Apply → OK

5. Ctrl+Shift+Alt+/
6. Registry...
7. ✅ compiler.automake.allow.when.app.running
8. Close
```

**Step 3: 使用热重载**

```
1. 启动应用（只用启动一次）
2. 修改代码（比如改返回值）
3. Ctrl+F9（Build Project）
4. 浏览器刷新 → 已生效！
```

**对比Django**：

| 特性 | Django | Spring Boot + DevTools |
|------|--------|---------------------|
| **检测代码变化** | 自动 | 需要手动Build |
| **重启速度** | 较慢（Python解释器） | 较快（只加载变化的类） |
| **配置难度** | 无需配置 | 需要IDEA配置 |

---

### 4.7 本章小结

#### 🎯 核心概念对比

| 概念 | Django | Spring Boot | 关键差异 |
|------|--------|-------------|----------|
| **项目创建** | `django-admin startproject` | Spring Initializr | Spring需更多配置 |
| **项目结构** | 相对自由 | 强制分层 | Spring更规范 |
| **路由配置** | `urls.py` | `@RequestMapping` | Spring用注解 |
| **启动方式** | `python manage.py runserver` | `mvn spring-boot:run` | 类似 |
| **热重载** | 自动 | 需手动Build | Spring略麻烦 |
| **配置文件** | `settings.py` | `application.yml` | 格式不同 |

#### 💡 重要收获

1. **Spring Boot的价值**：简化Spring配置，开箱即用
2. **自动配置原理**：约定 > 配置，按需加载
3. **分层架构**：Controller → Service → Repository
4. **开发习惯**：善用IDEA，配置热重载

#### 🚀 下一步

现在项目已经跑起来了！下一章我们将实现用户注册功能，看看Django的User模型在Java中怎么实现。

---

**第4章完**
