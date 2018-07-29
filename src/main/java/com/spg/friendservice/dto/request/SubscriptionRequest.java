package com.spg.friendservice.dto.request;

import com.spg.friendservice.validation.ValidationMessage;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRequest {

    @NotNull(message = ValidationMessage.REQUESTOR_EMAIL_CANNOT_BE_NULL)
    private String requestor;

    @NotNull(message = ValidationMessage.TARGET_EMAIL_CANNOT_BE_NULL)
    private String target;
}
