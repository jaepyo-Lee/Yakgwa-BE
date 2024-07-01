package com.prography.yakgwa.domain.vote.entity.time;

import com.prography.yakgwa.domain.meet.entity.Meet;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "TIMESLOT_TABLE")
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime time;
    private boolean confirm;

    @ManyToOne
    @JoinColumn(name = "meet_id")
    private Meet meet;
}
