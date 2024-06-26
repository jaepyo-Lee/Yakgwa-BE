package com.prography.yakgwa.domain.user.entity;

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
@Table(indexes = @Index(name = "auth_type",columnList = "auth_type"))
public class User {
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
}
