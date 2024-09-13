package com.assgn.yourssu.exception;

import com.assgn.yourssu.domain.common.BaseErrorCode;

public class CommentException extends GeneralException {
    public CommentException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
