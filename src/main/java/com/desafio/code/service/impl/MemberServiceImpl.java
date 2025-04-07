package com.desafio.code.service.impl;

import com.desafio.code.dto.MemberDTO;
import com.desafio.code.exception.BusinessException;
import com.desafio.code.exception.ErrorCode;
import com.desafio.code.model.MemberDocument;
import com.desafio.code.repository.MemberRepository;
import com.desafio.code.service.MemberService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.modelMapper = new ModelMapper();
        this.memberRepository = memberRepository;
    }

    @Override
    public MemberDTO save(MemberDTO memberDTO) {
        Optional<MemberDocument> document = memberRepository.findMemberDocumentByCPF(memberDTO.getCPF());
        if (document.isPresent()) {
            throw new BusinessException(ErrorCode.MEMBER_ALREADY_EXISTS);
        }

        MemberDocument memberDocument = modelMapper.map(memberDTO, MemberDocument.class);
        return MemberDTO.builder()
                .id(memberRepository.save(memberDocument).getId())
                .build();
    }

    @Override
    public MemberDTO getById(String id) {
        Optional<MemberDocument> document = memberRepository.findById(id);
        if (document.isEmpty()) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return modelMapper.map(document, MemberDTO.class);
    }
}
