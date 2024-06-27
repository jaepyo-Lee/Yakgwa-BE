package com.prography.yakgwa.domain.vote.entity;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PLACE_VOTE_TABLE")
public class PlaceVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long voteCnt;
    private Boolean confirm;
    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "meet_id")
    private Meet meet;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
