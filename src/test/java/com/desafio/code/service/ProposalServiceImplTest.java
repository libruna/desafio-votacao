package com.desafio.code.service;

import com.desafio.code.client.ValidatorClient;
import com.desafio.code.dto.MemberDTO;
import com.desafio.code.dto.ProposalDTO;
import com.desafio.code.dto.SessionDTO;
import com.desafio.code.dto.VoteDTO;
import com.desafio.code.enums.CPFValidationStatusEnum;
import com.desafio.code.enums.VotingResultStatusEnum;
import com.desafio.code.exception.BusinessException;
import com.desafio.code.exception.ErrorCode;
import com.desafio.code.model.ProposalDocument;
import com.desafio.code.repository.ProposalRepository;
import com.desafio.code.service.impl.ProposalServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProposalServiceImplTest {

    @Mock
    private ProposalRepository proposalRepository;

    @Mock
    private SessionService sessionService;

    @Mock
    private MemberService memberService;

    @Mock
    private VoteService voteService;

    @Mock
    private ValidatorClient validatorClient;

    @InjectMocks
    private ProposalServiceImpl proposalService;

    @Test
    public void save() {
        ProposalDocument proposalDocument = new ProposalDocument();
        proposalDocument.setId("123abc456def");
        proposalDocument.setTitle("Implantação de Semana de Inovação");
        proposalDocument.setDescription("Proposta para reservar uma semana por semestre dedicada a projetos internos de inovação.");

        ProposalDTO proposalDTO = ProposalDTO.builder()
                .title("Implantação de Semana de Inovação")
                .description("Proposta para reservar uma semana por semestre dedicada a projetos internos de inovação.")
                .build();

        when(proposalRepository.save(any(ProposalDocument.class))).thenReturn(proposalDocument);
        ProposalDTO response = this.proposalService.save(proposalDTO);

        assertEquals(proposalDocument.getId(), response.getId());

        verify(proposalRepository, times(1)).save(any(ProposalDocument.class));
    }

    @Test
    public void getById() {
        String proposalId = "123abc456def";

        ProposalDocument proposalDocument = new ProposalDocument();
        proposalDocument.setId("123abc456def");
        proposalDocument.setTitle("Implantação de Semana de Inovação");
        proposalDocument.setDescription("Proposta para reservar uma semana por semestre dedicada a projetos internos de inovação.");

        when(proposalRepository.findById(anyString())).thenReturn(Optional.of(proposalDocument));
        ProposalDTO response = this.proposalService.getById(proposalId);

        assertEquals(proposalId, response.getId());
        assertEquals(proposalDocument.getTitle(), response.getTitle());
        assertEquals(proposalDocument.getDescription(), response.getDescription());

        verify(proposalRepository, times(1)).findById(anyString());
    }

    @Test
    public void getProposalById_WhenNotFound() {
        String proposalId = "123abc456def";

        when(proposalRepository.findById(anyString())).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class,
                () -> proposalService.getById(proposalId));

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getCode().getStatusCode());
        assertEquals(ErrorCode.PROPOSAL_NOT_FOUND.getMessage(), exception.getCode().getMessage());

        verify(proposalRepository, times(1)).findById(anyString());
    }

    @Test
    public void openVotingSession() {
        String proposalId = "123abc456def";

        SessionDTO sessionDTO = SessionDTO.builder()
                .id("123abc456dee")
                .durationInMinutes(1)
                .build();

        when(proposalRepository.findById(anyString())).thenReturn(Optional.of(new ProposalDocument()));
        when(sessionService.hasOpenSessionForProposal(anyString())).thenReturn(false);
        when(sessionService.save(anyString(), any(SessionDTO.class))).thenReturn(sessionDTO);
        SessionDTO response = this.proposalService.openVotingSession(proposalId, sessionDTO);

        assertNotNull(response.getId());

        verify(proposalRepository, times(1)).findById(anyString());
        verify(sessionService, times(1)).hasOpenSessionForProposal(anyString());
        verify(sessionService, times(1)).save(anyString(), any(SessionDTO.class));
    }

    @Test
    public void openVotingSession_WhenSessionAlreadyOpenForProposal() {
        String proposalId = "123abc456def";

        SessionDTO sessionDTO = SessionDTO.builder()
                .id("123abc456deb")
                .durationInMinutes(1)
                .build();

        when(proposalRepository.findById(anyString())).thenReturn(Optional.of(new ProposalDocument()));
        when(sessionService.hasOpenSessionForProposal(anyString())).thenReturn(true);
        BusinessException exception = assertThrows(BusinessException.class,
                () -> proposalService.openVotingSession(proposalId, sessionDTO));

        assertEquals(HttpStatus.CONFLICT.value(), exception.getCode().getStatusCode());
        assertEquals(ErrorCode.SESSION_ALREADY_OPEN_FOR_THIS_PROPOSAL.getMessage(), exception.getCode().getMessage());

        verify(proposalRepository, times(1)).findById(anyString());
        verify(sessionService, times(1)).hasOpenSessionForProposal(anyString());
        verify(sessionService, never()).save(anyString(), any(SessionDTO.class));
    }

    @Test
    public void registerVote() {
        String proposalId = "123abc456def";

        MemberDTO memberDTO = MemberDTO.builder()
                .id("123abc456deg")
                .name("Beatriz Rodrigues Gomes")
                .CPF("11113719702")
                .build();

        SessionDTO sessionDTO = SessionDTO.builder()
                .id("123abc456det")
                .durationInMinutes(1)
                .build();

        VoteDTO voteDTO = VoteDTO.builder()
                .id("123abc456dev")
                .memberId("657hij654tuv")
                .sessionId("987xyz654mno")
                .favorable(true)
                .build();

        when(proposalRepository.findById(anyString())).thenReturn(Optional.of(new ProposalDocument()));
        when(memberService.getById(anyString())).thenReturn(memberDTO);
        when(validatorClient.CPFValidator(anyString())).thenReturn(CPFValidationStatusEnum.ABLE_TO_VOTE);
        when(sessionService.getOpenSessionForProposal(anyString())).thenReturn(sessionDTO);
        doNothing().when(voteService).validateMemberHasNotVoted(anyString(), anyString());
        when(voteService.save(any(VoteDTO.class))).thenReturn(voteDTO);

        VoteDTO response = this.proposalService.registerVote(proposalId, voteDTO);

        assertNotNull(response.getId());

        verify(proposalRepository, times(1)).findById(anyString());
        verify(memberService, times(1)).getById(anyString());
        verify(validatorClient, times(1)).CPFValidator(anyString());
        verify(sessionService, times(1)).getOpenSessionForProposal(anyString());
        verify(voteService, times(1)).validateMemberHasNotVoted(anyString(), anyString());
        verify(voteService, times(1)).save(any(VoteDTO.class));
    }

    @Test
    public void registerVote_WhenMemberIsUnableToVote() {
        String proposalId = "123abc456def";

        MemberDTO memberDTO = MemberDTO.builder()
                .id("123abc456det")
                .name("Beatriz Rodrigues Gomes")
                .CPF("11113719702")
                .build();

        VoteDTO voteDTO = VoteDTO.builder()
                .id("123abc456deh")
                .memberId("657hij654tuv")
                .sessionId("987xyz654mno")
                .favorable(true)
                .build();

        when(proposalRepository.findById(anyString())).thenReturn(Optional.of(new ProposalDocument()));
        when(memberService.getById(anyString())).thenReturn(memberDTO);
        when(validatorClient.CPFValidator(anyString())).thenReturn(CPFValidationStatusEnum.UNABLE_TO_VOTE);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> proposalService.registerVote(proposalId, voteDTO));

        assertEquals(HttpStatus.FORBIDDEN.value(), exception.getCode().getStatusCode());
        assertEquals(ErrorCode.MEMBER_UNABLE_TO_VOTE.getMessage(), exception.getCode().getMessage());

        verify(proposalRepository, times(1)).findById(anyString());
        verify(memberService, times(1)).getById(anyString());
        verify(validatorClient, times(1)).CPFValidator(anyString());
        verify(sessionService, never()).getOpenSessionForProposal(anyString());
        verify(voteService, never()).validateMemberHasNotVoted(anyString(), anyString());
        verify(voteService, never()).save(any(VoteDTO.class));
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

        when(proposalRepository.findById(anyString())).thenReturn(Optional.of(new ProposalDocument()));
        when(sessionService.getLatestSessionForProposal(anyString())).thenReturn(sessionDTO);
        SessionDTO response = this.proposalService.getVotingResult(proposalId);

        assertEquals(sessionDTO.getStartTime(), response.getStartTime());
        assertEquals(sessionDTO.getEndTime(), response.getEndTime());
        assertEquals(sessionDTO.getTotalVotes(), response.getTotalVotes());
        assertEquals(sessionDTO.getTotalFavorable(), response.getTotalFavorable());
        assertEquals(sessionDTO.getTotalOpposed(), response.getTotalOpposed());
        assertEquals(sessionDTO.getStatus(), response.getStatus());

        verify(proposalRepository, times(1)).findById(anyString());
        verify(sessionService, times(1)).getLatestSessionForProposal(anyString());
        verify(sessionService, never()).buildVotingSessionResult(any(SessionDTO.class), anyInt(), anyInt());
    }

    @Test
    public void getVotingResult_WhenCountForTheFirstTime() {
        String proposalId = "123abc456def";

        SessionDTO sessionDTO = SessionDTO.builder()
                .id("123abc456deh")
                .startTime(LocalDateTime.now().minusHours(2))
                .endTime(LocalDateTime.now().minusHours(1))
                .build();

        SessionDTO sessionDTOAfterCount = SessionDTO.builder()
                .startTime(LocalDateTime.now().minusHours(2))
                .endTime(LocalDateTime.now().minusHours(1))
                .totalVotes(50)
                .totalFavorable(45)
                .totalOpposed(5)
                .status(VotingResultStatusEnum.APPROVED)
                .build();

        Map<String, Integer> totalVotes = new HashMap<>();
        totalVotes.put("favorable", 45);
        totalVotes.put("opposed", 5);

        when(proposalRepository.findById(anyString())).thenReturn(Optional.of(new ProposalDocument()));
        when(sessionService.getLatestSessionForProposal(anyString())).thenReturn(sessionDTO);
        when(voteService.countVotesForSession(anyString())).thenReturn(totalVotes);
        when(sessionService.buildVotingSessionResult(any(SessionDTO.class), anyInt(), anyInt())).thenReturn(sessionDTOAfterCount);
        SessionDTO response = this.proposalService.getVotingResult(proposalId);

        assertEquals(sessionDTOAfterCount.getStartTime(), response.getStartTime());
        assertEquals(sessionDTOAfterCount.getEndTime(), response.getEndTime());
        assertEquals(sessionDTOAfterCount.getTotalVotes(), response.getTotalVotes());
        assertEquals(sessionDTOAfterCount.getTotalFavorable(), response.getTotalFavorable());
        assertEquals(sessionDTOAfterCount.getTotalOpposed(), response.getTotalOpposed());
        assertEquals(sessionDTOAfterCount.getStatus(), response.getStatus());

        verify(proposalRepository, times(1)).findById(anyString());
        verify(sessionService, times(1)).getLatestSessionForProposal(anyString());
        verify(voteService, times(1)).countVotesForSession(anyString());
        verify(sessionService, times(1)).buildVotingSessionResult(any(SessionDTO.class), anyInt(), anyInt());
    }
}
