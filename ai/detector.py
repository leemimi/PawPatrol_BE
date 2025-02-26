import os
import cv2
import dlib
import numpy as np
import matplotlib.pyplot as plt
import torch
from PIL import Image
from torchvision import transforms
from imutils import face_utils
import clip

# =====================
# 모델 및 전처리 초기화
# =====================

# dlib 모델 파일 경로
detector_path = os.path.join('models', 'dogHeadDetector.dat')
predictor_path = os.path.join('models', 'landmarkDetector.dat')

# dlib 모델 로드
detector = dlib.cnn_face_detection_model_v1(detector_path)
predictor = dlib.shape_predictor(predictor_path)

# Device 선택: cuda, mps, 없으면 cpu
if torch.cuda.is_available():
    device = "cuda"
elif torch.backends.mps.is_available():
    device = "mps"
else:
    device = "cpu"

# CLIP 모델 로드 (ViT-B/16)
clip_model, clip_preprocess = clip.load("ViT-B/16", device=device)
clip_model.eval()
transform = clip_preprocess  # CLIP 전처리 transform

# =====================
# 헬퍼 함수
# =====================

def _trim_css_to_bounds(css, image_shape):
    """이미지 경계 내로 좌표 제한 (top, right, bottom, left)"""
    return (max(css[0], 0),
            min(css[1], image_shape[1]),
            min(css[2], image_shape[0]),
            max(css[3], 0))

def _rect_to_css(rect):
    """dlib rect를 CSS 스타일 좌표 (top, right, bottom, left)로 변환"""
    return rect.top(), rect.right(), rect.bottom(), rect.left()

def _raw_face_locations(img, upsample_num=1):
    """얼굴 위치 검출"""
    return detector(img, upsample_num)

def face_locations(img, upsample_num=1):
    """얼굴 위치 검출 및 좌표 변환"""
    detections = _raw_face_locations(img, upsample_num)
    return [_trim_css_to_bounds(_rect_to_css(face.rect), img.shape)
            for face in detections]

def extract_face_embedding(image, face_location, padding=50):
    """얼굴 영역에서 CLIP 임베딩 추출"""
    top, right, bottom, left = face_location
    face_img = image[
        max(0, top - padding): min(image.shape[0], bottom + padding),
        max(0, left - padding): min(image.shape[1], right + padding)
    ]
    face_pil = Image.fromarray(cv2.cvtColor(face_img, cv2.COLOR_BGR2RGB))
    face_tensor = transform(face_pil).unsqueeze(0).to(device)
    with torch.no_grad():
        embedding = clip_model.encode_image(face_tensor)
    # 코사인 유사도 계산을 위해 정규화
    embedding = embedding / embedding.norm(dim=-1, keepdim=True)
    return embedding

