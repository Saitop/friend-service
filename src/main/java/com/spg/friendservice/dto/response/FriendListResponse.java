package com.spg.friendservice.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendListResponse {
    @Builder.Default
    private Boolean success = Boolean.TRUE;

    private List<String> friends;

    private Integer count;

}
