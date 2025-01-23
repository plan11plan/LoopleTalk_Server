package com.demo.loopleTalk.dto.member;

import com.demo.loopleTalk.domain.member.Member;
import lombok.Getter;

@Getter
public class MembersResponse {

    private Long memberId;
    private String name;
    private String email;

    public MembersResponse(Member member) {
        this.memberId = member.getMemberId();
        this.name = member.getName();
        this.email = member.getEmail();
    }
}
