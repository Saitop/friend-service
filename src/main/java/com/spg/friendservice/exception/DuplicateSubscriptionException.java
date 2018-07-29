package com.spg.friendservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.spg.friendservice.validation.ValidationMessage.DUPLICATE_SUBSCRIPTION;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateSubscriptionException extends BaseException {

    public DuplicateSubscriptionException(String requestor, String target) {
        super(String.format(DUPLICATE_SUBSCRIPTION, requestor, target));
    }
}
