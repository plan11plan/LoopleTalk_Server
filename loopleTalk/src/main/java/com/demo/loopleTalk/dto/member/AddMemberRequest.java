package com.demo.loopleTalk.dto.member;

import com.demo.loopleTalk.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddMemberRequest {

    private String name;
    private String email;
    private String password;
    private String phone;
    private LocalDate birth;

    public Member of() {
        return Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .phone(phone)
                .birth(birth)
                .build();
    }
}
