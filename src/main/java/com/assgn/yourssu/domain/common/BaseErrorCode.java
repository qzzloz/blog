package com.assgn.yourssu.domain.common;

import com.assgn.yourssu.dto.ErrorReasonDTO;

public interface BaseErrorCode {

    public ErrorReasonDTO getReason();

    public ErrorReasonDTO getReasonHttpStatus();
}
