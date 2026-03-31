@echo off
chcp 65001 >nul
echo ==========================================
echo   Taoshu API Test - Port 8083
echo ==========================================
echo.

echo [1] Register User...
curl -X POST http://127.0.0.1:8083/api/user/register -H "Content-Type: application/json" -d "{\"phone\":\"13800138000\",\"password\":\"123456\"}"
echo.
echo.

echo [2] User Login...
curl -X POST http://127.0.0.1:8083/api/user/login -H "Content-Type: application/json" -d "{\"phone\":\"13800138000\",\"password\":\"123456\"}"
echo.
echo.

echo [3] Get Book List...
curl -X GET "http://127.0.0.1:8083/api/book/list?page=0&size=10"
echo.
echo.

echo ==========================================
echo   Test Complete!
echo ==========================================
echo.
echo Copy the token from login response above.
echo Then test Book API with:
echo curl -X POST http://127.0.0.1:8083/api/book -H "Content-Type: application/json" -H "Authorization: Bearer YOUR_TOKEN" -d "{\"title\":\"Spring Boot\",\"author\":\"Zhang\",\"price\":99.9,\"stock\":10}"
pause
