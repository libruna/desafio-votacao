package com.desafio.code.controller;

import com.desafio.code.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

}
