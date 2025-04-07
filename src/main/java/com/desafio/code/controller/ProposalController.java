package com.desafio.code.controller;

import com.desafio.code.service.ProposalService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/proposals")
@AllArgsConstructor
public class ProposalController {

    private final ProposalService proposalService;

}
