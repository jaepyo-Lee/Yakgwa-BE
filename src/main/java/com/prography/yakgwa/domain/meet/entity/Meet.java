package com.prography.yakgwa.domain.meet.entity;

import com.prography.yakgwa.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class Meet extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;
    private String description;
    private int endHour;
    private LocalDate from;
    private LocalDate to;
    @ManyToOne
    @JoinColumn(name = "meet_theme_id")
    private MeetTheme meetTheme;
}
