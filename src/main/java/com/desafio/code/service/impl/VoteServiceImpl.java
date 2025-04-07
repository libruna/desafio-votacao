package com.desafio.code.service.impl;

import com.desafio.code.dto.VoteDTO;
import com.desafio.code.exception.BusinessException;
import com.desafio.code.exception.ErrorCode;
import com.desafio.code.model.VoteDocument;
import com.desafio.code.repository.VoteRepository;
import com.desafio.code.service.VoteService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService {

    private final ModelMapper modelMapper;
    private final VoteRepository voteRepository;

    public VoteServiceImpl(VoteRepository voteRepository) {
        this.modelMapper = new ModelMapper();
        this.voteRepository = voteRepository;
    }

    @Override
    public VoteDTO save(VoteDTO voteDTO) {
        return VoteDTO.builder()
                .id(voteRepository.save(modelMapper.map(voteDTO, VoteDocument.class)).getId())
                .build();
    }

    @Override
    public VoteDTO getById(String id) {
        Optional<VoteDocument> voteDocument = voteRepository.findById(id);
        if (voteDocument.isEmpty()) {
            throw new BusinessException(ErrorCode.VOTE_NOT_FOUND);
        }
        return modelMapper.map(voteDocument, VoteDTO.class);
    }

    @Override
    public void validateMemberHasNotVoted(String memberId, String sessionId) {
        Optional<VoteDocument> voteDocument = this.voteRepository.findByMemberIdAndSessionId(memberId, sessionId);
        if (voteDocument.isPresent()) {
            throw new BusinessException(ErrorCode.MEMBER_ALREADY_VOTED);
        }
    }

    @Override
    public Map<String, Integer> countVotesForSession(String sessionId) {
        return voteRepository.countVotesForSession(sessionId);
    }
}
