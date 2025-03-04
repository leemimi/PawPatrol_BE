package com.patrol.global.initData;

import com.patrol.api.member.member.dto.request.PetRegisterRequest;
import com.patrol.domain.animal.enums.AnimalGender;
import com.patrol.domain.animal.enums.AnimalSize;
import com.patrol.domain.animal.enums.AnimalType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * packageName    : com.patrol.global.initData
 * fileName       : SampleMyPetData
 * author         : sungjun
 * date           : 2025-03-02
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-02        kyd54       최초 생성
 */
public class SampleMyPetData {

    // 샘플 이미지 URL 리스트
    public static List<String> getSampleImageUrls() {
        return Arrays.asList(
                "https://kr.object.ncloudstorage.com/paw-patrol/petRegister/tt1.jpg",
                "https://kr.object.ncloudstorage.com/paw-patrol/petRegister/tt2.jpg",
                "https://kr.object.ncloudstorage.com/paw-patrol/petRegister/tt3.jpg",
                "https://kr.object.ncloudstorage.com/paw-patrol/petRegister/tt4.jpg",
                "https://kr.object.ncloudstorage.com/paw-patrol/petRegister/tt5.jpg",
                "https://kr.object.ncloudstorage.com/paw-patrol/petRegister/tt6.jpg"
        );
    }

    // 반려동물 등록 요청 객체 리스트 생성
    public static List<PetRegisterRequest> getSamplePets() {
        List<PetRegisterRequest> pets = new ArrayList<>();

        // 1. 비숑프리제
        pets.add(new PetRegisterRequest(
                "초코", // 이름
                "BF12345", // 등록번호
                AnimalType.DOG, // 개
                "비숑프리제",
                AnimalGender.M, // 수컷
                AnimalSize.SMALL, // 소형견
                null, // MultipartFile은 null로 설정
                "3", // 3세
                "건강함", // 건강 상태
                "털이 하얗고 곱슬거립니다. 활발하고 사교적인 성격입니다." // 특징
        ));

        // 2. 슈나우저
        pets.add(new PetRegisterRequest(
                "몽이",
                "SZ67890",
                AnimalType.DOG,
                "슈나우저",
                AnimalGender.W, // 암컷
                AnimalSize.MEDIUM, // 중형견
                null,
                "4", // 4세
                "건강함",
                "수염이 특징적이며 영리하고 경계심이 강합니다. 털 관리가 필요합니다."
        ));

        // 3. 이탈리안 그레이하운드
        pets.add(new PetRegisterRequest(
                "루나",
                "IG24680",
                AnimalType.DOG,
                "이탈리안 그레이하운드",
                AnimalGender.W,
                AnimalSize.MEDIUM,
                null,
                "2", // 2세
                "건강함",
                "날씬하고 우아한 체형, 빠른 달리기 실력이 있습니다. 온순하고 애정표현을 좋아합니다."
        ));

        // 4. 스핑크스
        pets.add(new PetRegisterRequest(
                "스핑키",
                "SP13579",
                AnimalType.CAT, // 고양이
                "스핑크스",
                AnimalGender.M,
                AnimalSize.MEDIUM,
                null,
                "1", // 1세
                "건강함",
                "털이 없는 고양이로 체온 유지에 주의가 필요합니다. 활발하고 사람을 좋아합니다."
        ));

        // 5. 렉돌
        pets.add(new PetRegisterRequest(
                "구름",
                "RD97531",
                AnimalType.CAT,
                "렉돌",
                AnimalGender.W,
                AnimalSize.LARGE, // 대형 고양이
                null,
                "3", // 3세
                "건강함",
                "푹신한 털과 아름다운 눈이 특징입니다. 온순하고 사람을 잘 따릅니다."
        ));

        // 6. 아비시니안
        pets.add(new PetRegisterRequest(
                "티키",
                "AB86420",
                AnimalType.CAT,
                "아비시니안",
                AnimalGender.M,
                AnimalSize.MEDIUM,
                null,
                "2", // 2세
                "건강함",
                "티키색 털이 특징적이며 활동적이고 호기심이 많습니다. 지능이 높은 고양이입니다."
        ));

        return pets;
    }
}