def extract_landmark_features(shape, image, gray_image):
    """랜드마크 특징 추출 (오류 상황을 고려하여 robust하게 처리)"""
    coords = face_utils.shape_to_np(shape)
    features = []
    
    if coords.size == 0:
        return np.array(features)
    
    n_landmarks = coords.shape[0]
    
    # 1. 얼굴 윤곽 특징: aspect ratio (division by zero 방지)
    face_width = np.max(coords[:, 0]) - np.min(coords[:, 0])
    face_height = np.max(coords[:, 1]) - np.min(coords[:, 1])
    aspect_ratio = face_width / face_height if face_height != 0 else 0
    features.append(aspect_ratio)
    
    # 2. 얼굴 대칭성
    mid_x = np.mean(coords[:, 0])
    left_points = coords[coords[:, 0] < mid_x]
    right_points = coords[coords[:, 0] > mid_x]
    if left_points.size > 0 and right_points.size > 0:
        left_mean = np.mean(left_points, axis=0)
        right_mean = np.mean(right_points, axis=0)
        symmetry = np.linalg.norm(left_mean - right_mean)
        features.append(symmetry)
    
    # 3. 눈 특징 (전체 랜드마크의 1/3씩을 눈 영역으로 가정)
    third = n_landmarks // 3
    if third > 0:
        left_eye_points = coords[:third]
        right_eye_points = coords[third:2*third]
        if left_eye_points.size > 0 and right_eye_points.size > 0:
            left_eye_width = np.max(left_eye_points[:, 0]) - np.min(left_eye_points[:, 0])
            right_eye_width = np.max(right_eye_points[:, 0]) - np.min(right_eye_points[:, 0])
            eye_ratio = left_eye_width / right_eye_width if right_eye_width != 0 else 0
            features.append(eye_ratio)
            
            for eye_points in [left_eye_points, right_eye_points]:
                x1, y1 = np.min(eye_points, axis=0)
                x2, y2 = np.max(eye_points, axis=0)
                if x2 > x1 and y2 > y1:
                    eye_region = gray_image[y1:y2, x1:x2]
                    if eye_region.size > 0:
                        features.extend([
                            float(np.mean(eye_region)),
                            float(np.std(eye_region)),
                            float(np.max(eye_region) - np.min(eye_region))
                        ])
    
    # 4. 코 특징: 충분한 랜드마크가 있을 때만 처리
    if n_landmarks >= 18:
        nose_points = coords[12:18]
        if nose_points.size > 0:
            nose_width = np.max(nose_points[:, 0]) - np.min(nose_points[:, 0])
            nose_height = np.max(nose_points[:, 1]) - np.min(nose_points[:, 1])
            nose_ratio = nose_width / nose_height if nose_height != 0 else 0
            features.append(nose_ratio)
            
            x1, y1 = np.min(nose_points, axis=0)
            x2, y2 = np.max(nose_points, axis=0)
            if x2 > x1 and y2 > y1:
                nose_region = gray_image[y1:y2, x1:x2]
                if nose_region.size > 0:
                    features.extend([
                        float(np.mean(nose_region)),
                        float(np.std(nose_region)),
                        float(np.max(nose_region) - np.min(nose_region))
                    ])
    
    # 5. 윤곽선 곡률: 인접 점들 사이의 각도 (0으로 나누는 경우 방지)
    for i in range(1, n_landmarks - 1):
        p1, p2, p3 = coords[i - 1], coords[i], coords[i + 1]
        v1 = p1 - p2
        v2 = p3 - p2
        norm_product = np.linalg.norm(v1) * np.linalg.norm(v2)
        if norm_product > 0:
            angle = np.arccos(np.clip(np.dot(v1, v2) / norm_product, -1.0, 1.0))
            features.append(angle)
    
    # 6. 텍스처 패턴: 각 랜드마크 주변의 간단한 HOG 특징
    patch_size = 7
    for (x, y) in coords.astype(int):
        x_start = max(0, x - patch_size)
        x_end = min(gray_image.shape[1], x + patch_size)
        y_start = max(0, y - patch_size)
        y_end = min(gray_image.shape[0], y + patch_size)
        patch = gray_image[y_start:y_end, x_start:x_end]
        if patch.size > 0:
            gx = cv2.Sobel(patch, cv2.CV_32F, 1, 0)
            gy = cv2.Sobel(patch, cv2.CV_32F, 0, 1)
            mag, ang = cv2.cartToPolar(gx, gy)
            features.extend([
                float(np.mean(mag)),
                float(np.std(mag)),
                float(np.mean(ang)),
                float(np.std(ang))
            ])
    
    # 7. 컬러 특징: 각 랜드마크 주변의 BGR 채널 평균/표준편차
    for (x, y) in coords.astype(int):
        x_start = max(0, x - 3)
        x_end = min(image.shape[1], x + 3)
        y_start = max(0, y - 3)
        y_end = min(image.shape[0], y + 3)
        patch = image[y_start:y_end, x_start:x_end]
        if patch.size > 0:
            for i in range(3):
                features.extend([
                    float(np.mean(patch[:, :, i])),
                    float(np.std(patch[:, :, i]))
                ])
    
    return np.array(features)

