package com.desafio.code.repository;

import com.desafio.code.model.VoteDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VoteRepository extends MongoRepository<VoteDocument, String>, VoteRepositoryCustom{

    Optional<VoteDocument> findByMemberIdAndSessionId(String memberId, String sessionId);
}
