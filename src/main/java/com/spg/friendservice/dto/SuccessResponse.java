package com.spg.friendservice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SuccessResponse {
    @Builder.Default
    private Boolean success = Boolean.TRUE;
}
