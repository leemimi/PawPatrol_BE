package com.patrol.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "제공된 입력 값이 유효하지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 요청 방식입니다."),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "요청한 엔티티를 찾을 수 없습니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "제공된 값의 타입이 유효하지 않습니다."),
    ERROR_PARSING_JSON_RESPONSE(HttpStatus.BAD_REQUEST, "JSON 응답을 파싱하는 중 오류가 발생했습니다."),
    MISSING_INPUT_VALUE(HttpStatus.BAD_REQUEST, "필수 입력 값이 누락되었습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    FILE_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제 중 오류가 발생했습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),


    // Member
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST,"사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일 입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
