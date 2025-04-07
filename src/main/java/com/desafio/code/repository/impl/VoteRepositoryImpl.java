package com.desafio.code.repository.impl;

import com.desafio.code.model.VoteDocument;
import com.desafio.code.repository.VoteRepositoryCustom;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class VoteRepositoryImpl implements VoteRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public VoteRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Map countVotesForSession(String sessionId) {
        Aggregation aggregation = Aggregation.newAggregation(
                match(where("sessionId").is(sessionId)),
                group()
                        .sum(ConditionalOperators.when(where("favorable").is(true))
                                .then(1).otherwise(0)).as("favorable")
                        .sum(ConditionalOperators.when(where("favorable").is(false))
                                .then(1).otherwise(0)).as("opposed")
        );

        return mongoTemplate.aggregate(aggregation, VoteDocument.class, Map.class).getUniqueMappedResult();
    }
}
