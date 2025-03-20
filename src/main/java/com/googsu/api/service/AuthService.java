package com.googsu.api.service;

import com.googsu.api.domain.Member;
import com.googsu.api.dto.LoginResponse;
import com.googsu.api.repository.MemberRepository;
import com.googsu.api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final MemberRepository memberRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtTokenProvider jwtTokenProvider;
        private final AuthenticationManager authenticationManager;

        public LoginResponse login(String email, String password) {
                try {
                        Authentication authentication = authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(email, password));

                        Member member = memberRepository.findByEmail(email)
                                        .orElseThrow(() -> new UsernameNotFoundException(
                                                        "User not found with email: " + email));

                        String token = jwtTokenProvider.createToken(member.getId(), member.getEmail());

                        return new LoginResponse(
                                        token,
                                        member.getId(),
                                        member.getEmail(),
                                        member.getName());
                } catch (AuthenticationException e) {
                        throw new BadCredentialsException("Invalid email or password");
                }
        }
}