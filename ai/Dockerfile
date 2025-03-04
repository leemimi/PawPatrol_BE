# 1. Python 3.9 기반 이미지 사용
FROM python:3.9

# 2. 환경 변수 설정 (pip 캐싱 최적화)
ENV PIP_NO_CACHE_DIR=0 \
    PIP_DISABLE_PIP_VERSION_CHECK=1 \
    DEBIAN_FRONTEND=noninteractive

# 3. 필수 패키지 설치 (dlib 빌드 필수 패키지 포함)
RUN apt-get update && apt-get install -y --no-install-recommends \
    cmake \
    make \
    g++ \
    build-essential \
    libgl1 \
    libglib2.0-0 \
    libx11-dev \
    libatlas-base-dev \
    libjpeg-dev \
    libpng-dev \
    libtiff-dev \
    libboost-all-dev \
    libopenblas-dev \
    python3-dev \
    python3-setuptools \
    && rm -rf /var/lib/apt/lists/*


# 4. 작업 디렉토리 설정
WORKDIR /app

# 5. requirements.txt를 먼저 복사 (캐싱 활용)
COPY requirements.txt .

# 6. pip 업그레이드 및 numpy 먼저 설치 (dlib 의존성 해결)
RUN pip install --upgrade pip && \
    pip install --no-cache-dir numpy

# 7. dlib 먼저 설치 (컴파일 필요)
RUN pip install --no-cache-dir dlib==19.24.0

# 7. 다른 Python 패키지 설치 (캐시 유지 가능)
RUN pip install --no-cache-dir -r requirements.txt

# 8. 소스 코드 복사 (코드가 바뀌어도 패키지 캐싱 유지됨)
COPY . .

# 9. FastAPI 실행
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
