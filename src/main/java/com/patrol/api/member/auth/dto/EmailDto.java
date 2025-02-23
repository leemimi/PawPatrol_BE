package com.patrol.api.member.auth.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

/**
 * packageName    : com.patrol.api.member.auth.dto
 * fileName       : EmailDto
 * author         : sungjun
 * date           : 2025-02-23
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-23        kyd54       최초 생성
 */
@Getter
@Setter
public class EmailDto {
    private String from;
    private String receiver;
    private String text;
    private String title;
    private InputStream file;

    // 발신자 정보
    public static final String SENDER_EMAIL = "tjdwnswnswns@naver.com";
    public static final String SENDER_NAME = "퍼피구조대";
}
