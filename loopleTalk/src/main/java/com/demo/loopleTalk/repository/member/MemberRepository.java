package com.demo.loopleTalk.repository.member;

import com.demo.loopleTalk.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
