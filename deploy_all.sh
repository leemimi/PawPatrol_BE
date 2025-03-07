#!/bin/bash

# 설정 변수
MAIN_SERVICE_PORT=8081
AI_SERVICE_PORT=8000

# 색상 정의
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 로그 함수
log_info() {
  echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
  echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
  echo -e "${RED}[ERROR]${NC} $1"
}

# 메인 서비스 배포
deploy_main_service() {
  log_info "메인 서비스 배포 시작..."

  # 최신 이미지 가져오기
  docker pull ghcr.io/backendschoolplus3th/pawpatrol

  # 무중단 배포 스크립트 실행
  python3 infraScript/zero_downtime_deploy.py

  if [ $? -eq 0 ]; then
    log_info "메인 서비스 배포 완료"
  else
    log_error "메인 서비스 배포 실패"
    return 1
  fi
}

# AI 서비스 배포
deploy_ai_service() {
  log_info "AI 서비스 배포 시작..."

  # 최신 이미지 가져오기
  docker pull ghcr.io/your-username/pawpatrol-ai-service:latest
  docker tag ghcr.io/your-username/pawpatrol-ai-service:latest pawpatrol-ai-service:latest

  # 무중단 배포 스크립트 실행
  python3 ai_deploy.py

  if [ $? -eq 0 ]; then
    log_info "AI 서비스 배포 완료"
  else
    log_error "AI 서비스 배포 실패"
    return 1
  fi
}

# 서비스 상태 확인
check_services() {
  log_info "서비스 상태 확인 중..."

  # 메인 서비스 확인
  curl -s http://localhost:$MAIN_SERVICE_PORT/actuator/health > /dev/null
  if [ $? -eq 0 ]; then
    log_info "메인 서비스: 정상"
  else
    log_warn "메인 서비스: 비정상"
  fi

  # AI 서비스 확인
  curl -s http://localhost:$AI_SERVICE_PORT/docs > /dev/null
  if [ $? -eq 0 ]; then
    log_info "AI 서비스: 정상"
  else
    log_warn "AI 서비스: 비정상"
  fi
}

# 메인 함수
main() {
  log_info "전체 배포 프로세스 시작..."

  # 메인 서비스 배포
  deploy_main_service
  MAIN_RESULT=$?

  # AI 서비스 배포
  deploy_ai_service
  AI_RESULT=$?

  # 서비스 상태 확인
  check_services

  # 배포 결과 요약
  log_info "배포 프로세스 완료"
  if [ $MAIN_RESULT -eq 0 ] && [ $AI_RESULT -eq 0 ]; then
    log_info "모든 서비스가 성공적으로 배포되었습니다."
  else
    log_warn "일부 서비스 배포에 문제가 발생했습니다. 로그를 확인하세요."
  fi
}

# 스크립트 실행
main
