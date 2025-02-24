package com.patrol.global.initData;


import com.patrol.api.member.auth.dto.request.SignupRequest;
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

@Configuration
@Profile("!prod")
public class NotProd {
  @Bean
  public ApplicationRunner applicationRunner(V2AuthService authService) {
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
      }
    };
  }
}
