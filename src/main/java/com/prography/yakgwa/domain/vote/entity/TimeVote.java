package com.prography.yakgwa.domain.vote.entity;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "TIME_VOTE_TABLE")
public class TimeVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "meet_id")
    private Meet meet;
}
