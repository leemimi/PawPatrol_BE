//package com.patrol.global.initData;
//
//
//import com.patrol.domain.member.auth.service.AuthService;
//import com.patrol.domain.member.member.entity.Member;
//import com.patrol.domain.member.member.enums.ProviderType;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.transaction.annotation.Transactional;
//
//@Configuration
//@Profile("!prod")
//public class NotProd {
//  @Bean
//  public ApplicationRunner applicationRunner(AuthService authService) {
//    return new ApplicationRunner() {
//      @Transactional
//      @Override
//      public void run(ApplicationArguments args) throws Exception {
//        // Member1,2,3 생성
//
//        Member member1 =
//            authService.signup("test1@test.com", "1234", "강남", ProviderType.SELF, null, null, null);
//        Member member2 =
//            authService.signup("test2@test.com", "1234", "홍길동", ProviderType.SELF, null, null, null);
//        Member member3 =
//            authService.signup("test3@test.com", "1234", "제펫토", ProviderType.SELF, null, null, null);
//
//      }
//    };
//  }
//}
