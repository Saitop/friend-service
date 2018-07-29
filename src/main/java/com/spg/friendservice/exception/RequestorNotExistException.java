package com.spg.friendservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.spg.friendservice.validation.ValidationMessage.REQUESTOR_NOT_EXIST;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Query a non-exist user")
public class RequestorNotExistException extends BaseException {

    public RequestorNotExistException(){
        super(REQUESTOR_NOT_EXIST);
    }

}
