package com.spg.friendservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.spg.friendservice.validation.ValidationMessage.TARGET_NOT_EXIST;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Query a non-exist user")
public class TargetNotExistException extends BaseException {

    public TargetNotExistException() {
        super(TARGET_NOT_EXIST);
    }

}
