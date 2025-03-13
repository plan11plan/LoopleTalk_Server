package com.demo.loopleTalk.dto.profile;

import com.demo.loopleTalk.domain.profile.Profile;
import lombok.Getter;

@Getter
public class ProfilesResponse {

    private Long profileId;
    private String nickname;
    private boolean gender;
    private String location;

    public ProfilesResponse(Profile profile) {
        this.profileId = profile.getProfileId();
        this.nickname = profile.getNickname();
        this.gender = profile.isGender();
        this.location = profile.getLocation();
    }
}
