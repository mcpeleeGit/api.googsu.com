package com.googsu.api.service;

import com.googsu.api.domain.Member;
import com.googsu.api.dto.MemberDto;
import com.googsu.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberDto signup(MemberDto memberDto) {
        if (memberRepository.findByEmail(memberDto.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        Member member = Member.builder()
                .email(memberDto.getEmail())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .name(memberDto.getName())
                .build();

        Member savedMember = memberRepository.save(member);
        return MemberDto.from(savedMember);
    }
}