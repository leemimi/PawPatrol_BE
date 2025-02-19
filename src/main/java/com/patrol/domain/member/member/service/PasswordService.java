package com.patrol.domain.member.member.service;



import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.repository.MemberRepository;
import com.patrol.global.exceptions.ErrorCode;
import com.patrol.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordService {

  private final PasswordEncoder passwordEncoder;
  private final MemberRepository memberRepository;


  @Transactional
  public void changePassword(String email, String currentPassword, String newPassword) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new ServiceException(ErrorCode.INVALID_EMAIL));

    if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
      throw new ServiceException(ErrorCode.INVALID_PASSWORD);
    }
    member.setPassword(passwordEncoder.encode(newPassword));
  }


  @Transactional
  public void resetPassword(String email, String newPassword) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new ServiceException(ErrorCode.INVALID_EMAIL));

    member.setPassword(passwordEncoder.encode(newPassword));
  }
}
