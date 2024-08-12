package com.prography.yakgwa.domain.vote.entity.place;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.place.entity.Place;
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
@Builder
@Getter
@Entity(name = "PLACESLOT_TABLE")
public class PlaceSlot extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean confirm;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meet_id")
    private Meet meet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    public void confirm() {
        confirm = true;
    }


    public static PlaceSlot of(Meet meet, Boolean confirm, Place place) {
        return PlaceSlot.builder()
                .meet(meet)
                .confirm(confirm)
                .place(place)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        PlaceSlot placeSlot = (PlaceSlot) o;
        return Objects.equals(id, placeSlot.id);
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }

    public boolean isSamePlace(String title, String mapx, String mapy) {
        return isTitleCompareTo(title) && isXCompareTo(mapx) && isYCompareTo(mapy);
    }

    private boolean isYCompareTo(String mapy) {
        return this.getPlace().getMapy().equals(mapy);
    }

    private boolean isXCompareTo(String mapx) {
        return this.getPlace().getMapx().equals(mapx);
    }

    private boolean isTitleCompareTo(String title) {
        return this.getPlace().getTitle().equals(title);
    }
    public boolean isConfirm(){
        return this.confirm.equals(TRUE);
    }
}
