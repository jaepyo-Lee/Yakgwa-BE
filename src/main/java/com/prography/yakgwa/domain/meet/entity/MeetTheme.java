package com.prography.yakgwa.domain.meet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "MEET_THEME_TABLE")
public class MeetTheme {
    @Id
    private Long id;
    private String name;
}
