package com.prography.yakgwa.domain.meet.entity;

import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

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
    private String description;
    private int validInviteHour;
    @Embedded
    private VotePeriod period;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meet_theme_id")
    private MeetTheme meetTheme;

    public LocalDateTime getVoteTime() {
        return this.getCreatedDate().plusHours(this.validInviteHour);
    }

    public boolean isConfirmTimeEnd() {
        return getConfirmTime().isBefore(LocalDateTime.now());
    }

    private LocalDateTime getConfirmTime() {
        return getVoteTime().plusHours(24);
    }

    public boolean isVoteTimeEnd() {
        return getVoteTime().isBefore(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Meet meet = (Meet) o;
        return Objects.equals(id, meet.id);
    }

    public boolean isWithinVotePeriod(LocalDateTime targetTime) {
        return getVoteEndDate().isBefore(targetTime.toLocalDate()) || getVoteStartDate().isAfter(targetTime.toLocalDate());
    }

    public boolean isSameId(Long id) {
        return this.id.equals(id);
    }

    public String getThemeName() {
        return meetTheme.getName();
    }

    public LocalDate getVoteStartDate() {
        return period.getStartDate();
    }

    public LocalDate getVoteEndDate() {
        return period.getEndDate();
    }
}
