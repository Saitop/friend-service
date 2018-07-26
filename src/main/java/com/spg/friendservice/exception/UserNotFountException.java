package com.spg.friendservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Query a non-exist user")
public class UserNotFountException extends BaseException {
    public UserNotFountException(){
        super("User not fount");
    }
}
