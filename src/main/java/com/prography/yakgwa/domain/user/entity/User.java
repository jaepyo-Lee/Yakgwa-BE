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
@Entity(name = "USER_TABLE")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private AuthType authType;
    private String  authId;
    private String name;
    private Boolean isNew;
    private String imageUrl;
    private String fcmToken;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role= Role.ROLE_USER;

    public void changeImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
