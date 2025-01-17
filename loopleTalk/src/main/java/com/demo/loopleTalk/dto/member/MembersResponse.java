package com.demo.loopleTalk.dto.member;

import com.demo.loopleTalk.domain.member.Member;
import lombok.Getter;

@Getter
public class MembersResponse {

    private String name;
    private String email;

    public MembersResponse(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
    }
}
