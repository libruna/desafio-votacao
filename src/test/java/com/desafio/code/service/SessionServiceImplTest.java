package com.desafio.code.service;

import com.desafio.code.dto.SessionDTO;
import com.desafio.code.exception.BusinessException;
import com.desafio.code.exception.ErrorCode;
import com.desafio.code.model.SessionDocument;
import com.desafio.code.repository.SessionRepository;
import com.desafio.code.service.impl.SessionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceImplTest {

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionServiceImpl sessionService;

    @Test
    public void save() {
        String proposalId = "987xyz654mno";

        SessionDocument sessionDocument = new SessionDocument();
        sessionDocument.setId("123abc456def");
        sessionDocument.setProposalId("987xyz654mno");
        sessionDocument.setStartTime(LocalDateTime.now());
        sessionDocument.setEndTime(LocalDateTime.now().plusMinutes(1));

        SessionDTO sessionDTO = SessionDTO.builder()
                .id("123abc456def")
                .durationInMinutes(1)
                .build();

        when(sessionRepository.save(any(SessionDocument.class))).thenReturn(sessionDocument);
        SessionDTO response = sessionService.save(proposalId, sessionDTO);

        assertEquals(sessionDTO.getId(), response.getId());
        assertEquals(sessionDTO.getStartTime(), response.getStartTime());
        assertEquals(sessionDTO.getStartTime(), response.getStartTime());

        verify(sessionRepository, times(1)).save(any(SessionDocument.class));
    }

    @Test
    public void getById() {
        String sessionId = "123abc456def";

        SessionDocument sessionDocument = new SessionDocument();
        sessionDocument.setId("123abc456def");
        sessionDocument.setProposalId("987xyz654mno");
        sessionDocument.setStartTime(LocalDateTime.now());
        sessionDocument.setEndTime(LocalDateTime.now().plusMinutes(1));

        when(sessionRepository.findById(anyString())).thenReturn(Optional.of(sessionDocument));
        SessionDTO response = sessionService.getById(sessionId);

        assertEquals(sessionDocument.getId(), response.getId());
        assertEquals(sessionDocument.getStartTime(), response.getStartTime());
        assertEquals(sessionDocument.getStartTime(), response.getStartTime());

        verify(sessionRepository, times(1)).findById(anyString());
    }

    @Test
    public void getById_WhenSessionNotFound() {
        String sessionId = "123abc456def";

        when(sessionRepository.findById(anyString())).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class,
                () -> sessionService.getById(sessionId));

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getCode().getStatusCode());
        assertEquals(ErrorCode.SESSION_NOT_FOUND.getMessage(), exception.getCode().getMessage());

        verify(sessionRepository, times(1)).findById(anyString());
    }

    @Test
    public void getOpenSessionForProposal() {
        String proposalId = "987xyz654mno";

        SessionDocument sessionDocument = new SessionDocument();
        sessionDocument.setId("123abc456def");
        sessionDocument.setProposalId("987xyz654mno");
        sessionDocument.setStartTime(LocalDateTime.now());
        sessionDocument.setEndTime(LocalDateTime.now().plusMinutes(1));

        when(sessionRepository.findByProposalIdAndEndTimeAfter(anyString(), any(LocalDateTime.class)))
                .thenReturn(Optional.of(sessionDocument));
        SessionDTO response = sessionService.getOpenSessionForProposal(proposalId);

        assertEquals(sessionDocument.getId(), response.getId());
        assertEquals(sessionDocument.getStartTime(), response.getStartTime());
        assertEquals(sessionDocument.getStartTime(), response.getStartTime());

        verify(sessionRepository, times(1)).findByProposalIdAndEndTimeAfter(anyString(), any(LocalDateTime.class));
    }

    @Test
    public void getOpenSessionForProposal_WhenSessionIsNotOpen() {
        String proposalId = "000xyz654mno";

        when(sessionRepository.findByProposalIdAndEndTimeAfter(anyString(), any(LocalDateTime.class))).
                thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class,
                () -> sessionService.getOpenSessionForProposal(proposalId));

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getCode().getStatusCode());
        assertEquals(ErrorCode.VOTING_SESSION_CLOSED.getMessage(), exception.getCode().getMessage());

        verify(sessionRepository, times(1))
                .findByProposalIdAndEndTimeAfter(anyString(), any(LocalDateTime.class));
    }

    @Test
    public void getLatestSessionForProposal() {
        String proposalId = "123abc456def";

        SessionDocument sessionDocument = new SessionDocument();
        sessionDocument.setId("123abc456def");
        sessionDocument.setProposalId("987xyz654mno");
        sessionDocument.setStartTime(LocalDateTime.now().minusHours(2));
        sessionDocument.setEndTime(LocalDateTime.now().minusHours(1));

        when(sessionRepository.findTopByProposalIdOrderByStartTimeDesc(anyString()))
                .thenReturn(Optional.of(sessionDocument));
        SessionDTO response = sessionService.getLatestSessionForProposal(proposalId);

        assertEquals(sessionDocument.getId(), response.getId());
        assertEquals(sessionDocument.getStartTime(), response.getStartTime());
        assertEquals(sessionDocument.getStartTime(), response.getStartTime());

        verify(sessionRepository, times(1)).findTopByProposalIdOrderByStartTimeDesc(anyString());
    }

    @Test
    public void getLatestSessionForProposal_WhenSessionNotFound() {
        String proposalId = "000xyz654mno";

        when(sessionRepository.findTopByProposalIdOrderByStartTimeDesc(anyString())).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class,
                () -> sessionService.getLatestSessionForProposal(proposalId));

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getCode().getStatusCode());
        assertEquals(ErrorCode.SESSION_NOT_FOUND.getMessage(), exception.getCode().getMessage());

        verify(sessionRepository, times(1))
                .findTopByProposalIdOrderByStartTimeDesc(anyString());
    }

    @Test
    public void getLatestSessionForProposal_WhenSessionIsOpen() {
        String proposalId = "000xyz654mno";

        SessionDocument sessionDocument = new SessionDocument();
        sessionDocument.setId("123abc456def");
        sessionDocument.setProposalId("987xyz654mno");
        sessionDocument.setStartTime(LocalDateTime.now());
        sessionDocument.setEndTime(LocalDateTime.now().plusMinutes(1));

        when(sessionRepository.findTopByProposalIdOrderByStartTimeDesc(anyString())).thenReturn(Optional.of(sessionDocument));
        BusinessException exception = assertThrows(BusinessException.class,
                () -> sessionService.getLatestSessionForProposal(proposalId));

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), exception.getCode().getStatusCode());
        assertEquals(ErrorCode.WAIT_FOR_SESSION_END.getMessage(), exception.getCode().getMessage());

        verify(sessionRepository, times(1))
                .findTopByProposalIdOrderByStartTimeDesc(anyString());
    }

    @Test
    public void hasOpenSessionForProposal() {
        String proposalId = "123abc456def";

        SessionDocument sessionDocument = new SessionDocument();
        sessionDocument.setId("123abc456def");
        sessionDocument.setProposalId("987xyz654mno");
        sessionDocument.setStartTime(LocalDateTime.now());
        sessionDocument.setEndTime(LocalDateTime.now().plusMinutes(1));

        when(sessionRepository.findByProposalIdAndEndTimeAfter(anyString(), any(LocalDateTime.class)))
                .thenReturn(Optional.of(sessionDocument));
        boolean response = sessionService.hasOpenSessionForProposal(proposalId);

        assertTrue(response);

        verify(sessionRepository, times(1))
                .findByProposalIdAndEndTimeAfter(anyString(), any(LocalDateTime.class));
    }
}
