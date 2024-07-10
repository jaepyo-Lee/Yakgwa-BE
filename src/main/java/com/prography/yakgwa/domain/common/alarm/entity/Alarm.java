package com.prography.yakgwa.domain.common.alarm.entity;

import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Alarm extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;
    private LocalDateTime sendTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
