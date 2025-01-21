package com.demo.loopleTalk.domain.profile;

import com.demo.loopleTalk.domain.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profileId", updatable = false)
    private Long profileId;

    @NotNull
    private String nickname;

    @Column(nullable = false, length = 4)
    private String mbti;

    @NotNull
    private String job;

    @NotNull
    private boolean gender; // 0(남) or 1(여)

    @NotNull
    private String location;

    @Column(length = 600)
    private String intro;

    @NotNull
    private double positionX;

    @NotNull
    private double positionY;

    @NotNull
    @CreatedDate
    private LocalDateTime createdAt;

    @NotNull
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "memberId")
    private Member member;

    @Builder
    public Profile(String nickname, String mbti, String job, boolean gender, String location, String intro, double positionX, double positionY, Member member) {
        this.nickname = nickname;
        this.mbti = mbti;
        this.job = job;
        this.gender = gender;
        this.location = location;
        this.intro = intro;
        this.positionX = positionX;
        this.positionY = positionY;
        this.member = member;
    }

    public void update(String nickname, String mbti, String job, String location, String intro, double positionX, double positionY) {
        this.nickname = nickname;
        this.mbti = mbti;
        this.job = job;
        this.location = location;
        this.intro = intro;
        this.positionX = positionX;
        this.positionY = positionY;
    }
}