def draw_annotations(image, face, shape, scores):
    """시각화: 얼굴 박스, 랜드마크, 점수 표시"""
    top, right, bottom, left = face
    cv2.rectangle(image, (left, top), (right, bottom), (0, 255, 0), 4)
    for (x, y) in face_utils.shape_to_np(shape):
        cv2.circle(image, (x, y), 3, (0, 0, 255), -1)
    text1 = f"Emb: {scores['embedding']:.2f}, Lmk: {scores['landmark']:.2f}"
    text2 = f"Combined: {scores['combined']:.2f}"
    cv2.putText(image, text1, (10, 30),
                cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 255, 0), 2)
    cv2.putText(image, text2, (10, 60),
                cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 255, 0), 2)

# =====================
# 얼굴 비교 및 시각화
# =====================

def compare_faces(img1, img2, display=True):
    """
    두 이미지의 얼굴(강아지 머리)을 비교하여 임베딩 및 랜드마크 기반 유사도를 계산합니다.
    display=True인 경우 matplotlib으로 시각화합니다.
    """
    gray1 = cv2.cvtColor(img1, cv2.COLOR_BGR2GRAY)
    gray2 = cv2.cvtColor(img2, cv2.COLOR_BGR2GRAY)
    faces1 = face_locations(gray1)
    faces2 = face_locations(gray2)
    
    if not faces1 or not faces2:
        print("하나 이상의 이미지에서 강아지 얼굴을 찾을 수 없습니다.")
        return None, None, None

    # 첫 번째 검출된 얼굴 사용
    face1 = faces1[0]
    face2 = faces2[0]

    # dlib.rectangle 생성 (dlib.rectangle(left, top, right, bottom))
    face1_rect = dlib.rectangle(face1[3], face1[0], face1[1], face1[2])
    face2_rect = dlib.rectangle(face2[3], face2[0], face2[1], face2[2])
    
    shape1 = predictor(gray1, face1_rect)
    shape2 = predictor(gray2, face2_rect)
    
    # 랜드마크 특징 추출 (컬러와 그레이스케일 이미지 모두 사용)
    lmk_features1 = extract_landmark_features(shape1, img1, gray1)
    lmk_features2 = extract_landmark_features(shape2, img2, gray2)
    
    # CLIP 임베딩 추출
    embedding1 = extract_face_embedding(img1, face1)
    embedding2 = extract_face_embedding(img2, face2)
    
    # 임베딩 유사도 (코사인 유사도)
    emb_sim = torch.nn.functional.cosine_similarity(embedding1, embedding2).item()
    
    # 랜드마크 특징 유사도 (0 division 방지)
    norm1 = np.linalg.norm(lmk_features1)
    norm2 = np.linalg.norm(lmk_features2)
    if norm1 + norm2 == 0:
        lmk_sim = 0
    else:
        lmk_sim = 1 - (np.linalg.norm(lmk_features1 - lmk_features2) / (norm1 + norm2))
    
    # 가중치 조정 (상황에 따라 조정 가능)
    comb_score = 0.6 * emb_sim + 0.4 * lmk_sim

    result_img1 = img1.copy()
    result_img2 = img2.copy()
    scores = {'embedding': emb_sim, 'landmark': lmk_sim, 'combined': comb_score}
    
    draw_annotations(result_img1, face1, shape1, scores)
    draw_annotations(result_img2, face2, shape2, scores)
    
    # 시각화를 위한 얼굴 영역 추출 (패딩 적용)
    vis_padding = 50
    face1_img = img1[
        max(0, face1[0] - vis_padding):min(img1.shape[0], face1[2] + vis_padding),
        max(0, face1[3] - vis_padding):min(img1.shape[1], face1[1] + vis_padding)
    ]
    face2_img = img2[
        max(0, face2[0] - vis_padding):min(img2.shape[0], face2[2] + vis_padding),
        max(0, face2[3] - vis_padding):min(img2.shape[1], face2[1] + vis_padding)
    ]
    
    # display=True인 경우에만 matplotlib 시각화 실행
    if display:
        plt.figure(figsize=(15, 8))
        plt.subplot(231)
        plt.imshow(cv2.cvtColor(result_img1, cv2.COLOR_BGR2RGB))
        plt.title('Image 1')
        plt.axis('off')
        
        plt.subplot(232)
        plt.imshow(cv2.cvtColor(result_img2, cv2.COLOR_BGR2RGB))
        plt.title('Image 2')
        plt.axis('off')
        
        plt.subplot(233)
        plt.text(0.5, 0.6, 'Similarity Scores:',
                 horizontalalignment='center',
                 verticalalignment='center',
                 fontsize=12, transform=plt.gca().transAxes)
        plt.text(0.5, 0.4,
                 f'Embedding: {emb_sim:.2f}\nLandmark: {lmk_sim:.2f}\nCombined: {comb_score:.2f}',
                 horizontalalignment='center',
                 verticalalignment='center',
                 fontsize=10, transform=plt.gca().transAxes)
        plt.axis('off')
        
        plt.subplot(235)
        plt.imshow(cv2.cvtColor(face1_img, cv2.COLOR_BGR2RGB))
        plt.title('Face 1')
        plt.axis('off')
        
        plt.subplot(236)
        plt.imshow(cv2.cvtColor(face2_img, cv2.COLOR_BGR2RGB))
        plt.title('Face 2')
        plt.axis('off')
        
        plt.tight_layout()
        plt.show()
    
    return result_img1, result_img2, comb_score

