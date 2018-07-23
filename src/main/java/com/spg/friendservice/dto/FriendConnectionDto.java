package com.spg.friendservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendConnectionDto {
    private List<String> friends;
}
