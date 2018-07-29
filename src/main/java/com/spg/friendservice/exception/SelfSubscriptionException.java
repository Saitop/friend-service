package com.spg.friendservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.spg.friendservice.validation.ValidationMessage.CANNOT_SUBSCRIBE_TO_ONESELF;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SelfSubscriptionException extends BaseException {

    public SelfSubscriptionException(){
        super(CANNOT_SUBSCRIBE_TO_ONESELF);
    }

}
