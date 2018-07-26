package com.spg.friendservice.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResult {

    /**
     * to correspond with the requirement,  the successful response contain a 'success' field,
     * error result should contain a 'false' indicator with a key 'success',
     * which is not a good practice from my point of view.
     *
     */
    private Boolean success = Boolean.FALSE;

    private Integer status;
    private String message;

    public ErrorResult(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
