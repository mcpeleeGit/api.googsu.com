package com.googsu.api.security;

import com.googsu.api.domain.Member;
import com.googsu.api.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

        private final JwtTokenProvider jwtTokenProvider;
        private final MemberRepository memberRepository;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {
                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttribute("kakao_account");
                Map<String, Object> properties = (Map<String, Object>) oAuth2User.getAttribute("properties");

                String email = (String) kakaoAccount.get("email");
                String name = (String) properties.get("nickname");

                Member member = memberRepository.findByEmail(email)
                                .orElseGet(() -> {
                                        Member newMember = Member.builder()
                                                        .email(email)
                                                        .name(name)
                                                        .build();
                                        return memberRepository.save(newMember);
                                });

                String token = jwtTokenProvider.createToken(member.getId(), member.getEmail());
                String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000")
                                .queryParam("token", token)
                                .build().toUriString();

                getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
}