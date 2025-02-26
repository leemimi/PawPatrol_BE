import os
import cv2
import torch
import numpy as np
from PIL import Image
from fastapi import FastAPI, File, UploadFile, HTTPException, Form
from fastapi.middleware.cors import CORSMiddleware
from torchvision import transforms
import clip
import io
import uvicorn
import json
import requests  # 추가: URL에서 이미지 다운로드용
from typing import List, Dict, Any, Optional
from pydantic import BaseModel

app = FastAPI()

# CORS 설정 추가
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Device 설정
device = "cuda" if torch.cuda.is_available() else "cpu"
print(f"사용 디바이스: {device}")

# CLIP 모델 로드
try:
    clip_model, clip_preprocess = clip.load("ViT-B/32", device=device)
    clip_model.eval()
    print("CLIP 모델 로드 성공")
except Exception as e:
    print(f"CLIP 모델 로드 실패: {str(e)}")

# dlib 관련 코드 제거하고 CLIP만 사용하도록 수정
# 응답 모델 정의
class SimilarityResponse(BaseModel):
    similarity: float
    result: str

class EmbeddingResponse(BaseModel):
    embedding: List[float]
    success: bool

def extract_face_embedding(image):
    """CLIP을 이용해 이미지 임베딩을 추출"""
    try:
        # 이미지 처리
        image_pil = Image.open(io.BytesIO(image))

        # 이미지 크기 확인 및 조정
        if max(image_pil.size) > 1000:
            # 큰 이미지 크기 조정
            ratio = 1000.0 / max(image_pil.size)
            new_size = (int(image_pil.size[0] * ratio), int(image_pil.size[1] * ratio))
            image_pil = image_pil.resize(new_size, Image.LANCZOS)

        # 전처리 및 텐서 변환
        image_tensor = clip_preprocess(image_pil).unsqueeze(0).to(device)

        # 임베딩 추출
        with torch.no_grad():
            embedding = clip_model.encode_image(image_tensor)

        # 정규화
        embedding_normalized = embedding / embedding.norm(dim=-1, keepdim=True)
        return embedding_normalized.cpu().numpy()[0]
    except Exception as e:
        print(f"임베딩 추출 오류: {str(e)}")
        return None

@app.get("/")
async def read_root():
    return {"message": "Pet Finder AI API가 실행 중입니다."}

@app.post("/compare", response_model=SimilarityResponse)
async def compare_faces(file1: UploadFile = File(...), file2: UploadFile = File(...)):
    """두 이미지 파일을 받아 비교"""
    try:
        # 파일 유효성 검사
        if not file1.filename or not file2.filename:
            raise HTTPException(status_code=400, detail="유효하지 않은 파일입니다.")

        image1 = await file1.read()
        image2 = await file2.read()

        # 임베딩 추출
        emb1 = extract_face_embedding(image1)
        emb2 = extract_face_embedding(image2)

        if emb1 is None or emb2 is None:
            raise HTTPException(status_code=400, detail="이미지에서 특징을 추출할 수 없습니다.")

        # 코사인 유사도 계산
        similarity = float(np.dot(emb1, emb2))

        # 결과값이 -1~1 범위인지 확인
        if similarity < -1 or similarity > 1:
            print(f"경고: 유사도 값이 범위를 벗어남: {similarity}")
            similarity = max(-1, min(similarity, 1))

        result = "같은 동물" if similarity >= 0.85 else "다른 동물"
        return {"similarity": similarity, "result": result}
    except Exception as e:
        print(f"비교 중 오류 발생: {str(e)}")
        raise HTTPException(status_code=500, detail=f"비교 중 오류 발생: {str(e)}")

