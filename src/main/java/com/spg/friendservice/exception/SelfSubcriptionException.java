package com.spg.friendservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.spg.friendservice.validation.ValidationMessage.CANNOT_SUBSCRIBE_TO_YOURSELF;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SelfSubcriptionException extends BaseException {

    public SelfSubcriptionException(){
        super(CANNOT_SUBSCRIBE_TO_YOURSELF);
    }
}
