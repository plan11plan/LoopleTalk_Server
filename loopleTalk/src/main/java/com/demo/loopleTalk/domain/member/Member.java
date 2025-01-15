package com.demo.loopleTalk.domain.member;

import com.demo.loopleTalk.domain.profile.Profile;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId", updatable = false)
    private Long memberId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @CreatedDate
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "member")
    private Profile profile;

    @Builder
    public Member(String name, String email, String password, String phone, LocalDate birth) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.birth = birth;
    }
}
