package com.patrol.global.initData;


import com.patrol.api.LostPost.dto.LostPostRequestDto;
import com.patrol.api.findPost.dto.FindPostRequestDto;
import com.patrol.domain.LostPost.entity.LostPost;
import com.patrol.domain.LostPost.repository.LostPostRepository;
import com.patrol.domain.findPost.entity.FindPost;
import com.patrol.domain.findPost.repository.FindPostRepository;
import com.patrol.domain.member.auth.service.AuthService;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Configuration
@Profile("!prod")
@RequiredArgsConstructor
public class NotProd {

  private static final String[] SEOUL_LOCATIONS = {
          "강남구", "서초구", "송파구", "강동구", "마포구", "서대문구", "은평구", "종로구",
          "중구", "용산구", "성동구", "광진구", "동대문구", "중랑구", "성북구", "강북구",
          "도봉구", "노원구", "양천구", "강서구", "구로구", "금천구", "영등포구", "동작구", "관악구"
  };

  private static final String[] DOG_BREEDS = {
          "포메라니안", "말티즈", "시바견", "비숑", "푸들", "치와와", "골든리트리버",
          "진돗개", "웰시코기", "도베르만", "시츄", "요크셔테리어", "닥스훈트", "보더콜리",
          "래브라도리트리버", "사모예드", "시베리안허스키", "베들링턴테리어", "차우차우"
  };

  private static final String[] DOG_CHARACTERISTICS = {
          "온순한 성격", "활발한 성격", "겁이 많음", "사교적임", "호기심이 많음",
          "사람을 잘 따름", "귀가 쫑긋함", "꼬리가 말림", "배에 하얀 무늬", "목에 검은 반점",
          "앞발에 흰색 양말", "짧은 꼬리", "긴 털", "짧은 털", "곱슬머리",
          "귀가 접혀있음", "목줄 착용", "방울 목걸이 착용", "빨간 하네스 착용"
  };

  private static final String[] COLOR_TAGS = {
          "흰색", "검정", "갈색", "크림색", "베이지", "진갈색", "블랙앤탄", "회색", "삼색"
  };

  @Bean
  public ApplicationRunner applicationRunner(
          AuthService authService,
          LostPostRepository lostPostRepository,
          FindPostRepository findPostRepository) {
    return new ApplicationRunner() {
      @Transactional
      @Override
      public void run(ApplicationArguments args) throws Exception {
        Random random = new Random();

        // Create Members
        Member member1 = authService.signup("test1@test.com", "1234", "강남", ProviderType.SELF, null, null, null);
        Member member2 = authService.signup("test2@test.com", "1234", "홍길동", ProviderType.SELF, null, null, null);
        Member member3 = authService.signup("test3@test.com", "1234", "제펫토", ProviderType.SELF, null, null, null);

        List<LostPost> lostPosts = new ArrayList<>();

        // Create 50 LostPosts
        for (int i = 0; i < 50; i++) {
          String location = SEOUL_LOCATIONS[random.nextInt(SEOUL_LOCATIONS.length)];
          String breed = DOG_BREEDS[random.nextInt(DOG_BREEDS.length)];

          // Generate random coordinates within Seoul
          double baseLat = 37.5665; // Seoul center latitude
          double baseLong = 126.9780; // Seoul center longitude
          double latitude = baseLat + (random.nextDouble() - 0.5) * 0.2;  // +/- 0.1 degree
          double longitude = baseLong + (random.nextDouble() - 0.5) * 0.2;

          String colorTag = COLOR_TAGS[random.nextInt(COLOR_TAGS.length)];
          List<String> tags = Arrays.asList(breed, colorTag, location);

          LostPostRequestDto lostPostDto = LostPostRequestDto.builder()
                  .title(String.format("%s를 찾습니다 - %s", breed, location))
                  .content(String.format("%s에서 잃어버린 %s %s를 찾고 있습니다. 발견하시면 연락 부탁드립니다.",
                          location, colorTag, breed))
                  .location("서울특별시 " + location)
                  .latitude(latitude)
                  .longitude(longitude)
                  .ownerPhone(String.format("010-%04d-%04d",
                          random.nextInt(10000), random.nextInt(10000)))
                  .tags(tags)
                  .lostTime(LocalDateTime.now()
                          .minusDays(random.nextInt(30))
                          .minusHours(random.nextInt(24))
                          .toString())
                  .status(random.nextInt(10) < 8 ? "FINDING" :
                          random.nextInt(10) < 5 ? "FOSTERING" : "FOUND")
                  .build();

          LostPost lostPost = new LostPost(lostPostDto);
          lostPost.setMemberId(random.nextInt(2) == 0 ?
                  member1.getId() : member2.getId());
          lostPost.setPetId((long) (i + 1));

          lostPosts.add(lostPostRepository.save(lostPost));
        }

        // Create 50 FindPosts
        for (int i = 0; i < 50; i++) {
          String location = SEOUL_LOCATIONS[random.nextInt(SEOUL_LOCATIONS.length)];
          String breed = DOG_BREEDS[random.nextInt(DOG_BREEDS.length)];

          // Generate random coordinates within Seoul
          double baseLat = 37.5665;
          double baseLong = 126.9780;
          double latitude = baseLat + (random.nextDouble() - 0.5) * 0.2;
          double longitude = baseLong + (random.nextDouble() - 0.5) * 0.2;

          String colorTag = COLOR_TAGS[random.nextInt(COLOR_TAGS.length)];
          String characteristic = DOG_CHARACTERISTICS[random.nextInt(DOG_CHARACTERISTICS.length)];
          List<String> tags = Arrays.asList(breed, colorTag, location);

          FindPostRequestDto findPostDto = FindPostRequestDto.builder()
                  .title(String.format("%s 발견 - %s", breed, location))
                  .content(String.format("%s에서 %s %s를 발견했습니다. %s",
                          location, colorTag, breed, characteristic))
                  .location("서울특별시 " + location)
                  .latitude(latitude)
                  .longitude(longitude)
                  .findTime(LocalDateTime.now()
                          .minusDays(random.nextInt(30))
                          .minusHours(random.nextInt(24))
                          .toString())
                  .tags(tags)
                  .status(random.nextInt(10) < 8 ? "FINDING" :
                          random.nextInt(10) < 5 ? "FOSTERING" : "FOUND")
                  .birthDate(LocalDate.now().minusYears(random.nextInt(10) + 1)
                          .minusMonths(random.nextInt(12)))
                  .breed(breed)
                  .name(random.nextInt(2) == 0 ? "미상" : "임시보호" + (i + 1))
                  .characteristics(characteristic)
                  .size(breed.equals("치와와") || breed.equals("요크셔테리어") || breed.equals("포메라니안") ? "SMALL" :
                          breed.equals("골든리트리버") || breed.equals("도베르만") ? "LARGE" : "MEDIUM")
                  .gender(random.nextBoolean() ? "MALE" : "FEMALE")
                  .build();

          // 20% chance to link with a LostPost
          LostPost linkedPost = random.nextInt(5) == 0 && !lostPosts.isEmpty() ?
                  lostPosts.get(random.nextInt(lostPosts.size())) : null;

          FindPost findPost = new FindPost(findPostDto, linkedPost, member3.getId());
          findPostRepository.save(findPost);
        }
      }
    };
  }
}
