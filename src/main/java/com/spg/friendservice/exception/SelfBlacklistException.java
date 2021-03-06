package com.spg.friendservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.spg.friendservice.validation.ValidationMessage.CANNOT_BLACKLIST_ONESELF;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SelfBlacklistException extends BaseException {

    public SelfBlacklistException(){
        super(CANNOT_BLACKLIST_ONESELF);
    }

}