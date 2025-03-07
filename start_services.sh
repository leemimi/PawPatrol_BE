#!/bin/bash

# 모델 디렉토리 생성
mkdir -p /dockerProjects/pawpatrol/ai/models
mkdir -p /dockerProjects/pawpatrol/volumes/gen

# 모델 파일이 있는지 확인
if [ ! -f "/dockerProjects/pawpatrol/ai/models/dogHeadDetector.dat" ]; then
  echo "모델 파일이 없습니다. 모델 파일을 다운로드하거나 복사해주세요."
  exit 1
fi

# Docker 이미지 가져오기
docker pull ghcr.io/backendschoolplus3th/pawpatrol
docker pull ghcr.io/backendschoolplus3th/pawpatrol-ai-service:latest
docker tag ghcr.io/backendschoolplus3th/pawpatrol-ai-service:latest pawpatrol-ai-service:latest

# 컨테이너 초기 실행
docker run -d --name=pawpatrol_1 --restart unless-stopped -p 8082:8090 -e TZ=Asia/Seoul -v /dockerProjects/pawpatrol/volumes/gen:/gen ghcr.io/backendschoolplus3th/pawpatrol
docker run -d --name=ai_service_1 --restart unless-stopped -p 8001:8000 -e TZ=Asia/Seoul -v /dockerProjects/pawpatrol/ai/models:/app/models pawpatrol-ai-service:latest

# 포트 포워딩 설정
nohup socat -t0 TCP-LISTEN:8081,fork,reuseaddr TCP:localhost:8082 &>/dev/null &
nohup socat -t0 TCP-LISTEN:8000,fork,reuseaddr TCP:localhost:8001 &>/dev/null &

echo "서비스가 성공적으로 시작되었습니다."
echo "메인 서비스: https://pawpatrols.shop"
echo "AI 서비스: https://ai.pawpatrols.shop"  # 또는 https://pawpatrols.shop/ai 등 원하는 URL 패턴
