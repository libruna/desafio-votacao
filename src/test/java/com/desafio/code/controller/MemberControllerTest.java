package com.desafio.code.controller;

import com.desafio.code.dto.MemberDTO;
import com.desafio.code.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    @Test
    public void registerMember() {
        MemberDTO memberDTO = MemberDTO.builder()
                .id("123abc456def")
                .name("Beatriz Rodrigues Gomes")
                .CPF("11113719702")
                .build();

        when(memberService.save(any(MemberDTO.class))).thenReturn(memberDTO);
        ResponseEntity<MemberDTO> response = memberController.registerMember(memberDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        verify(memberService, times(1)).save(any(MemberDTO.class));
    }

    @Test
    public void getMember() {
        String memberId = "123abc456def";

        MemberDTO memberDTO = MemberDTO.builder()
                .id("123abc456def")
                .name("Beatriz Rodrigues Gomes")
                .CPF("11113719702")
                .build();

        when(memberService.getById(anyString())).thenReturn(memberDTO);
        ResponseEntity<MemberDTO> response = memberController.getMember(memberId);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(memberDTO.getName(), response.getBody().getName());
        assertEquals(memberDTO.getCPF(), response.getBody().getCPF());

        verify(memberService, times(1)).getById(anyString());
    }
}
