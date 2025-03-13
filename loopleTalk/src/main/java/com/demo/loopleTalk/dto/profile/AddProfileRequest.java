package com.demo.loopleTalk.dto.profile;

import com.demo.loopleTalk.domain.member.Member;
import com.demo.loopleTalk.domain.profile.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddProfileRequest {

    private String nickname;
    private String mbti;
    private String job;
    private boolean gender;
    private String location;
    private String intro;
    private double positionX;
    private double positionY;
    private Long memberId;

    public Profile of() {
        return Profile.builder()
                .nickname(nickname)
                .mbti(mbti)
                .job(job)
                .gender(gender)
                .location(location)
                .intro(intro)
                .positionX(positionX)
                .positionY(positionY)
                .build();
    }
}