@app.post("/extract-embedding", response_model=EmbeddingResponse)
async def extract_embedding(file: UploadFile = File(...)):
    """이미지에서 임베딩을 추출하여 반환"""
    try:
        if not file.filename:
            raise HTTPException(status_code=400, detail="유효하지 않은 파일입니다.")

        image_data = await file.read()
        embedding = extract_face_embedding(image_data)

        if embedding is None:
            return {"embedding": [], "success": False}

        # numpy 배열을 Python 리스트로 변환
        embedding_list = embedding.tolist()
        return {"embedding": embedding_list, "success": True}
    except Exception as e:
        print(f"임베딩 추출 중 오류 발생: {str(e)}")
        raise HTTPException(status_code=500, detail=f"임베딩 추출 중 오류 발생: {str(e)}")

@app.post("/compare-with-embedding")
async def compare_with_embedding(
        file: UploadFile = File(...),
        stored_embedding: str = Form(...)
):
    """저장된 임베딩과 이미지를 비교"""
    try:
        # 문자열로 받은 임베딩을 파싱
        try:
            stored_embedding_list = json.loads(stored_embedding)
        except json.JSONDecodeError:
            raise HTTPException(status_code=400, detail="임베딩 형식이 잘못되었습니다.")

        image_data = await file.read()
        new_embedding = extract_face_embedding(image_data)

        if new_embedding is None:
            raise HTTPException(status_code=400, detail="이미지에서 특징을 추출할 수 없습니다.")

        # numpy 배열로 변환
        stored_embedding_np = np.array(stored_embedding_list)

        # 코사인 유사도 계산
        similarity = float(np.dot(new_embedding, stored_embedding_np))

        # 결과값이 -1~1 범위인지 확인
        if similarity < -1 or similarity > 1:
            print(f"경고: 유사도 값이 범위를 벗어남: {similarity}")
            similarity = max(-1, min(similarity, 1))

        result = "같은 동물" if similarity >= 0.85 else "다른 동물"
        return {"similarity": similarity, "result": result}
    except Exception as e:
        print(f"비교 중 오류 발생: {str(e)}")
        raise HTTPException(status_code=500, detail=f"비교 중 오류 발생: {str(e)}")

@app.post("/batch-compare")
async def batch_compare(
        file: UploadFile = File(...),
        embeddings_json: str = Form(...)
):
    """저장된 여러 임베딩과 이미지를 비교하고 유사도 순으로 결과 반환"""
    try:
        # JSON 파싱
        try:
            embeddings = json.loads(embeddings_json)
        except json.JSONDecodeError:
            raise HTTPException(status_code=400, detail="임베딩 JSON 형식이 잘못되었습니다.")

        image_data = await file.read()
        new_embedding = extract_face_embedding(image_data)

        if new_embedding is None:
            raise HTTPException(status_code=400, detail="이미지에서 특징을 추출할 수 없습니다.")

        results = []
        for animal_id, stored_embedding in embeddings.items():
            try:
                # numpy 배열로 변환
                stored_embedding_np = np.array(stored_embedding)

                # 코사인 유사도 계산
                similarity = float(np.dot(new_embedding, stored_embedding_np))

                # 결과값이 -1~1 범위인지 확인
                if similarity < -1 or similarity > 1:
                    print(f"경고: 유사도 값이 범위를 벗어남: {similarity}")
                    similarity = max(-1, min(similarity, 1))

                results.append({
                    "animal_id": animal_id,
                    "similarity": similarity,
                    "is_match": similarity >= 0.85
                })
            except Exception as e:
                print(f"동물 ID {animal_id} 처리 중 오류: {str(e)}")

        # 유사도 내림차순으로 정렬
        results = sorted(results, key=lambda x: x["similarity"], reverse=True)

        return {"results": results}
    except Exception as e:
        print(f"배치 비교 중 오류 발생: {str(e)}")
        raise HTTPException(status_code=500, detail=f"배치 비교 중 오류 발생: {str(e)}")
