package com.prography.yakgwa.domain.meet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity(name = "MEET_THEME_TABLE")
public class MeetTheme {
    @Id
    private Long id;
    private String name;
}
