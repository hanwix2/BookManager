package com.learning.jpa.BookManager.domain;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class User {

    private final String name;

    private final String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}

