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


    // AnimalCase
    INVALID_CASE(HttpStatus.BAD_REQUEST, "유효하지 않은 케이스입니다."),
    INVALID_STATUS_CHANGE(HttpStatus.BAD_REQUEST, "유효하지 않은 상태 변경입니다."),
    INVALID_HISTORY_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 히스토리 상태입니다."),
    NOT_ASSIGNED_PROTECTION(HttpStatus.BAD_REQUEST, "지정된 보호자가 없습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "해당 리소스에 대한 접근 권한이 없습니다."),


    // BAD_REQUEST : 요청이 잘못되었어요, FORBIDDEN : 요청은 이해했는데 당신한테 권한이 없어요
    // Protection
    ALREADY_FOSTER(HttpStatus.BAD_REQUEST, "이미 보호자입니다."),
    ALREADY_APPLIED(HttpStatus.BAD_REQUEST, "이미 신청하였습니다."),


    // Member
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST,"사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.FORBIDDEN, "이미 사용중인 이메일 입니다."),
    RESTRICTED_ACCOUNT_ACCESS(HttpStatus.BAD_REQUEST, "이 계정은 현재 접근이 제한되어 있습니다. 계정이 휴면, 정지 또는 탈퇴 처리되었을 수 있으니, 자세한 내용은 고객센터로 문의해 주세요."),

    // File
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 파일 형식입니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "파일 크기는 5MB를 넘을 수 없습니다."),

    // Auth
    EMAIL_NOT_FOUND(HttpStatus.FORBIDDEN, "등록되지 않은 이메일 입니다."),
    VERIFICATION_NOT_FOUND(HttpStatus.FORBIDDEN, "유효하지 않은 접근입니다."),

    // Animal
    PET_OWNER_MISMATCH(HttpStatus.FORBIDDEN, "해당 반려동물의 소유자가 아닙니다. 본인이 등록한 반려동물만 수정할 수 있습니다."),

    POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 게시글을 찾을 수 없습니다."),
    ANIMAL_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 반려동물을 찾을 수 없습니다."),
    FILE_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "파일 업로드 중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
