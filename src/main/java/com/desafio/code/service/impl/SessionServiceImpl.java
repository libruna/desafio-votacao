package com.desafio.code.service.impl;

import com.desafio.code.dto.SessionDTO;
import com.desafio.code.enums.VotingResultStatusEnum;
import com.desafio.code.exception.BusinessException;
import com.desafio.code.exception.ErrorCode;
import com.desafio.code.model.SessionDocument;
import com.desafio.code.repository.SessionRepository;
import com.desafio.code.service.SessionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {

    private final ModelMapper modelMapper;
    private final SessionRepository sessionRepository;

    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.modelMapper = new ModelMapper();
        this.sessionRepository = sessionRepository;
    }

    public SessionDTO save(String proposalId, SessionDTO sessionDTO) {
        int duration = 1;
        if (sessionDTO == null) {
            sessionDTO = new SessionDTO();
        } else if (sessionDTO.getDurationInMinutes() > 0) {
            duration = sessionDTO.getDurationInMinutes();
        }

        SessionDocument sessionDocument = modelMapper.map(sessionDTO, SessionDocument.class);
        sessionDocument.setProposalId(proposalId);
        sessionDocument.setStartTime(LocalDateTime.now());
        sessionDocument.setEndTime(sessionDocument.getStartTime().plusMinutes(duration));

        return SessionDTO.builder()
                .id(sessionRepository.save(sessionDocument).getId())
                .build();
    }

    @Override
    public SessionDTO getById(String id) {
        Optional<SessionDocument> sessionDocument = sessionRepository.findById(id);
        if (sessionDocument.isEmpty()) {
            throw new BusinessException(ErrorCode.SESSION_NOT_FOUND);
        }
        return modelMapper.map(sessionDocument, SessionDTO.class);
    }

    @Override
    public SessionDTO getOpenSessionForProposal(String proposalId) {
        Optional<SessionDocument> sessionDocument = sessionRepository
                .findByProposalIdAndEndTimeAfter(proposalId, LocalDateTime.now());

        if (sessionDocument.isEmpty()) {
            throw new BusinessException(ErrorCode.VOTING_SESSION_CLOSED);
        }
        return modelMapper.map(sessionDocument, SessionDTO.class);
    }

    @Override
    public SessionDTO getLatestSessionForProposal(String proposalId) {
        Optional<SessionDocument> sessionDocument = sessionRepository
                .findTopByProposalIdOrderByStartTimeDesc(proposalId);

        if (sessionDocument.isEmpty()) {
            throw new BusinessException(ErrorCode.SESSION_NOT_FOUND);
        }

        if (sessionDocument.get().getEndTime().isAfter(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.WAIT_FOR_SESSION_END);
        }
        return modelMapper.map(sessionDocument, SessionDTO.class);
    }

    @Override
    public boolean hasOpenSessionForProposal(String proposalId) {
        return sessionRepository.findByProposalIdAndEndTimeAfter(proposalId, LocalDateTime.now())
                .isPresent();
    }

    @Override
    public SessionDTO buildVotingSessionResult(SessionDTO sessionDTO, int totalFavorable, int totalOpposed) {
        SessionDTO votingResult = new SessionDTO();
        votingResult.setTotalFavorable(totalFavorable);
        votingResult.setTotalOpposed(totalOpposed);
        votingResult.setTotalVotes(totalFavorable + totalOpposed);
        votingResult.setStartTime(sessionDTO.getStartTime());
        votingResult.setEndTime(sessionDTO.getEndTime());

        if (totalFavorable == totalOpposed) {
            votingResult.setStatus(VotingResultStatusEnum.TIED);
        } else if (totalFavorable > totalOpposed) {
            votingResult.setStatus(VotingResultStatusEnum.APPROVED);
        } else {
            votingResult.setStatus(VotingResultStatusEnum.REJECTED);
        }

        return votingResult;
    }
}
