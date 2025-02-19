package com.patrol.global.oauth2;




import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.service.MemberService;
import com.patrol.global.exceptions.ErrorCode;
import com.patrol.global.exceptions.ServiceException;
import com.patrol.global.rq.Rq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  private final MemberService memberService;
  private final Rq rq;


  @SneakyThrows
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    Member actor = memberService.findById(rq.getActor().getId()).get();

    rq.makeAuthCookies(actor);

    String redirectUrl = request.getParameter("state");
    if (redirectUrl != null && !redirectUrl.isEmpty()) {
      System.out.println("Redirecting to: " + redirectUrl);
    } else {
      throw new ServiceException(ErrorCode.REDIRECT_URL_NOT_FOUND);
    }

    response.sendRedirect(redirectUrl);
  }
}

