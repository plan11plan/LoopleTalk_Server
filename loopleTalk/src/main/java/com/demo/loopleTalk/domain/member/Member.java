package com.demo.loopleTalk.domain.member;

import com.demo.loopleTalk.domain.profile.Profile;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId", updatable = false)
    private Long memberId;

    @NotNull
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    private String password;

    @Column(nullable = false, unique = true)
    private String phone;

    @NotNull
    private LocalDate birth;

    @NotNull
    @CreatedDate
    private LocalDateTime createdAt;

    @NotNull
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Setter
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "profileId")
    private Profile profile;

    @Builder
    public Member(String name, String email, String password, String phone, LocalDate birth) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.birth = birth;
    }

    public void update(String name, String password, String phone, LocalDate birth) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.birth = birth;
    }
}
