package com.jointest.join.controller;

import com.jointest.join.dto.SignUpFormDTO;
import com.jointest.join.service.interfaces.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity userSignup(@RequestBody SignUpFormDTO formDTO) {
        return memberService.signup(formDTO);
    }

}