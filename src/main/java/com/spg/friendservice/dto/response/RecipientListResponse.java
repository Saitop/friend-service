package com.spg.friendservice.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipientListResponse {
    @Builder.Default
    private Boolean success = Boolean.TRUE;

    private List<String> recipients;

    private Integer count;
}
