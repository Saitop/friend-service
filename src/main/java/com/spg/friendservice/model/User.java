package com.spg.friendservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection="users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    @Version
    private Long version;

    private String email;

    @Builder.Default
    private List<String> friends = new ArrayList<>();
}
