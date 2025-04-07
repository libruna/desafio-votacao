package com.desafio.code.service;

import com.desafio.code.dto.ProposalDTO;
import com.desafio.code.dto.SessionDTO;
import com.desafio.code.dto.VoteDTO;

public interface ProposalService {

    ProposalDTO save(ProposalDTO agendaDTO);
    ProposalDTO getById(String proposalId);
    SessionDTO openVotingSession(String proposalId, SessionDTO sessionDTO);
    SessionDTO getVotingResult(String proposalId);
    VoteDTO registerVote(String proposalId, VoteDTO voteDTO);
}
