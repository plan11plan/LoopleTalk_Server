package com.demo.loopleTalk.service.profile;

import com.demo.loopleTalk.domain.member.Member;
import com.demo.loopleTalk.domain.profile.Profile;
import com.demo.loopleTalk.dto.profile.AddProfileRequest;
import com.demo.loopleTalk.repository.member.MemberRepository;
import com.demo.loopleTalk.repository.profile.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProfileService {

    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;

    public Profile create(AddProfileRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member Not Found : " + request.getMemberId()));
        return profileRepository.save(request.of(member));
    }

    public Profile find(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile Not Found : " + id));
    }

    public List<Profile> findAll() {
        return profileRepository.findAll();
    }

    public void delete(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile Not Found : " + id));
        profileRepository.delete(profile);
    }
}
