package com.demo.loopleTalk.service.member;

import com.demo.loopleTalk.domain.member.Member;
import com.demo.loopleTalk.dto.member.AddMemberRequest;
import com.demo.loopleTalk.dto.member.UpdateMemberRequest;
import com.demo.loopleTalk.repository.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member create(AddMemberRequest addMemberRequest) {
        return memberRepository.save(addMemberRequest.of());
    }

    public Member find(String email) {
        return memberRepository.findByEmail(email);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Transactional
    public Member update(String email, UpdateMemberRequest request) {
        Member member = memberRepository.findByEmail(email);
        member.update(
                request.getName(),
                request.getPassword(),
                request.getPhone(),
                request.getBirth()
        );
        return member;
    }
}
