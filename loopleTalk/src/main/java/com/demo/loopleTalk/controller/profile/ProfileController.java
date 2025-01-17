package com.demo.loopleTalk.controller.profile;

import com.demo.loopleTalk.domain.profile.Profile;
import com.demo.loopleTalk.dto.profile.AddProfileRequest;
import com.demo.loopleTalk.dto.profile.ProfileResponse;
import com.demo.loopleTalk.service.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<Profile> createProfile(@RequestBody AddProfileRequest request) {
        Profile profile = profileService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profile);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> findProfile(@PathVariable(name = "id") Long id) {
        Profile profile = profileService.find(id);
        return ResponseEntity.ok()
                .body(new ProfileResponse(profile));
    }
}