# URL에서 직접 임베딩을 추출하는 함수 추가
def extract_embedding_from_url_directly(url: str):
    """URL에서 직접 이미지를 읽어 임베딩 추출"""
    try:
        response = requests.get(url, timeout=10)
        response.raise_for_status()

        # 이미지 데이터를 바로 PIL 이미지로 변환
        image_pil = Image.open(io.BytesIO(response.content))

        # 이미지 크기 확인 및 조정
        if max(image_pil.size) > 1000:
            ratio = 1000.0 / max(image_pil.size)
            new_size = (int(image_pil.size[0] * ratio), int(image_pil.size[1] * ratio))
            image_pil = image_pil.resize(new_size, Image.LANCZOS)

        # 전처리 및 텐서 변환
        image_tensor = clip_preprocess(image_pil).unsqueeze(0).to(device)

        # 임베딩 추출
        with torch.no_grad():
            embedding = clip_model.encode_image(image_tensor)

        # 정규화
        embedding_normalized = embedding / embedding.norm(dim=-1, keepdim=True)
        return embedding_normalized.cpu().numpy()[0]
    except Exception as e:
        print(f"URL 임베딩 추출 오류: {str(e)}")
        return None

@app.post("/extract-embedding-from-url", response_model=EmbeddingResponse)
async def extract_embedding_from_url(url: str = Form(...)):
    """URL에서 이미지를 직접 임베딩 추출"""
    try:
        embedding = extract_embedding_from_url_directly(url)

        if embedding is None:
            return {"embedding": [], "success": False}

        embedding_list = embedding.tolist()
        return {"embedding": embedding_list, "success": True}
    except Exception as e:
        print(f"URL 임베딩 추출 중 오류 발생: {str(e)}")
        raise HTTPException(status_code=500, detail=f"URL 임베딩 추출 중 오류 발생: {str(e)}")

@app.post("/compare-urls")
async def compare_urls(url1: str = Form(...), url2: str = Form(...)):
    """두 URL의 이미지를 비교"""
    try:
        emb1 = extract_embedding_from_url_directly(url1)
        emb2 = extract_embedding_from_url_directly(url2)

        if emb1 is None or emb2 is None:
            raise HTTPException(status_code=400, detail="이미지에서 특징을 추출할 수 없습니다.")

        similarity = float(np.dot(emb1, emb2))
        similarity = max(-1, min(similarity, 1))

        result = "같은 동물" if similarity >= 0.85 else "다른 동물"
        return {"similarity": similarity, "result": result}
    except Exception as e:
        print(f"URL 비교 중 오류 발생: {str(e)}")
        raise HTTPException(status_code=500, detail=f"URL 비교 중 오류 발생: {str(e)}")

@app.post("/batch-compare-url")
async def batch_compare_url(
        url: str = Form(...),
        embeddings_json: str = Form(...)
):
    """URL 이미지와 저장된 여러 임베딩을 비교"""
    try:
        embeddings = json.loads(embeddings_json)

        new_embedding = extract_embedding_from_url_directly(url)

        if new_embedding is None:
            raise HTTPException(status_code=400, detail="이미지에서 특징을 추출할 수 없습니다.")

        # 기존 배치 비교 로직과 동일
        results = []
        for animal_id, stored_embedding in embeddings.items():
            try:
                stored_embedding_np = np.array(stored_embedding)
                similarity = float(np.dot(new_embedding, stored_embedding_np))
                similarity = max(-1, min(similarity, 1))

                results.append({
                    "animal_id": animal_id,
                    "similarity": similarity,
                    "is_match": similarity >= 0.85
                })
            except Exception as e:
                print(f"동물 ID {animal_id} 처리 중 오류: {str(e)}")

        results = sorted(results, key=lambda x: x["similarity"], reverse=True)
        return {"results": results}
    except Exception as e:
        print(f"URL 배치 비교 중 오류 발생: {str(e)}")
        raise HTTPException(status_code=500, detail=f"URL 배치 비교 중 오류 발생: {str(e)}")

# 앱 실행을 위한 코드 추가
if __name__ == "__main__":
    # FastAPI 앱 실행
    print("API 서버 시작 중...")
    uvicorn.run(app, host="0.0.0.0", port=8000)
