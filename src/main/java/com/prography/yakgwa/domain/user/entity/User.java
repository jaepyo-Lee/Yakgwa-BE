package com.prography.yakgwa.domain.user.entity;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private AuthType authType;
    private String authId;

    private Boolean isNew;
    private String imageUrl;
    private String fcmToken;
}
