package com.desafio.code.repository.impl;

import com.desafio.code.model.VoteDocument;
import com.desafio.code.repository.VoteRepositoryCustom;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VoteRepositoryImpl implements VoteRepositoryCustom {

    private final MongoTemplate mongoTemplate;

}
