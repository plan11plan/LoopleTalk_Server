package com.demo.loopleTalk.dto.profile;

import com.demo.loopleTalk.domain.profile.Profile;
import lombok.Getter;

@Getter
public class ProfileResponse {

    private String nickname;
    private String mbti;
    private String job;
    private boolean gender;
    private String location;
    private String intro;
    private double positionX;
    private double positionY;

    public ProfileResponse(Profile profile) {
        this.nickname = profile.getNickname();
        this.mbti = profile.getMbti();
        this.job = profile.getJob();
        this.gender = profile.isGender();
        this.location = profile.getLocation();
        this.intro = profile.getIntro();
        this.positionX = profile.getPositionX();
        this.positionY = profile.getPositionY();
    }
}
