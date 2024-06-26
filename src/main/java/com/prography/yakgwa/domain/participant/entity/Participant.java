package com.prography.yakgwa.domain.participant.entity;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.user.entity.User;
import jakarta.persistence.*;

@Entity(name = "PARTICIPANT_TABLE")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private MeetRole meetRole;
    @ManyToOne
    @JoinColumn(name = "meet_id")
    private Meet meet;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
