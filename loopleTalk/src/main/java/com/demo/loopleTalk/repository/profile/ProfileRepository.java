package com.demo.loopleTalk.repository.profile;

import com.demo.loopleTalk.domain.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
