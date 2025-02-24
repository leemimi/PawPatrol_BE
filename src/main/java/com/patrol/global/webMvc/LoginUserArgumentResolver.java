package com.patrol.global.webMvc;



import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.service.MemberService;
import com.patrol.global.exceptions.ErrorCodes;
import com.patrol.global.exceptions.ServiceException;
import com.patrol.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
  // @LoginUser 어노테이션의 실제 동작을 처리하는 클래스

  private final MemberService memberService;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(LoginUser.class) &&
        parameter.getParameterType().equals(Member.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory
  ) {

    // 1. 현재 인증 정보 확인
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new ServiceException(ErrorCodes.UNAUTHORIZED);
    }

    // 2. SecurityUser에서 이메일 추출
    SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
    String email = securityUser.getName();

    // 3. 이메일로 회원 정보 조회
    return memberService.findByEmail(email);
  }
}
