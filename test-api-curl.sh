#!/bin/bash
# API测试脚本 - 使用curl测试

echo "=========================================="
echo "  淘书网API测试 - 端口8083"
echo "=========================================="
echo ""

# 1. 测试用户注册
echo "1️⃣ 测试用户注册..."
curl -X POST http://localhost:8083/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"phone":"13800138000","password":"123456"}'
echo ""
echo ""

# 2. 测试用户登录
echo "2️⃣ 测试用户登录..."
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8083/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"phone":"13800138000","password":"123456"}')
echo "$LOGIN_RESPONSE"
echo ""

# 提取token（Windows Git Bash兼容）
TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"data":"[^"]*"' | cut -d'"' -f4)
echo "获取到的Token: $TOKEN"
echo ""

# 3. 测试创建商品（需要登录）
echo "3️⃣ 测试创建商品..."
curl -X POST http://localhost:8083/api/books \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"title":"Spring Boot实战","author":"张三","price":99.9,"stock":10}'
echo ""
echo ""

# 4. 测试查询商品列表
echo "4️⃣ 测试查询商品列表..."
curl -X GET "http://localhost:8083/api/books?page=0&size=10"
echo ""
echo ""

# 5. 测试查询用户信息（需要登录）
echo "5️⃣ 测试查询当前用户信息..."
curl -X GET http://localhost:8083/api/users/me \
  -H "Authorization: Bearer $TOKEN"
echo ""
echo ""

echo "=========================================="
echo "  ✅ 测试完成！"
echo "=========================================="
