package com.googsu.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2Controller {

    @GetMapping("/oauth2/redirect")
    public String oauth2Redirect(@RequestParam String token) {
        return "Login successful! Token: " + token;
    }
}