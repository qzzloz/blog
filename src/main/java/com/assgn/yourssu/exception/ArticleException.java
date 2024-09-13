package com.assgn.yourssu.exception;

import com.assgn.yourssu.domain.common.BaseErrorCode;

public class ArticleException extends GeneralException {
    public ArticleException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
