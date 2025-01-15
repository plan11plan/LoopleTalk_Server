package com.demo.loopleTalk.service.member;

import com.demo.loopleTalk.domain.member.Member;
import com.demo.loopleTalk.dto.member.AddMemberRequest;
import com.demo.loopleTalk.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member create(AddMemberRequest addMemberRequest) {
        return memberRepository.save(addMemberRequest.of());
    }
}
