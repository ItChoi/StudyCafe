#!/bin/bash
BASE_URL="http://localhost:8080"

echo "===== 회원가입 ====="
curl -s -X POST "$BASE_URL/api/auth/signup" \
  -H "Content-Type: application/json" \
  -d '{"nickname":"testuser","password":"password123","gender":"MALE"}' | jq .

echo ""
echo "===== 로그인 ====="
TOKEN=$(curl -s -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"nickname":"testuser","password":"password123"}' | jq -r '.token')
echo "Token: $TOKEN"

echo ""
echo "===== 내 정보 조회 ====="
curl -s -X GET "$BASE_URL/api/members/me" \
  -H "Authorization: Bearer $TOKEN" | jq .
