package com.prography.yakgwa.domain.meet.entity;

import com.prography.yakgwa.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "MEET_TABLE")
public class Meet extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;
    private String description;
    private int inviteValidHour;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne
    @JoinColumn(name = "meet_theme_id")
    private MeetTheme meetTheme;
}
