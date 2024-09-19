package com.assgn.yourssu.domain.common;

import com.assgn.yourssu.dto.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode{

    // 공통
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바람"),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청"),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증 필요"),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청"),

    // 유저
    USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "USER4001", "유저가 없습니다."),
    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "USER4001", "이미 존재하는 이메일입니다."),
    NOT_VALID_PASSWORD(HttpStatus.BAD_REQUEST, "PASSWORD4001", "비밀번호가 틀렸습니다."),

    // 게시글
    ARTICLE_NOT_EXITS(HttpStatus.BAD_REQUEST, "ARTICLE4001", "존재하지 않는 게시글입니다."),
    NOT_VALID_USER(HttpStatus.BAD_REQUEST, "ARTICLE4001", "자신이 작성한 게시글, 댓글만 수정 및 삭제 할 수 있습니다."),
    INVALID_TITLE(HttpStatus.BAD_REQUEST, "400", "유효하지 않은 title 형식입니다."),
    INVALID_CONTENT(HttpStatus.BAD_REQUEST, "400", "유효하지 않은 content 형식입니다."),

    // 댓글
    COMMENT_NOT_EXIST(HttpStatus.BAD_REQUEST, "COMMENT4001", "존재하지 않는 댓글입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
