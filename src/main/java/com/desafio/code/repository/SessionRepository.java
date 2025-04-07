package com.desafio.code.repository;

import com.desafio.code.model.SessionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionRepository extends MongoRepository<SessionDocument, String> {
}
