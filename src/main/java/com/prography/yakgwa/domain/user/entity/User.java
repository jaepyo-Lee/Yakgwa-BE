package com.prography.yakgwa.domain.user.entity;

import com.prography.yakgwa.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {@Index(name = "authId_idx", columnList = "authId")})
@Entity(name = "USER_TABLE")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Enumerated(EnumType.STRING)
    private AuthType authType;
    @Getter
    private String  authId;
    @Getter
    private String name;
    @Getter
    private String imageUrl;
    @Getter
    private String fcmToken;

    @Getter
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role= Role.ROLE_USER;

    public void changeImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void signout(String baseImage){
        this.authType = null;
        this.authId = null;
        this.imageUrl = baseImage;
        this.fcmToken = null;
        this.role = null;
    }
    public void refreshFcm(String fcmToken){
        this.fcmToken = fcmToken;
    }
}
