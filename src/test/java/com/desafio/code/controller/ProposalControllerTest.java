package com.desafio.code.controller;

import com.desafio.code.dto.ProposalDTO;
import com.desafio.code.dto.SessionDTO;
import com.desafio.code.dto.VoteDTO;
import com.desafio.code.enums.VotingResultStatusEnum;
import com.desafio.code.service.ProposalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProposalControllerTest {

    @Mock
    private ProposalService proposalService;

    @InjectMocks
    private ProposalController proposalController;

    @Test
    public void createProposal() {
        ProposalDTO proposalDTO = ProposalDTO.builder()
                .id("123abc456def")
                .title("Implantação de Semana de Inovação")
                .description("Proposta para reservar uma semana por semestre dedicada a projetos internos de inovação.")
                .build();

        when(proposalService.save(any(ProposalDTO.class))).thenReturn(proposalDTO);
        ResponseEntity<ProposalDTO> response = proposalController.createProposal(proposalDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        verify(proposalService, times(1)).save(any(ProposalDTO.class));
    }

    @Test
    public void getProposal() {
        String proposalId = "123abc456def";

        ProposalDTO proposalDTO = ProposalDTO.builder()
                .id("123abc456def")
                .title("Implantação de Semana de Inovação")
                .description("Proposta para reservar uma semana por semestre dedicada a projetos internos de inovação.")
                .build();

        when(proposalService.getById(anyString())).thenReturn(proposalDTO);
        ResponseEntity<ProposalDTO> response = proposalController.getProposal(proposalId);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(proposalDTO.getTitle(), response.getBody().getTitle());
        assertEquals(proposalDTO.getDescription(), response.getBody().getDescription());

        verify(proposalService, times(1)).getById(anyString());
    }

    @Test
    public void openVotingSession() {
        String proposalId = "123abc456def";
        SessionDTO requestBody = SessionDTO.builder()
                .durationInMinutes(1)
                .build();

        SessionDTO sessionDTO = SessionDTO.builder()
                .id("123abc456del")
                .startTime(LocalDateTime.now().minusHours(2))
                .endTime(LocalDateTime.now().minusHours(1))
                .build();

        when(proposalService.openVotingSession(anyString(), any(SessionDTO.class))).thenReturn(sessionDTO);
        ResponseEntity<SessionDTO> response = proposalController.openVotingSession(proposalId, requestBody);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        verify(proposalService, times(1)).openVotingSession(anyString(), any(SessionDTO.class));
    }

    @Test
    public void registerVote() {
        String proposalId = "123abc456def";
        VoteDTO voteDTO = VoteDTO.builder()
                .id("123abc456dev")
                .memberId("657hij654tuv")
                .sessionId("987xyz654mno")
                .favorable(true)
                .build();

        when(proposalService.registerVote(anyString(), any(VoteDTO.class))).thenReturn(voteDTO);
        ResponseEntity<VoteDTO> response = proposalController.registerVote(proposalId, voteDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        verify(proposalService, times(1)).registerVote(anyString(), any(VoteDTO.class));
    }

    @Test
    public void getVotingResult() {
        String proposalId = "123abc456def";
        SessionDTO sessionDTO = SessionDTO.builder()
                .startTime(LocalDateTime.now().minusHours(2))
                .endTime(LocalDateTime.now().minusHours(1))
                .totalVotes(50)
                .totalFavorable(45)
                .totalOpposed(5)
                .status(VotingResultStatusEnum.APPROVED)
                .build();

        when(proposalService.getVotingResult(anyString())).thenReturn(sessionDTO);
        ResponseEntity<SessionDTO> response = proposalController.getVotingResult(proposalId);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(proposalService, times(1)).getVotingResult(anyString());
    }
}
