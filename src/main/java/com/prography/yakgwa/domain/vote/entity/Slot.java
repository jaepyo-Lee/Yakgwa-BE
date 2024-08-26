package com.prography.yakgwa.domain.vote.entity;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "SLOT_TABLE")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "slot_type", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor
public class Slot extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean confirm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meet_id")
    private Meet meet;

    public Slot(Long id) {
        this.id = id;
    }

    public Slot(Boolean confirm, Meet meet) {
        this.confirm = confirm;
        this.meet = meet;
    }

    public void confirm() {
        confirm = true;
    }

    public boolean isConfirm() {
        return Boolean.TRUE.equals(this.confirm);
    }
}

