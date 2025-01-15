package com.demo.loopleTalk.domain.profile;

import com.demo.loopleTalk.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profileId", updatable = false)
    private Long profileId;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "mbti", nullable = false, length = 4)
    private String mbti;

    @Column(name = "job", nullable = false)
    private String job;

    @Column(name = "gender", nullable = false)
    private boolean gender; // 0(남) or 1(여)

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "intro")
    private String intro;

    @Column(name = "positionX", nullable = false)
    private double positionX;

    @Column(name = "positionY", nullable = false)
    private double positionY;

    @CreatedDate
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "profile")
    private Member member;

    @Builder
    public Profile(String nickname, String mbti, String job, boolean gender, String location, String intro, double positionX, double positionY) {
        this.nickname = nickname;
        this.mbti = mbti;
        this.job = job;
        this.gender = gender;
        this.location = location;
        this.intro = intro;
        this.positionX = positionX;
        this.positionY = positionY;
    }
}
