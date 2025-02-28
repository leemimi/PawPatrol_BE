package com.patrol.global.initData;

import com.patrol.api.member.member.dto.request.PetRegisterRequest;
import com.patrol.domain.animal.enums.AnimalGender;
import com.patrol.domain.animal.enums.AnimalSize;
import com.patrol.domain.animal.enums.AnimalType;

import java.util.ArrayList;
import java.util.List;


public class SampleAnimalData {
  
  public static List<PetRegisterRequest> getSampleStrayAnimals() {
    List<PetRegisterRequest> animals = new ArrayList<>();

    // 1. 진도믹스 강아지 (황색/갈색)
    animals.add(new PetRegisterRequest(
        "밤이",                   // name (no name as stray)
        null,                   // registrationNo
        AnimalType.DOG,         // animalType
        "진도 믹스",            // breed
        AnimalGender.M,      // gender
        AnimalSize.MEDIUM,      // size
        null,                   // imageFile (will be handled separately)
        "1-2살 추정",            // estimatedAge
        "체중 미달",          // healthCondition
        "황갈색 털에 뾰족한 귀, 수줍은 성격"  // feature
    ));

    // 2. 푸른 눈의 얼룩 고양이 새끼
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏 태비", // breed
        AnimalGender.W,   // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "2-3달 추정",           // estimatedAge
        "건강 양호",              // healthCondition
        "갈색 태비 무늬에 밝은 파란 눈, 매우 어린 나이"  // feature
    ));

    // 3. 깔때기 착용한 고양이
    animals.add(new PetRegisterRequest(
        "단이",                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏",   // breed
        AnimalGender.M,      // gender
        AnimalSize.MEDIUM,      // size
        null,                   // imageFile
        "2-3살 추정",            // estimatedAge
        "최근 수술 받음", // healthCondition
        "흑백 털에 애교만점 냥이"  // feature
    ));

    // 4. 얼룩 고양이 새끼
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏 태비", // breed
        AnimalGender.W,   // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "3-4달 추정",           // estimatedAge
        "건강함",              // healthCondition
        "갈색 태비 무늬의 겁이 많은 착한 아이"  // feature
    ));

    // 5. 흰색 옷 입은 치와와
    animals.add(new PetRegisterRequest(
        "콩콩이",                   // name
        null,                   // registrationNo
        AnimalType.DOG,         // animalType
        "치와와",            // breed
        AnimalGender.W,   // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "5-7살 추정",            // estimatedAge
        "건강 양호",       // healthCondition
        "흰색/회색 털, 옷 입는 것을 좋아함"  // feature
    ));

    // 6. 푸른 눈의 흰 고양이 새끼
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "레그돌 믹스", // breed
        AnimalGender.W,   // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "2-3달 추정",           // estimatedAge
        "건강 양호",              // healthCondition
        "흰색 털에 황갈색 포인트가 있는 파란 눈 새끼 고양이"  // feature
    ));

    // 7. 회색 줄무늬 고양이
    animals.add(new PetRegisterRequest(
        "구름",                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏 태비", // breed
        AnimalGender.W,   // gender
        AnimalSize.MEDIUM,      // size
        null,                   // imageFile
        "1-2달 추정",            // estimatedAge
        "건강 양호",       // healthCondition
        "회색/갈색 태비에 흰색 턱, 검사 중"  // feature
    ));

    // 8. 장난감을 가진 작은 강아지
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.DOG,         // animalType
        "포메라니안 믹스",       // breed
        AnimalGender.W,   // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "1-2살 추정",           // estimatedAge
        "건강 양호",              // healthCondition
        "크림/베이지색 털, 작은 공 장난감을 가진 폭신한 강아지"  // feature
    ));

    // 9. 하얀 비숑 프리제
    animals.add(new PetRegisterRequest(
        "두부",                   // name
        null,                   // registrationNo
        AnimalType.DOG,         // animalType
        "비숑 프리제",         // breed
        AnimalGender.W,   // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "4-5살 추정",            // estimatedAge
        "건강 양호",       // healthCondition
        "흰색 폭신한 털, 둥근 얼굴, 경계하는 표정"  // feature
    ));

    // 10. 장난감을 가진 작은 흰 말티즈 강아지
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.DOG,         // animalType
        "말티즈 믹스",          // breed
        AnimalGender.W,   // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "3-4달 추정",           // estimatedAge
        "건강 양호",              // healthCondition
        "흰색 폭신한 털, 다채로운 로프 장난감을 가진 어린 강아지"  // feature
    ));

    // 11. 파란 침대에 있는 하얀 강아지
    animals.add(new PetRegisterRequest(
        "보석",                   // name
        null,                   // registrationNo
        AnimalType.DOG,         // animalType
        "사모예드 믹스",          // breed
        AnimalGender.W,   // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "2-3달 추정",           // estimatedAge
        "건강 양호",              // healthCondition
        "흰색 털, 파란 체크무늬 침대에 있는 매우 어린 강아지"  // feature
    ));

    // 12. 얼룩 고양이
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏",   // breed
        AnimalGender.W,   // gender
        AnimalSize.MEDIUM,      // size
        null,                   // imageFile
        "2-3살 추정",            // estimatedAge
        "건강 양호",      // healthCondition
        "태비 무늬, 둥근 얼굴, 경계하는 표정"  // feature
    ));

    // 13. 흑백 턱시도 고양이 (Image 1)
    animals.add(new PetRegisterRequest(
        "쑥이",                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "턱시도 고양이",         // breed
        AnimalGender.W,         // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "3-4개월 추정",          // estimatedAge
        "건강 양호",             // healthCondition
        "얼굴 반은 검정, 반은 흰색인 턱시도 패턴, 호기심 많은 표정"  // feature
    ));

    // 14. 브라운 태비와 삼색 코숏 고양이 두 마리 (Image 2)
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏 믹스",             // breed
        AnimalGender.W,         // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "2-3개월 추정",          // estimatedAge
        "건강 양호",             // healthCondition
        "브라운 태비와 삼색이 나란히 앉아있는 모습, 큰 눈망울이 특징"  // feature
    ));

    // 15. 회색 태비 고양이 (Image 3)
    animals.add(new PetRegisterRequest(
        "마루",                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏 태비",       // breed
        AnimalGender.M,         // gender
        AnimalSize.MEDIUM,      // size
        null,                   // imageFile
        "1-2살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "은회색 줄무늬 털, 캣타워에 앉아있는 모습, 경계하는 눈빛"  // feature
    ));

    // 16. 회색 고양이 (Image 4)
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏",           // breed
        AnimalGender.W,         // gender
        AnimalSize.MEDIUM,      // size
        null,                   // imageFile
        "1-2살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "은회색 털에 흰색 배와 다리, 파란 침구 위에서 카메라를 쳐다보는 모습"  // feature
    ));

    // 17. 흑백 고양이 (Image 5)
    animals.add(new PetRegisterRequest(
        "포포",                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏",           // breed
        AnimalGender.W,         // gender
        AnimalSize.MEDIUM,      // size
        null,                   // imageFile
        "2-3살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "검정 흰색 무늬에 분홍색 침구 위에 앉아있는 고양이, 반쯤 감긴 눈매"  // feature
    ));

    // 18. 노란 고양이 (Image 6)
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏",           // breed
        AnimalGender.M,         // gender
        AnimalSize.MEDIUM,      // size
        null,                   // imageFile
        "3-4살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "진한 황갈색(오렌지) 털, 쓰다듬는 손과 함께 있는 모습, 편안해 보이는 표정"  // feature
    ));

    // 19. 회색 고양이 (Image 7)
    animals.add(new PetRegisterRequest(
        "새벽",                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏 태비",       // breed
        AnimalGender.W,         // gender
        AnimalSize.MEDIUM,      // size
        null,                   // imageFile
        "1-2살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "은회색 줄무늬 털에 배는 흰색, 바닥에 누워있는 모습, 항의하는 듯한 표정"  // feature
    ));

    // 20. 검정 고양이 (Image 8)
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏",           // breed
        AnimalGender.M,         // gender
        AnimalSize.MEDIUM,      // size
        null,                   // imageFile
        "1-3살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "윤기나는 검정 털, 노란 눈, 장난기 가득한 표정으로 누워있는 모습"  // feature
    ));

    // 21. 갈색 태비 고양이 (Image 9)
    animals.add(new PetRegisterRequest(
        "오롯이",                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏 태비",       // breed
        AnimalGender.M,         // gender
        AnimalSize.MEDIUM,      // size
        null,                   // imageFile
        "3-5살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "거대한 녹색 눈, 갈색 태비 무늬, 카메라를 응시하는 정면 얼굴"  // feature
    ));

    // 22. 삼색 고양이 (Image 10)
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "삼색 코숏",             // breed
        AnimalGender.W,         // gender
        AnimalSize.MEDIUM,      // size
        null,                   // imageFile
        "1-2살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "흰색, 검정, 주황색 세 가지 색상의 패턴, 호기심 많아 보이는 표정"  // feature
    ));

    // 23. 흰 고양이 (Image 11)
    animals.add(new PetRegisterRequest(
        "마음이",                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "화이트 코숏",           // breed
        AnimalGender.W,         // gender
        AnimalSize.MEDIUM,      // size
        null,                   // imageFile
        "1-2살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "순백색 털, 연한 눈동자, 침대에 옆으로 누워있는 나른한 모습"  // feature
    ));

    // 24. 흰색과 회색 고양이 (Image 12)
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏",           // breed
        AnimalGender.W,         // gender
        AnimalSize.MEDIUM,      // size
        null,                   // imageFile
        "1-3살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "흰색 털에 회색 얼굴과 귀, 타일 바닥에서 호기심 가득한 표정으로 위를 올려다보는 모습"  // feature
    ));

    // 25. 테디베어 같은 말티푸 강아지 (Image 1)
    animals.add(new PetRegisterRequest(
        "바람",                   // name
        null,                   // registrationNo
        AnimalType.DOG,         // animalType
        "말티푸",               // breed
        AnimalGender.M,         // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "2-3개월 추정",          // estimatedAge
        "건강 양호",             // healthCondition
        "베이지색 곱슬 털, 둥근 얼굴과 검은 코, 귀여운 강아지 눈망울, 작은 체구"  // feature
    ));

    // 26. 하얀 포메라니안 (Image 2)
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.DOG,         // animalType
        "포메라니안",           // breed
        AnimalGender.W,         // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "3-4살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "순백색 털에 체크무늬 옷을 입고 있음, 밝은 표정, 작고 귀여운 혀를 내밀고 있음"  // feature
    ));

    // 27. 갈색 닥스훈트 (Image 3)
    animals.add(new PetRegisterRequest(
        "초승",                   // name
        null,                   // registrationNo
        AnimalType.DOG,         // animalType
        "닥스훈트",             // breed
        AnimalGender.M,         // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "2-3살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "갈색과 검정색 무늬, 흰색 옷을 입고 있음, 눈이 쳐져있는 표정, 눈동자가 짙은 갈색"  // feature
    ));

    // 28. 진돗개 (Image 4)
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.DOG,         // animalType
        "진돗개",               // breed
        AnimalGender.M,         // gender
        AnimalSize.MEDIUM,      // size
        null,                   // imageFile
        "2-3살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "주황빛이 도는 황갈색 털, 뾰족한 귀, 컬 형태의 꼬리, 훈련된 듯한 자세"  // feature
    ));

    // 29. 하얀 진돗개 (Image 5)
    animals.add(new PetRegisterRequest(
        "쏠",                   // name
        null,                   // registrationNo
        AnimalType.DOG,         // animalType
        "진돗개",               // breed
        AnimalGender.W,         // gender
        AnimalSize.MEDIUM,      // size
        null,                   // imageFile
        "2-3살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "순백색 털, 뾰족한 귀, 밝은 표정으로 혀를 내밀고 있음, 친근한 인상"  // feature
    ));

    // 30. 크림색 믹스견 (Image 6)
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.DOG,         // animalType
        "믹스견",               // breed
        AnimalGender.M,         // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "2-4살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "크림색 털, 작은 귀, 이빨이 살짝 보이는 표정, 전체적으로 둥근 인상"  // feature
    ));

    // 31. 래브라도 리트리버 (Image 7)
    animals.add(new PetRegisterRequest(
        "파랑",                   // name
        null,                   // registrationNo
        AnimalType.DOG,         // animalType
        "래브라도 리트리버",     // breed
        AnimalGender.M,         // gender
        AnimalSize.LARGE,       // size
        null,                   // imageFile
        "1-2살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "황금빛 털, 체크무늬 조끼를 입고 있음, 온순한 표정, 짙은 갈색 눈동자"  // feature
    ));

    // 32. 갈색 믹스견 (Image 8)
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.DOG,         // animalType
        "믹스견",               // breed
        AnimalGender.W,         // gender
        AnimalSize.MEDIUM,      // size
        null,                   // imageFile
        "2-3살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "황갈색 털, 혀를 내밀고 있는 활기찬 표정, 짧은 털, 귀여운 표정"  // feature
    ));

    // 33. 크림색 고양이 (Image 9)
    animals.add(new PetRegisterRequest(
        "코코아",                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏",           // breed
        AnimalGender.W,         // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "1-2개월 추정",          // estimatedAge
        "건강 양호",             // healthCondition
        "크림색 털에 주황색 무늬가 있는 아기 고양이, 분홍색 담요에 싸여 있음, 큰 귀와 호기심 많은 눈"  // feature
    ));

    // 34. 하얀 고양이 (Image 10)
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏",           // breed
        AnimalGender.W,         // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "3-4개월 추정",          // estimatedAge
        "건강 양호",             // healthCondition
        "하얀 털에 노란색과 파란색 줄무늬 담요 위에 앉아있는 모습, 동그란 눈"  // feature
    ));

    // 35. 하얀 고양이 (Image 11)
    animals.add(new PetRegisterRequest(
        "가온",                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏",           // breed
        AnimalGender.W,         // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "2-3개월 추정",          // estimatedAge
        "건강 양호",             // healthCondition
        "하얀 털에 파란 눈, 회색 방석 위에 앉아있는 모습, 귀여운 아기 고양이 얼굴"  // feature
    ));

    // 36. 회색 줄무늬 고양이 (Image 12)
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏 태비",      // breed
        AnimalGender.W,         // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "2-3개월 추정",          // estimatedAge
        "건강 양호",             // healthCondition
        "회색 줄무늬와 하얀색 무늬가 섞인 태비 고양이, 탐험적인 표정, 작은 체구"  // feature
    ));

    // 37. 크림색과 하얀색 고양이
    animals.add(new PetRegisterRequest(
        "가온",                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏",           // breed
        AnimalGender.W,         // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "3-4개월 추정",          // estimatedAge
        "건강 양호",             // healthCondition
        "크림색과 흰색이 섞인 털, 베이지색 카펫 위에 앉아있는 모습, 호기심 많은 표정"  // feature
    ));

    // 38. 주황색 고양이
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "코숏",           // breed
        AnimalGender.M,         // gender
        AnimalSize.MEDIUM,      // size
        null,                   // imageFile
        "1-2살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "진한 주황색 장모, 하얀 침대 위에 누워있는 모습, 나른한 표정, 복슬복슬한 털"  // feature
    ));

    // 39. 하얀 포메라니안
    animals.add(new PetRegisterRequest(
        "새봄",                   // name
        null,                   // registrationNo
        AnimalType.DOG,         // animalType
        "포메라니안",           // breed
        AnimalGender.W,         // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "2-3살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "순백색 털, 혀를 내밀고 있는 활기찬 표정, 검은 눈과 코, 밝고 친근한 모습"  // feature
    ));

    // 40. 하얀 포메라니안
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.DOG,         // animalType
        "포메라니안",           // breed
        AnimalGender.W,         // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "1-2살 추정",            // estimatedAge
        "건강 양호",             // healthCondition
        "순백색 털, 활짝 웃는 듯한 표정, 작은 체구, 곧게 선 귀, 사랑스러운 모습"  // feature
    ));

    return animals;
  }

  /**
   * 샘플 동물 이미지에 대한 가상 URL을 반환합니다.
   * 실제 구현에서는 이미지 처리 로직에 맞게 수정해야 합니다.
   * @return 12마리 동물의 이미지 URL 리스트
   */
  public static List<String> getSampleImageUrls() {
    List<String> imageUrls = new ArrayList<>();

    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample07.jpeg");
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample08.jpg");
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample09.jpg");
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample10.jpg");
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample11.jpg");
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample12.jpg");
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample01.jpg");
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample02.jpg");
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample03.jpg");
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample04.jpg");
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample05.jpeg");
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample06.jpeg");

    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample24.jpg");  // 흑백 턱시도 고양이
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample13.jpg");  // 브라운 태비와 삼색 코숏 고양이 두 마리
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample14.jpg");  // 회색 태비 고양이
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample15.jpg");  // 회색 고양이
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample16.jpg");  // 흑백 고양이
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample17.jpg");  // 노란 고양이
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample18.jpg");  // 회색 고양이
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample19.jpg");  // 검정 고양이
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample20.jpg");  // 갈색 태비 고양이
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample21.jpg");  // 삼색 고양이
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample22.jpg");  // 흰 고양이
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample23.jpg");  // 흰색과 회색 고양이
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample33.jpg");  // 테디베어 같은 말티푸 강아지
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample34.jpg");  // 하얀 포메라니안
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample35.jpg");  // 갈색 닥스훈트
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample36.jpg");  // 진돗개
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample37.jpg");  // 하얀 진돗개
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample38.jpg");  // 크림색 믹스견
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample39.jpg");  // 래브라도 리트리버
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample40.jpg");  // 갈색 믹스견
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample25.jpg");  // 크림색 고양이
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample26.jpg");  // 하얀 고양이
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample27.jpg");  // 하얀 고양이
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample28.jpg");  // 회색 줄무늬 고양이
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample29.jpg");  // 크림색과 하얀색 고양이
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample30.jpg");  // 주황색 고양이
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample31.jpg");  // 하얀 포메라니안
    imageUrls.add("https://kr.object.ncloudstorage.com/paw-patrol/protection/sample32.jpg");  // 하얀 포메라니안
    return imageUrls;
  }

  public static List<String> getSampleTitles() {
    List<String> protectionTitles = new ArrayList<>();

    protectionTitles.add("반짝이는 눈망울이 매력적인 '밤이'");
    protectionTitles.add("따뜻한 가족을 기다리는 작은 천사");
    protectionTitles.add("달콤한 미소를 가진 사랑둥이 '단이'");
    protectionTitles.add("귀여운 털뭉치가 가족을 기다리고 있어요");
    protectionTitles.add("작은 몸에서 넘치는 에너지를 가진 '콩콩이'");
    protectionTitles.add("천진난만한 눈빛이 사랑스러운 강아지");
    protectionTitles.add("포근한 털이 마치 구름 같은 '구름이'");
    protectionTitles.add("장난기 가득한 눈빛을 가진 귀여운 친구");
    protectionTitles.add("말랑말랑한 매력의 '두부'");
    protectionTitles.add("하얀 솜뭉치 같은 사랑스러운 아이");
    protectionTitles.add("반짝이는 눈이 매력적인 '보석이'");
    protectionTitles.add("호기심 많은 고양이를 만나보세요");
    protectionTitles.add("사람 곁이 좋은 다정한 '쑥이'");
    protectionTitles.add("작지만 당찬 성격의 사랑스러운 친구");
    protectionTitles.add("밝고 건강한 에너지를 가진 '마루'");
    protectionTitles.add("호기심으로 가득 찬 귀여운 꼬마 강아지");
    protectionTitles.add("장난꾸러기지만 애교 많은 '포포'");
    protectionTitles.add("사랑받을 준비가 된 작고 소중한 존재");
    protectionTitles.add("맑고 깊은 눈빛이 매력적인 '새벽'");
    protectionTitles.add("따뜻한 햇살 같은 미소를 가진 강아지");
    protectionTitles.add("온전히 사랑받을 자격이 있는 '오롯이'");
    protectionTitles.add("애교 넘치는 작은 털뭉치와 함께하세요");
    protectionTitles.add("사랑을 전하는 따뜻한 존재, '마음이'");
    protectionTitles.add("포근한 품을 기다리는 사랑스러운 강아지");
    protectionTitles.add("자유롭고 호기심 넘치는 '바람이'");
    protectionTitles.add("활발하고 귀여운 고양이를 만나보세요");
    protectionTitles.add("부드러운 달빛 같은 '초승이'");
    protectionTitles.add("눈부신 털빛이 매력적인 친구");
    protectionTitles.add("햇살처럼 따뜻한 '쏠이'");
    protectionTitles.add("포근한 가족을 기다리는 사랑스러운 아이");
    protectionTitles.add("맑고 깊은 눈빛의 '파랑이'");
    protectionTitles.add("하얀 눈처럼 깨끗한 마음을 가진 강아지");
    protectionTitles.add("달달한 매력을 가진 '코코아'");
    protectionTitles.add("따뜻한 가족을 기다리는 초코빛 친구");
    protectionTitles.add("언제나 중심이 되어줄 따뜻한 '가온이'");
    protectionTitles.add("귀여운 외모와 밝은 성격을 가진 아이");
    protectionTitles.add("따스한 봄처럼 다가오는 '새봄이'");
    protectionTitles.add("온화한 성격이 돋보이는 사랑스러운 친구");
    protectionTitles.add("밝은 햇살처럼 따뜻한 '해온이'");
    protectionTitles.add("반짝이는 눈망울이 매력적인 작은 천사");

    return protectionTitles;
  }

  public static List<String> getAnimalNameList() {
    List<String> animalNameList = new ArrayList<>();

    animalNameList.add("밤이");
    animalNameList.add(null);
    animalNameList.add("단이");
    animalNameList.add(null);
    animalNameList.add("콩콩이");
    animalNameList.add(null);
    animalNameList.add("구름이");
    animalNameList.add(null);
    animalNameList.add("두부");
    animalNameList.add(null);
    animalNameList.add("보석이");
    animalNameList.add(null);
    animalNameList.add("쑥이");
    animalNameList.add(null);
    animalNameList.add("마루");
    animalNameList.add(null);
    animalNameList.add("포포");
    animalNameList.add(null);
    animalNameList.add("새벽");
    animalNameList.add(null);
    animalNameList.add("오롯이");
    animalNameList.add(null);
    animalNameList.add("마음이");
    animalNameList.add(null);
    animalNameList.add("바람이");
    animalNameList.add(null);
    animalNameList.add("초승이");
    animalNameList.add(null);
    animalNameList.add("쏠이");
    animalNameList.add(null);
    animalNameList.add("파랑이");
    animalNameList.add(null);
    animalNameList.add("코코아");
    animalNameList.add(null);
    animalNameList.add("가온이");
    animalNameList.add(null);
    animalNameList.add("새봄이");
    animalNameList.add(null);
    animalNameList.add("해온이");
    animalNameList.add(null);

    return animalNameList;
  }
}
