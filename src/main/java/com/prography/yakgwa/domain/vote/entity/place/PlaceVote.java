package com.prography.yakgwa.domain.vote.entity.place;

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

    @ManyToOne
    @JoinColumn(name = "placeslot_id")
    private PlaceSlot placeSlot;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
