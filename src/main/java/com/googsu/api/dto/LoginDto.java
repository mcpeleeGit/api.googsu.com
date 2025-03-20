package com.googsu.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class LoginDto {
    @Getter
    @Setter
    public static class Request {
        @NotBlank(message = "이메일은 필수입니다")
        @Email(message = "올바른 이메일 형식이 아닙니다")
        private String email;

        @NotBlank(message = "비밀번호는 필수입니다")
        private String password;
    }

    @Getter
    @Setter
    public static class Response {
        private String token;
        private Long id;
        private String email;
        private String name;

        public Response(String token, Long id, String email, String name) {
            this.token = token;
            this.id = id;
            this.email = email;
            this.name = name;
        }
    }
}