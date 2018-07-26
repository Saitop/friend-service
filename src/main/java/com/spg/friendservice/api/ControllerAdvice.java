package com.spg.friendservice.api;

import com.spg.friendservice.exception.BaseException;
import com.spg.friendservice.exception.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<ErrorResult> handleBaseException(BaseException e) {
        Optional<ResponseStatus> responseStatus = Optional.ofNullable(e.getClass().getDeclaredAnnotation(ResponseStatus.class));
        HttpStatus httpStatus = responseStatus.map(ResponseStatus::code).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
        String reason = responseStatus.map(ResponseStatus::reason).orElse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        log.error("server exception: code: {} reason: {} message: {}",
                httpStatus,
                reason,
                e);
        return ResponseEntity.status(httpStatus)
                .body(new ErrorResult(httpStatus.value(), e.getMessage()));
    }
}
