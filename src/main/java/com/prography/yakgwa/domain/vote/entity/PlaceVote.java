package com.prography.yakgwa.domain.vote.entity;

import com.prography.yakgwa.domain.place.entity.Place;
import jakarta.persistence.*;

@Entity
public class PlaceVote {
    @Id
    private Long id;
    private Long voteCnt;
    @OneToOne
    @JoinColumn(name = "place_id")
    private Place place;

}
