package com.assgn.yourssu.exception;

import com.assgn.yourssu.domain.common.BaseErrorCode;

public class UserException extends GeneralException {
    public UserException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
