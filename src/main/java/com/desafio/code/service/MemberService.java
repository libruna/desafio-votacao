package com.desafio.code.service;

import com.desafio.code.dto.MemberDTO;

public interface MemberService {

    MemberDTO save(MemberDTO memberDTO);
    MemberDTO getById(String id);
}
