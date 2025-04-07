package com.desafio.code.repository;

import com.desafio.code.model.ProposalDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProposalRepository extends MongoRepository<ProposalDocument, String> {
}
