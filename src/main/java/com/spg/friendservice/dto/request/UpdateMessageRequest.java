package com.spg.friendservice.dto.request;

import com.spg.friendservice.validation.ValidationMessage;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMessageRequest {

    @NotNull(message = ValidationMessage.SENDER_CONNOT_BE_NULL)
    private String sender;

    private String text;

}
