package com.spg.friendservice.dto.request;

import com.spg.friendservice.validation.ValidationMessage;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendListRequest {

    @NotNull(message = ValidationMessage.EMAILS_CANNOT_BE_NULL)
    private String email;

}
