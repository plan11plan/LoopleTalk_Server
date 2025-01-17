package com.demo.loopleTalk.dto.member;

import com.demo.loopleTalk.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateMemberRequest {

    private String name;
    private String password;
    private String phone;
    private LocalDate birth;

    public UpdateMemberRequest(Member member) {
        this.name = member.getName();
        this.password = member.getPassword();
        this.phone = member.getPhone();
        this.birth = member.getBirth();
    }
}
