package com.spg.friendservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.spg.friendservice.validation.ValidationMessage.DUPLICATE_BLACKLIST;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateBlacklistException extends BaseException {

    public DuplicateBlacklistException(String requestor, String target) {
        super(String.format(DUPLICATE_BLACKLIST, requestor, target));
    }

}
