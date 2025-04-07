package com.desafio.code.service;

import com.desafio.code.dto.VoteDTO;
import com.desafio.code.exception.BusinessException;
import com.desafio.code.exception.ErrorCode;
import com.desafio.code.model.VoteDocument;
import com.desafio.code.repository.VoteRepository;
import com.desafio.code.service.impl.VoteServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoteServiceImplTest {

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private VoteServiceImpl voteService;

    @Test
    public void save() {
        VoteDocument voteDocument = new VoteDocument();
        voteDocument.setId("123abc456def");
        voteDocument.setMemberId("657hij654tuv");
        voteDocument.setSessionId("987xyz654mno");
        voteDocument.setFavorable(true);

        VoteDTO voteDTO = VoteDTO.builder()
                .id("123abc456def")
                .memberId("657hij654tuv")
                .sessionId("987xyz654mno")
                .favorable(true)
                .build();

        when(voteRepository.save(any(VoteDocument.class))).thenReturn(voteDocument);
        VoteDTO response = this.voteService.save(voteDTO);

        assertEquals(voteDocument.getId(), response.getId());

        verify(voteRepository, times(1)).save(any(VoteDocument.class));
    }

    @Test
    public void getVoteById() {
        String voteId = "123abc456def";

        VoteDocument voteDocument = new VoteDocument();
        voteDocument.setId("123abc456def");
        voteDocument.setMemberId("657hij654tuv");
        voteDocument.setSessionId("987xyz654mno");
        voteDocument.setFavorable(true);

        when(voteRepository.findById(anyString())).thenReturn(Optional.of(voteDocument));
        VoteDTO response = this.voteService.getById(voteId);

        assertEquals(voteId, response.getId());
        assertEquals(voteDocument.getMemberId(), response.getMemberId());
        assertEquals(voteDocument.getSessionId(), response.getSessionId());

        verify(voteRepository, times(1)).findById(anyString());
    }

    @Test
    public void getVoteById_WhenVoteNotFound() {
        String voteId = "123abc456def";

        when(voteRepository.findById(anyString())).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class,
                () -> voteService.getById(voteId));

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getCode().getStatusCode());
        assertEquals(ErrorCode.VOTE_NOT_FOUND.getMessage(), exception.getCode().getMessage());

        verify(voteRepository, times(1)).findById(anyString());
    }
}
