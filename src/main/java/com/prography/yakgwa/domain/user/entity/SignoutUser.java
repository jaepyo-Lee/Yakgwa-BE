package com.prography.yakgwa.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "SIGNOUT_USER_TABLE")
public class SignoutUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AuthType authType;
    private String authId;
    private String name;
    private Boolean isNew;
    private String imageUrl;
    private String fcmToken;
    private Long userId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;
}
