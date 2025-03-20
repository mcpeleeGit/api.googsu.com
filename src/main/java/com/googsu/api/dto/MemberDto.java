package com.googsu.api.dto;

import com.googsu.api.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
    private Long id;
    private String email;
    private String password;
    private String name;

    public static MemberDto from(Member member) {
        MemberDto dto = new MemberDto();
        dto.setId(member.getId());
        dto.setEmail(member.getEmail());
        dto.setName(member.getName());
        return dto;
    }

    @Getter
    @Setter
    public static class SignUpRequest {
        @NotBlank(message = "이메일은 필수입니다")
        @Email(message = "올바른 이메일 형식이 아닙니다")
        private String email;

        @NotBlank(message = "비밀번호는 필수입니다")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
        private String password;

        @NotBlank(message = "이름은 필수입니다")
        private String name;
    }

    @Getter
    @Setter
    public static class SignUpResponse {
        private Long id;
        private String email;
        private String name;

        public SignUpResponse(Long id, String email, String name) {
            this.id = id;
            this.email = email;
            this.name = name;
        }
    }
}