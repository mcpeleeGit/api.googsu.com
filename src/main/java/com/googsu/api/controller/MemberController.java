package com.googsu.api.controller;

import com.googsu.api.dto.MemberDto;
import com.googsu.api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<MemberDto> signup(@RequestBody MemberDto memberDto) {
        return ResponseEntity.ok(memberService.signup(memberDto));
    }
}