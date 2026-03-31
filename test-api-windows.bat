@echo off
REM API测试脚本 - Windows CMD版本

echo ==========================================
echo   淘书网API测试 - 端口8083
echo ==========================================
echo.

REM 1. 测试用户注册
echo 1. 测试用户注册...
curl -X POST http://localhost:8083/api/users/register -H "Content-Type: application/json" -d "{\"phone\":\"13800138000\",\"password\":\"123456\"}"
echo.
echo.

REM 2. 测试用户登录
echo 2. 测试用户登录...
curl -X POST http://localhost:8083/api/users/login -H "Content-Type: application/json" -d "{\"phone\":\"13800138000\",\"password\":\"123456\"}"
echo.
echo.

echo ==========================================
echo   测试完成！
echo ==========================================
echo.
echo 请复制上面的 token，然后继续测试商品API
echo.
echo 测试创建商品：
echo curl -X POST http://localhost:8083/api/books -H "Content-Type: application/json" -H "Authorization: Bearer YOUR_TOKEN" -d "{\"title\":\"Spring Boot实战\",\"author\":\"张三\",\"price\":99.9,\"stock\":10}"
echo.
echo 测试查询商品列表：
echo curl -X GET "http://localhost:8083/api/books?page=0&size=10"
pause
