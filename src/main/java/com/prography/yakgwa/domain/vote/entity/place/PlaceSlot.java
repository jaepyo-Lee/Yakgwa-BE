package com.prography.yakgwa.domain.vote.entity.place;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.vote.entity.Slot;
import com.prography.yakgwa.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.Objects;

import static java.lang.Boolean.TRUE;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "PLACESLOT_TABLE")
@DiscriminatorValue("place")
public class PlaceSlot extends Slot {
/*
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean confirm;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meet_id")
    private Meet meet;*/

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    public boolean isSamePlaceSlot(String title, String mapx, String mapy) {
        return place.isSamePlace(title, mapx, mapy);
    }

    /*public void confirm() {
        confirm = true;
    }*/

    public boolean isEqualsMeet(Long meetId) {
        return super.getMeet().isSameId(meetId);
    }

    public PlaceSlot(Long id) {
        super(id);
    }

    @Builder
    public PlaceSlot(Boolean confirm, Meet meet, Place place) {
        super(confirm, meet);
        this.place = place;
    }

/*    public static PlaceSlot of(Meet meet, Boolean confirm, Place place) {
        return PlaceSlot.builder()
                .meet(meet)
                .confirm(confirm)
                .place(place)
                .build();
    }*/

    @Override
    public boolean equals(Object obj) {
        PlaceSlot placeSlot = (PlaceSlot) obj;
        return super.getId().equals(placeSlot.getId());
    }
/*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        PlaceSlot placeSlot = (PlaceSlot) o;
        return Objects.equals(id, placeSlot.id);
    }*/

 /*   public boolean isConfirm() {
        return this.confirm.equals(TRUE);
    }*/
}
