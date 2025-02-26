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
from detector import detector
import detector

app = FastAPI()

# CORS 설정 추가
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


class SimilarityResponse(BaseModel):
    similarity: float
    result: str

class EmbeddingResponse(BaseModel):
    embedding: List[float]
    success: bool

def extract_embedding_from_url_directly(url: str):
    """URL에서 직접 이미지를 읽어 임베딩 추출"""
    try:
        response = requests.get(url, timeout=10)
        response.raise_for_status()

        # 이미지 데이터를 바로 PIL 이미지로 변환
        image_pil = Image.open(io.BytesIO(response.content))
        image_cv = np.array(image_pil)
        if image_cv.ndim == 2:  # 흑백 이미지라면 RGB로 변환
            image_cv = cv2.cvtColor(image_cv, cv2.COLOR_GRAY2RGB)

        # 이미지 크기 확인 및 조정
        if max(image_pil.size) > 1000:
            ratio = 1000.0 / max(image_pil.size)
            new_size = (int(image_pil.size[0] * ratio), int(image_pil.size[1] * ratio))
            image_pil = image_pil.resize(new_size, Image.LANCZOS)

        feature1, embedding1 = detector.image_vector(image_cv)

        # 전처리 및 텐서 변환
        if embedding1 is None or feature1 is None:
            return {"embedding": [], "features": [], "success": False}

        return {
            "embedding": embedding1.tolist(),
            "features": feature1.tolist(),
            "success": True
        }

    except Exception as e:
        print(f"URL 임베딩 추출 중 오류 발생: {str(e)}")
        raise HTTPException(status_code=500, detail=f"URL 임베딩 추출 중 오류 발생: {str(e)}")



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
