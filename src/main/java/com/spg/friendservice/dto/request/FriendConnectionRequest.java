package com.spg.friendservice.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendConnectionRequest {
    private List<String> friends;
}
