package com.desafio.code.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "sessions")
public class SessionDocument {

    @Id
    private String id;
    private String proposalId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long totalFavorable;
    private Long totalOpposed;
    private String status;
}
