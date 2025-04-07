package com.desafio.code.service.impl;

import com.desafio.code.client.ValidatorClient;
import com.desafio.code.dto.ProposalDTO;
import com.desafio.code.dto.SessionDTO;
import com.desafio.code.dto.VoteDTO;
import com.desafio.code.enums.CPFValidationStatusEnum;
import com.desafio.code.exception.BusinessException;
import com.desafio.code.exception.ErrorCode;
import com.desafio.code.model.ProposalDocument;
import com.desafio.code.repository.ProposalRepository;
import com.desafio.code.service.MemberService;
import com.desafio.code.service.ProposalService;
import com.desafio.code.service.SessionService;
import com.desafio.code.service.VoteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ProposalServiceImpl implements ProposalService {

    private final ModelMapper modelMapper;
    private final ValidatorClient validatorClient;
    private final SessionService sessionService;
    private final VoteService voteService;
    private final MemberService memberService;
    private final ProposalRepository proposalRepository;

    @Autowired
    public ProposalServiceImpl(ValidatorClient validatorClient, SessionService sessionService,
                               VoteService voteService, MemberService memberService, ProposalRepository proposalRepository) {
        this.modelMapper = new ModelMapper();
        this.validatorClient = validatorClient;
        this.sessionService = sessionService;
        this.voteService = voteService;
        this.memberService = memberService;
        this.proposalRepository = proposalRepository;
    }

    @Override
    public ProposalDTO save(ProposalDTO proposalDTO) {
        return ProposalDTO.builder()
                .id(proposalRepository.save(modelMapper.map(proposalDTO, ProposalDocument.class)).getId())
                .build();
    }

    @Override
    public ProposalDTO getById(String proposalId) {
        Optional<ProposalDocument> document = proposalRepository.findById(proposalId);
        if (document.isEmpty()) {
            throw new BusinessException(ErrorCode.PROPOSAL_NOT_FOUND);
        }
        return modelMapper.map(document, ProposalDTO.class);
    }

    public SessionDTO openVotingSession(String proposalId, SessionDTO sessionDTO) {
        Optional<ProposalDocument> document = proposalRepository.findById(proposalId);
        if (document.isEmpty()) {
            throw new BusinessException(ErrorCode.PROPOSAL_NOT_FOUND);
        }

        if (sessionService.hasOpenSessionForProposal(proposalId)) {
            throw new BusinessException(ErrorCode.SESSION_ALREADY_OPEN_FOR_THIS_PROPOSAL);
        }

        return SessionDTO.builder()
                    .id(sessionService.save(proposalId, sessionDTO).getId())
                    .build();
    }

    @Override
    public VoteDTO registerVote(String proposalId, VoteDTO voteDTO) {
        Optional<ProposalDocument> document = proposalRepository.findById(proposalId);
        if (document.isEmpty()) {
            throw new BusinessException(ErrorCode.PROPOSAL_NOT_FOUND);
        }

        String CPF = memberService.getById(voteDTO.getMemberId()).getCPF();
        if (validatorClient.CPFValidator(CPF).equals(CPFValidationStatusEnum.UNABLE_TO_VOTE)) {
            throw new BusinessException(ErrorCode.MEMBER_UNABLE_TO_VOTE);
        }

        SessionDTO sessionDTO = sessionService.getOpenSessionForProposal(proposalId);
        voteDTO.setSessionId(sessionDTO.getId());

        voteService.validateMemberHasNotVoted(voteDTO.getMemberId(), sessionDTO.getId());

        return VoteDTO.builder()
                .id(voteService.save(voteDTO).getId())
                .build();
    }

    @Override
    public SessionDTO getVotingResult(String proposalId) {
        Optional<ProposalDocument> document = this.proposalRepository.findById(proposalId);
        if (document.isEmpty()) {
            throw new BusinessException(ErrorCode.PROPOSAL_NOT_FOUND);
        }

        SessionDTO sessionDTO = sessionService.getLatestSessionForProposal(proposalId);

        if (sessionDTO.getStatus() == null) {
            Map<String, Integer> totalVotes = voteService.countVotesForSession(sessionDTO.getId());
            return sessionService.buildVotingSessionResult(sessionDTO, totalVotes.get("favorable"), totalVotes.get("opposed"));
        }
        return sessionDTO;
    }
}
