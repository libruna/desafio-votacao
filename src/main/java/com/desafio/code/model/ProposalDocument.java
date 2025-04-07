package com.desafio.code.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "proposals")
public class ProposalDocument {

    @Id
    private String id;
    private String title;
    private String description;

    @CreatedDate
    private LocalDateTime createdAt;
}
