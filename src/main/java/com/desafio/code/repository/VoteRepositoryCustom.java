package com.desafio.code.repository;

import java.util.Map;

public interface VoteRepositoryCustom {

    Map<String, Integer> countVotesForSession(String sessionId);
}
