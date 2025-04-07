package com.desafio.code.service;

import com.desafio.code.dto.SessionDTO;

public interface SessionService {

    SessionDTO save(String proposalId, SessionDTO sessionDTO);
    SessionDTO getById(String id);
    SessionDTO getOpenSessionForProposal(String proposalId);
    SessionDTO getLatestSessionForProposal(String proposalId);
    SessionDTO buildVotingSessionResult(SessionDTO sessionDTO, int totalFavorable, int totalOpposed);
    boolean hasOpenSessionForProposal(String proposalId);
}