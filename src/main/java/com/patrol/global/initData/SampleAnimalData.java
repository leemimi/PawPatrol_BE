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
        null,                   // name (no name as stray)
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
        "국내 단모종 태비", // breed
        AnimalGender.W,   // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "2-3달 추정",           // estimatedAge
        "건강 양호",              // healthCondition
        "갈색 태비 무늬에 밝은 파란 눈, 매우 어린 나이"  // feature
    ));

    // 3. 깔때기 착용한 고양이
    animals.add(new PetRegisterRequest(
        null,                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "국내 단모종",   // breed
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
        "국내 단모종 태비", // breed
        AnimalGender.W,   // gender
        AnimalSize.SMALL,       // size
        null,                   // imageFile
        "3-4달 추정",           // estimatedAge
        "건강함",              // healthCondition
        "갈색 태비 무늬의 겁이 많은 착한 아이"  // feature
    ));

    // 5. 흰색 옷 입은 치와와
    animals.add(new PetRegisterRequest(
        null,                   // name
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
        null,                   // name
        null,                   // registrationNo
        AnimalType.CAT,         // animalType
        "국내 단모종 태비", // breed
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
        null,                   // name
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
        null,                   // name
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
        "국내 단모종",   // breed
        AnimalGender.W,   // gender
        AnimalSize.MEDIUM,      // size
        null,                   // imageFile
        "2-3살 추정",            // estimatedAge
        "건강 양호",      // healthCondition
        "태비 무늬, 둥근 얼굴, 경계하는 표정"  // feature
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

    imageUrls.add("sample07.jpeg");
    imageUrls.add("sample08.jpg");
    imageUrls.add("sample09.jpg");
    imageUrls.add("sample10.jpg");
    imageUrls.add("sample11.jpg");
    imageUrls.add("sample12.jpg");
    imageUrls.add("sample01.jpg");
    imageUrls.add("sample02.jpg");
    imageUrls.add("sample03.jpg");
    imageUrls.add("sample04.jpg");
    imageUrls.add("sample05.jpeg");
    imageUrls.add("sample06.jpeg");

    return imageUrls;
  }
}
