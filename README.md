# PawPatrol_BE
벡엔드 입니다
```
PawPatrol_BE
└─ src
   ├─ main
   │  ├─ java
   │  │  └─ com
   │  │     └─ patrol
   │  │        ├─ api
   │  │        │  ├─ member
   │  │        │  │  ├─ auth
   │  │        │  │  │  ├─ controller
   │  │        │  │  │  │  └─ ApiV1AuthController.java
   │  │        │  │  │  └─ dto
   │  │        │  │  │     ├─ EmailResponse.java
   │  │        │  │  │     ├─ FindEmailsResponse.java
   │  │        │  │  │     ├─ LoginUserDto.java
   │  │        │  │  │     ├─ request
   │  │        │  │  │     │  ├─ EmailRequest.java
   │  │        │  │  │     │  ├─ EmailVerifyRequest.java
   │  │        │  │  │     │  ├─ FindEmailsRequest.java
   │  │        │  │  │     │  ├─ PasswordResetRequest.java
   │  │        │  │  │     │  ├─ PasswordResetVerifyRequest.java
   │  │        │  │  │     │  ├─ PhoneNumberRequest.java
   │  │        │  │  │     │  ├─ PhoneVerificationRequest.java
   │  │        │  │  │     │  ├─ SetNewPasswordRequest.java
   │  │        │  │  │     │  └─ SignupRequest.java
   │  │        │  │  │     └─ SignupResponse.java
   │  │        │  │  └─ member
   │  │        │  │     ├─ controller
   │  │        │  │     │  └─ ApiV1MemberController.java
   │  │        │  │     └─ dto
   │  │        │  │        ├─ AddressResponse.java
   │  │        │  │        ├─ JusoApiResponse.java
   │  │        │  │        ├─ MemberDto.java
   │  │        │  │        ├─ MemberUpdateResponse.java
   │  │        │  │        ├─ OAuthProviderStatus.java
   │  │        │  │        └─ request
   │  │        │  │           ├─ AddPasswordRequest.java
   │  │        │  │           ├─ ChangePasswordRequest.java
   │  │        │  │           ├─ CheckPasswordRequest.java
   │  │        │  │           └─ MemberUpdateRequest.java
   │  │        │  └─ protection
   │  │        │     └─ facility
   │  │        │        ├─ controller
   │  │        │        │  └─ ApiV1FacilityController.java
   │  │        │        └─ dto
   │  │        │           └─ ShelterApiResponse.java
   │  │        ├─ domain
   │  │        │  ├─ member
   │  │        │  │  ├─ auth
   │  │        │  │  │  ├─ config
   │  │        │  │  │  │  ├─ AsyncConfig.java
   │  │        │  │  │  │  ├─ EmailConfig.java
   │  │        │  │  │  │  └─ SmsConfig.java
   │  │        │  │  │  ├─ entity
   │  │        │  │  │  │  ├─ BaseOAuthProvider.java
   │  │        │  │  │  │  ├─ GithubProvider.java
   │  │        │  │  │  │  ├─ GoogleProvider.java
   │  │        │  │  │  │  ├─ KakaoProvider.java
   │  │        │  │  │  │  ├─ NaverProvider.java
   │  │        │  │  │  │  └─ OAuthProvider.java
   │  │        │  │  │  ├─ repository
   │  │        │  │  │  │  └─ OAuthProviderRepository.java
   │  │        │  │  │  ├─ service
   │  │        │  │  │  │  ├─ AuthService.java
   │  │        │  │  │  │  ├─ AuthTokenService.java
   │  │        │  │  │  │  ├─ EmailService.java
   │  │        │  │  │  │  ├─ OAuthService.java
   │  │        │  │  │  │  ├─ PhoneVerificationService.java
   │  │        │  │  │  │  └─ SmsService.java
   │  │        │  │  │  └─ strategy
   │  │        │  │  │     ├─ GithubProviderStrategy.java
   │  │        │  │  │     ├─ GoogleProviderStrategy.java
   │  │        │  │  │     ├─ KakaoProviderStrategy.java
   │  │        │  │  │     ├─ NaverProviderStrategy.java
   │  │        │  │  │     └─ OAuthProviderStrategy.java
   │  │        │  │  └─ member
   │  │        │  │     ├─ entity
   │  │        │  │     │  └─ Member.java
   │  │        │  │     ├─ enums
   │  │        │  │     │  ├─ Gender.java
   │  │        │  │     │  ├─ MemberRole.java
   │  │        │  │     │  ├─ MemberStatus.java
   │  │        │  │     │  └─ ProviderType.java
   │  │        │  │     ├─ repository
   │  │        │  │     │  └─ MemberRepository.java
   │  │        │  │     └─ service
   │  │        │  │        ├─ MemberService.java
   │  │        │  │        ├─ PasswordService.java
   │  │        │  │        └─ SocialConnectService.java
   │  │        │  └─ protection
   │  │        │     └─ facility
   │  │        │        ├─ config
   │  │        │        │  └─ MapperConfig.java
   │  │        │        ├─ entity
   │  │        │        │  ├─ Facility.java
   │  │        │        │  ├─ Hospital.java
   │  │        │        │  ├─ OperatingHours.java
   │  │        │        │  └─ Shelter.java
   │  │        │        ├─ repository
   │  │        │        │  ├─ HospitalRepository.java
   │  │        │        │  └─ ShelterRepository.java
   │  │        │        ├─ scheduler
   │  │        │        │  └─ FetchShelterScheduler.java
   │  │        │        └─ service
   │  │        │           ├─ CsvParser.java
   │  │        │           └─ ShelterService.java
   │  │        ├─ global
   │  │        │  ├─ app
   │  │        │  │  └─ AppConfig.java
   │  │        │  ├─ error
   │  │        │  │  ├─ ErrorCode.java
   │  │        │  │  └─ ErrorResponse.java
   │  │        │  ├─ exceptions
   │  │        │  │  ├─ ErrorCodes.java
   │  │        │  │  ├─ GlobalExceptionHandler.java
   │  │        │  │  └─ ServiceException.java
   │  │        │  ├─ globalDto
   │  │        │  │  ├─ GlobalResponse.java
   │  │        │  │  └─ GlobalResponseCode.java
   │  │        │  ├─ initData
   │  │        │  │  └─ NotProd.java
   │  │        │  ├─ jpa
   │  │        │  │  └─ BaseEntity.java
   │  │        │  ├─ oauth2
   │  │        │  │  ├─ CustomAuthorizationRequestResolver.java
   │  │        │  │  ├─ CustomOAuth2AuthenticationSuccessHandler.java
   │  │        │  │  └─ CustomOAuth2UserService.java
   │  │        │  ├─ redis
   │  │        │  │  └─ RedisConfig.java
   │  │        │  ├─ rq
   │  │        │  │  └─ Rq.java
   │  │        │  ├─ rsData
   │  │        │  │  └─ RsData.java
   │  │        │  ├─ security
   │  │        │  │  ├─ ApiSecurityConfig.java
   │  │        │  │  ├─ CustomAuthenticationFilter.java
   │  │        │  │  ├─ SecurityConfig.java
   │  │        │  │  └─ SecurityUser.java
   │  │        │  └─ webMvc
   │  │        │     ├─ CustomWebMvcConfig.java
   │  │        │     ├─ LoginUser.java
   │  │        │     └─ LoginUserArgumentResolver.java
   │  │        ├─ PawpatrolApplication.java
   │  │        └─ standard
   │  │           ├─ base
   │  │           │  └─ Empty.java
   │  │           └─ util
   │  │              └─ Ut.java
   │  └─ resources
   │     ├─ application-dev.yml
   │     ├─ application-prod.yml
   │     ├─ application-test.yml
   │     └─ application.yml
   └─ test
      └─ java
         └─ com
            └─ patrol
               └─ PawpatrolApplicationTests.java

```
