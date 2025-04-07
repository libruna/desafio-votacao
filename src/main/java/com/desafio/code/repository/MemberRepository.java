package com.desafio.code.repository;

import com.desafio.code.model.MemberDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<MemberDocument, String> {
}
