package com.desafio.code.service;

import com.desafio.code.dto.VoteDTO;

import java.util.Map;

public interface VoteService {

    VoteDTO save(VoteDTO voteDTO);
    VoteDTO getById(String id);
    void validateMemberHasNotVoted(String memberId, String sessionId);
    Map<String, Integer> countVotesForSession(String sessionId);
}
