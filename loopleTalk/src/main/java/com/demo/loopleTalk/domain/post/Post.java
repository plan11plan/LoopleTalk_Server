package com.demo.loopleTalk.domain.post;


import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id
    @Nullable
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private long profileId;

    @NotBlank(message = "내용은 필수입니다")
    @Size(min = 1, max = 1000, message = "내용은 1자 이상 1000자 이하여야 합니다")
    private String content;

    private double longitude;

    private double latitude;

    @Builder
    public Post(Long postId, long profileId, String content, double longitude, double latitude) {
        this.postId = postId;
        this.profileId = profileId;
        this.content = content;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
