package com.desafio.code.repository;

import com.desafio.code.model.MemberDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MemberRepository extends MongoRepository<MemberDocument, String> {
    Optional<MemberDocument> findMemberDocumentByCPF(String CPF);
}
