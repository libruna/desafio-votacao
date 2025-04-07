package com.desafio.code.repository;

import com.desafio.code.model.SessionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SessionRepository extends MongoRepository<SessionDocument, String> {
    Optional<SessionDocument> findByProposalIdAndEndTimeAfter(String proposalId, LocalDateTime currentTime);
    Optional<SessionDocument> findTopByProposalIdOrderByStartTimeDesc(String proposalId);
}
