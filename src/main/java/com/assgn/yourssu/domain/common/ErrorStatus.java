package com.assgn.yourssu.domain.common;

import com.assgn.yourssu.dto.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode{

    // 유저
    USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "USER4001", "유저가 없습니다."),
    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "USER4001", "이미 존재하는 이메일입니다."),
    NOT_VALID_PASSWORD(HttpStatus.BAD_REQUEST, "PASSWORD4001", "비밀번호가 틀렸습니다."),

    // 게시글
    ARTICLE_NOT_EXITS(HttpStatus.BAD_REQUEST, "ARTICLE4001", "존재하지 않는 게시글입니다."),
    NOT_VALID_USER(HttpStatus.BAD_REQUEST, "ARTICLE4001", "자신이 작성한 게시글만 수정 및 삭제 할 수 있습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


//    @Override
//    public ApiResponse<Void> getErrorResponse() {
//        return ApiResponse.onFailure(code, message);
//    }

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
