package com.desafio.code.repository;

import com.desafio.code.model.VoteDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoteRepository extends MongoRepository<VoteDocument, String>, VoteRepositoryCustom{
}
