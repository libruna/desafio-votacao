package com.desafio.code.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "members")
public class MemberDocument {

    @Id
    private String id;
    private String CPF;
    private String name;
}
