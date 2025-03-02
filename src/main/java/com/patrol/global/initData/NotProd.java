package com.patrol.global.initData;


import com.patrol.api.member.auth.dto.request.SignupRequest;
import com.patrol.api.member.member.dto.request.PetRegisterRequest;
import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.service.AnimalService;
import com.patrol.domain.animalCase.service.AnimalCaseEventPublisher;
import com.patrol.domain.member.auth.service.AuthService;
import com.patrol.domain.member.auth.service.V2AuthService;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.enums.ProviderType;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile("!prod")
public class NotProd {
  @Bean
  public ApplicationRunner applicationRunner(
      V2AuthService authService,
      AnimalService animalService,
      AnimalCaseEventPublisher animalCaseEventPublisher

  ) {
    return new ApplicationRunner() {
      @Transactional
      @Override
      public void run(ApplicationArguments args) throws Exception {
        // Member1,2,3 생성
        // Member1,2,3 생성
        SignupRequest request1 = new SignupRequest("test1@test.com", "1234", "강남", "서울");
        SignupRequest request2 = new SignupRequest("test2@test.com", "1234", "홍길동", "경기도");
        SignupRequest request3 = new SignupRequest("test3@test.com", "1234", "제펫토", "강원도");

        Member member1 = authService.signUp(request1);
        Member member2 = authService.signUp(request2);
        Member member3 = authService.signUp(request3);

        List<PetRegisterRequest> sampleAnimals = SampleAnimalData.getSampleStrayAnimals();
        List<String> imageUrls = SampleAnimalData.getSampleImageUrls();
        List<String> animalNameList = SampleAnimalData.getAnimalNameList();

        // 생성된 동물들을 저장할 리스트
        List<Animal> animalList = new ArrayList<>();

        // 동물 생성 (40마리)
        int animalCount = Math.min(40, sampleAnimals.size());
        for (int i = 0; i < animalCount; i++) {
          PetRegisterRequest animalRequest = sampleAnimals.get(i);
          String imageUrl = imageUrls.get(i);
          String name = animalNameList.get(i);

          Animal animal = animalService.registerWithImageUrl(animalRequest, imageUrl);
          animal.setName(name);
          animalList.add(animal);

          System.out.println("샘플 데이터 생성: " + animal.getName() +
              " - " + animal.getBreed() + " (" + animal.getImageUrl() + ")");
        }

        // 임시보호용 게시글 타이틀 가져오기
        List<String> protectionTitles = SampleAnimalData.getSampleTitles();


        // member1 - 20마리
        int casesForMember1 = Math.min(20, animalList.size());
        for (int i = 0; i < casesForMember1; i++) {
          animalCaseEventPublisher.createAnimalCase(
              member1, animalList.get(i), protectionTitles.get(i), null
          );
          System.out.println("Member1의 동물 케이스 생성: " + animalList.get(i).getBreed());
        }

        // member2 - 10마리
        int casesForMember2 = Math.min(10, animalList.size() - casesForMember1);
        for (int i = 0; i < casesForMember2; i++) {
          int index = casesForMember1 + i;
          if (index < animalList.size()) {
            animalCaseEventPublisher.createAnimalCase(
                member2, animalList.get(index), protectionTitles.get(index), null
            );
            System.out.println("Member2의 동물 케이스 생성: " + animalList.get(index).getBreed());
          }
        }

        // member3 - 10마리
        int startIndexForMember3 = casesForMember1 + casesForMember2;
        int casesForMember3 = Math.min(10, animalList.size() - startIndexForMember3);
        for (int i = 0; i < casesForMember3; i++) {
          int index = startIndexForMember3 + i;
          if (index < animalList.size()) {
            animalCaseEventPublisher.createAnimalCase(
                member3, animalList.get(index), protectionTitles.get(index), null
            );
            System.out.println("Member3의 동물 케이스 생성: " + animalList.get(index).getBreed());
          }
        }
      }
    };
  }
}
