package com.spg.friendservice.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public abstract class BaseException extends RuntimeException {

    private Boolean success = Boolean.FALSE;

    BaseException(String message) {
        super(message);
    }

}