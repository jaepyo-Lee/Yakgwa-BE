package com.prography.yakgwa.domain.meet.entity;

import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "MEET_TABLE")
public class Meet extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String title;

    private int validInviteHour;

    @Embedded
    private VotePeriod period;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meet_theme_id")
    private MeetTheme meetTheme;
}
