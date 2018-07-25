package com.spg.friendservice.dto.response;

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
