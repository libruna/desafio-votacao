package com.desafio.code.service;

import com.desafio.code.dto.MemberDTO;
import com.desafio.code.exception.BusinessException;
import com.desafio.code.exception.ErrorCode;
import com.desafio.code.model.MemberDocument;
import com.desafio.code.repository.MemberRepository;
import com.desafio.code.service.impl.MemberServiceImpl;
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
public class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    public void save() {
        MemberDocument memberDocument = new MemberDocument();
        memberDocument.setId("123abc456def");
        memberDocument.setName("Beatriz Rodrigues Gomes");
        memberDocument.setCPF("11113719702");

        MemberDTO memberDTO = MemberDTO.builder()
                .name("Beatriz Rodrigues Gomes")
                .CPF("11113719702")
                .build();

        when(memberRepository.findMemberDocumentByCPF(anyString())).thenReturn(Optional.empty());
        when(memberRepository.save(any(MemberDocument.class))).thenReturn(memberDocument);
        MemberDTO response = this.memberService.save(memberDTO);

        assertEquals(memberDocument.getId(), response.getId());

        verify(memberRepository, times(1)).findMemberDocumentByCPF(anyString());
        verify(memberRepository, times(1)).save(any(MemberDocument.class));
    }

    @Test
    public void save_WhenMemberAlreadyExists() {
        MemberDocument memberDocument = new MemberDocument();
        memberDocument.setId("123abc456def");
        memberDocument.setName("Beatriz Rodrigues Gomes");
        memberDocument.setCPF("11113719702");

        MemberDTO memberDTO = MemberDTO.builder()
                .name("Beatriz Rodrigues Gomes")
                .CPF("11113719702")
                .build();

        when(memberRepository.findMemberDocumentByCPF(anyString())).thenReturn(Optional.of(memberDocument));
        BusinessException exception = assertThrows(BusinessException.class,
                () -> memberService.save(memberDTO));

        assertEquals(HttpStatus.CONFLICT.value(), exception.getCode().getStatusCode());
        assertEquals(ErrorCode.MEMBER_ALREADY_EXISTS.getMessage(), exception.getCode().getMessage());

        verify(memberRepository, times(1)).findMemberDocumentByCPF(anyString());
        verify(memberRepository, never()).save(any(MemberDocument.class));
    }

    @Test
    public void getMemberById() {
        String memberId = "123abc456def";

        MemberDocument memberDocument = new MemberDocument();
        memberDocument.setId("123abc456def");
        memberDocument.setName("Beatriz Rodrigues Gomes");
        memberDocument.setCPF("11113719702");

        when(memberRepository.findById(anyString())).thenReturn(Optional.of(memberDocument));
        MemberDTO response = this.memberService.getById(memberId);

        assertEquals(memberId, response.getId());
        assertEquals(memberDocument.getName(), response.getName());
        assertEquals(memberDocument.getCPF(), response.getCPF());

        verify(memberRepository, times(1)).findById(anyString());
    }

    @Test
    public void getMemberById_WhenMemberNotFound() {
        String memberId = "123abc456def";

        when(memberRepository.findById(anyString())).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class,
                () -> memberService.getById(memberId));

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getCode().getStatusCode());
        assertEquals(ErrorCode.MEMBER_NOT_FOUND.getMessage(), exception.getCode().getMessage());

        verify(memberRepository, times(1)).findById(anyString());
    }
}
