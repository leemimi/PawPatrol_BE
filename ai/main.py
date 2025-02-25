import os
import cv2
import dlib
import torch
import numpy as np
from PIL import Image
from fastapi import FastAPI, File, UploadFile
from torchvision import transforms
import clip
from imutils import face_utils
import io
import uvicorn

app = FastAPI()

# 모델 경로 설정
detector_path = os.path.join('models', 'dogHeadDetector.dat')
predictor_path = os.path.join('models', 'landmarkDetector.dat')

# dlib 모델 로드
detector = dlib.cnn_face_detection_model_v1(detector_path)
predictor = dlib.shape_predictor(predictor_path)

# Device 설정
device = "cuda" if torch.cuda.is_available() else "cpu"

# CLIP 모델 로드
clip_model, clip_preprocess = clip.load("ViT-B/16", device=device)
clip_model.eval()

def extract_face_embedding(image):
    """CLIP을 이용해 강아지 얼굴 임베딩을 추출"""
    image = Image.open(io.BytesIO(image))
    image = clip_preprocess(image).unsqueeze(0).to(device)

    with torch.no_grad():
        embedding = clip_model.encode_image(image)

    embedding = embedding / embedding.norm(dim=-1, keepdim=True)
    return embedding.cpu().numpy()

@app.post("/compare")
async def compare_faces(file1: UploadFile = File(...), file2: UploadFile = File(...)):
    """두 이미지 파일을 받아 강아지 얼굴 비교"""
    image1 = await file1.read()
    image2 = await file2.read()

    emb1 = extract_face_embedding(image1)
    emb2 = extract_face_embedding(image2)

    similarity = float(np.dot(emb1, emb2.T))  # 코사인 유사도

    result = "같은 동물" if similarity >= 0.9 else "다른 동물"
    return {"similarity": similarity, "result": result}

# 앱 실행을 위한 코드 추가
if __name__ == "__main__":
    # 모델 디렉토리 확인
    os.makedirs('models', exist_ok=True)

    # 모델 파일 존재 여부 확인
    if not os.path.exists(detector_path) or not os.path.exists(predictor_path):
        print(f"경고: 모델 파일이 없습니다. {detector_path}와 {predictor_path}를 다운로드하세요.")

    # FastAPI 앱 실행
    uvicorn.run(app, host="0.0.0.0", port=8000)