# =====================
# 메인 실행부 (테스트용)
# =====================
if __name__ == '__main__':
    img1_path = 'examples/dog5.jpg'
    img2_path = 'examples/dog5_1.jpg'
    
    img1 = cv2.imread(img1_path)
    img2 = cv2.imread(img2_path)
    
    if img1 is None or img2 is None:
        print("이미지 파일을 불러올 수 없습니다.")
    else:
        res1, res2, similarity = compare_faces(img1, img2, display=True)
        if res1 is not None:
            plt.figure(figsize=(15, 5))
            plt.subplot(121)
            plt.imshow(cv2.cvtColor(res1, cv2.COLOR_BGR2RGB))
            plt.title('Result Image 1')
            plt.axis('off')
            
            plt.subplot(122)
            plt.imshow(cv2.cvtColor(res2, cv2.COLOR_BGR2RGB))
            plt.title('Result Image 2')
            plt.axis('off')
            
            plt.suptitle(f'Combined Similarity: {similarity:.2f}', fontsize=16)
            plt.show()


def image_vector(img1):
    """
    두 이미지의 얼굴(강아지 머리)을 비교하여 임베딩 및 랜드마크 기반 유사도를 계산합니다.
    display=True인 경우 matplotlib으로 시각화합니다.
    """
    gray1 = cv2.cvtColor(img1, cv2.COLOR_BGR2GRAY)
    faces1 = face_locations(gray1)

    if len(faces1) == 0:  # 🛑 얼굴이 검출되지 않으면 예외 처리 필요
        raise ValueError("얼굴을 감지하지 못했습니다.")

    face1 = faces1[0]

    # dlib.rectangle 생성 (dlib.rectangle(left, top, right, bottom))
    face1_rect = dlib.rectangle(face1[3], face1[0], face1[1], face1[2])

    shape1 = predictor(gray1, face1_rect)

    # 랜드마크 특징 추출 (컬러와 그레이스케일 이미지 모두 사용)
    lmk_features1 = extract_landmark_features(shape1, img1, gray1)

    # CLIP 임베딩 추출
    embedding1 = extract_face_embedding(img1, face1)

    return lmk_features1, embedding1
