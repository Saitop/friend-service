package com.spg.friendservice.dto.request;

import com.spg.friendservice.validation.ValidationMessage;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendConnectionRequest {
    @NotNull(message = ValidationMessage.FRIENDS_CANNOT_BE_NULL)
    private List<String> friends;
}
