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
        SignupRequest request1 = new SignupRequest("test1@test.com", "1234", "강남");
        SignupRequest request2 = new SignupRequest("test2@test.com", "1234", "홍길동");
        SignupRequest request3 = new SignupRequest("test3@test.com", "1234", "제펫토");

        Member member1 = authService.signUp(request1);
        Member member2 = authService.signUp(request2);
        Member member3 = authService.signUp(request3);

        List<PetRegisterRequest> sampleAnimals = SampleAnimalData.getSampleStrayAnimals();
        List<String> imageUrls = SampleAnimalData.getSampleImageUrls();

        // 생성된 동물들을 저장할 리스트
        List<Animal> animalList = new ArrayList<>();

        // 동물 생성 (이름 변경 없이)
        int animalCount = Math.min(12, sampleAnimals.size());
        for (int i = 0; i < animalCount; i++) {
          PetRegisterRequest animalRequest = sampleAnimals.get(i);
          String imageUrl = imageUrls.get(i);

          Animal animal = animalService.registerWithImageUrl(animalRequest, imageUrl);
          animalList.add(animal);

          System.out.println("샘플 데이터 생성: " + animal.getName() +
              " - " + animal.getBreed() + " (" + animal.getImageUrl() + ")");
        }

        // animal1, animal2, ... 변수에 할당
        Animal animal1 = animalList.size() > 0 ? animalList.get(0) : null;
        Animal animal2 = animalList.size() > 1 ? animalList.get(1) : null;
        Animal animal3 = animalList.size() > 2 ? animalList.get(2) : null;
        Animal animal4 = animalList.size() > 3 ? animalList.get(3) : null;
        Animal animal5 = animalList.size() > 4 ? animalList.get(4) : null;
        Animal animal6 = animalList.size() > 5 ? animalList.get(5) : null;
        Animal animal7 = animalList.size() > 6 ? animalList.get(6) : null;
        Animal animal8 = animalList.size() > 7 ? animalList.get(7) : null;
        Animal animal9 = animalList.size() > 8 ? animalList.get(8) : null;
        Animal animal10 = animalList.size() > 9 ? animalList.get(9) : null;
        Animal animal11 = animalList.size() > 10 ? animalList.get(10) : null;
        Animal animal12 = animalList.size() > 11 ? animalList.get(11) : null;


        animalCaseEventPublisher.createAnimalCase(member1, animal1);
        animalCaseEventPublisher.createAnimalCase(member1, animal2);
        animalCaseEventPublisher.createAnimalCase(member1, animal3);
        animalCaseEventPublisher.createAnimalCase(member1, animal4);
        animalCaseEventPublisher.createAnimalCase(member1, animal5);
        animalCaseEventPublisher.createAnimalCase(member1, animal6);


      }
    };
  }
}
