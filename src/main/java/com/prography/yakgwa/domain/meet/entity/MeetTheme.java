package com.prography.yakgwa.domain.meet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class MeetTheme {
    @Id
    private Long id;
    private String name;
}
