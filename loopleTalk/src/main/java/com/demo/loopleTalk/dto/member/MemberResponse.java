package com.demo.loopleTalk.dto.member;

import com.demo.loopleTalk.domain.member.Member;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MemberResponse {

    private String name;
    private String email;
    private String password;
    private String phone;
    private LocalDate birth;

    public MemberResponse(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.phone = member.getPhone();
        this.birth = member.getBirth();
    }
}
