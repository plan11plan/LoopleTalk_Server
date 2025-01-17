package com.demo.loopleTalk.dto.profile;

import com.demo.loopleTalk.domain.profile.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateProfileRequest {

    private String nickname;
    private String mbti;
    private String job;
    private String location;
    private String intro;
    private double positionX;
    private double positionY;

    public UpdateProfileRequest(Profile profile) {
        this.nickname = profile.getNickname();
        this.mbti = profile.getMbti();
        this.job = profile.getJob();
        this.location = profile.getLocation();
        this.intro = profile.getIntro();
        this.positionX = profile.getPositionX();
        this.positionY = profile.getPositionY();
    }
}
