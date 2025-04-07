package com.desafio.code.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "votes")
public class VoteDocument {

    @Id
    private String id;
    private String memberId;
    private String sessionId;
    private boolean favorable;

    @CreatedDate
    private LocalDateTime createdAt;
}
