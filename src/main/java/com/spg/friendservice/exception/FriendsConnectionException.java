package com.spg.friendservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.spg.friendservice.validation.ValidationMessage.CANNOT_ADD_FRIENDS_WITHIN_BLACKLIST;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class FriendsConnectionException extends BaseException {

    public FriendsConnectionException() {
        super(CANNOT_ADD_FRIENDS_WITHIN_BLACKLIST);
    }

}
