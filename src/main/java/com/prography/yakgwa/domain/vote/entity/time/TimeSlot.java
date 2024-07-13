package com.prography.yakgwa.domain.vote.entity.time;

import com.prography.yakgwa.domain.meet.entity.Meet;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity(name = "TIMESLOT_TABLE")
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime time;
    private Boolean confirm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meet_id")
    private Meet meet;

    public void confirm() {
        confirm = true;
    }
}
