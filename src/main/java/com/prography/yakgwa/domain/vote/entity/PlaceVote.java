package com.prography.yakgwa.domain.vote.entity;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.user.entity.User;
import jakarta.persistence.*;

@Entity(name = "PLACE_VOTE_TABLE")
public class PlaceVote {
    @Id
    private Long id;
    private Long voteCnt;

    @OneToOne
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "meet_id")
    private Meet meet;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
