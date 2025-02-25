# 1. Python 3.9 기반 이미지 사용
FROM python:3.9

# 2. 필수 패키지 설치 (CMake, libgl1, libglib2.0-0 추가)
RUN apt-get update && apt-get install -y cmake libgl1 libglib2.0-0 && rm -rf /var/lib/apt/lists/*
# 3. CLIP 모델을 올바르게 설치
RUN pip install git+https://github.com/openai/CLIP.git

# 3. 작업 디렉토리 설정
WORKDIR /app

# 4. 필요한 패키지 설치
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 5. 소스 코드 복사
COPY . .

# 6. FastAPI 실행
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
