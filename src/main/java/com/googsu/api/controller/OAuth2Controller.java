package com.googsu.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuth2Controller {

    @GetMapping("/oauth2/authorization/kakao")
    public String kakaoLogin() {
        return "redirect:/oauth2/authorization/kakao";
    }
}