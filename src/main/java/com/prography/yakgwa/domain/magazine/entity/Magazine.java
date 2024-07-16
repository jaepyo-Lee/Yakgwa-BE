package com.prography.yakgwa.domain.magazine.entity;

import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "MAGAZINE_TABLE")
public class Magazine extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    @Builder.Default
    private boolean open=true;

    public boolean changeOpenState(){
        open = !open;
        return open;
    }
}
