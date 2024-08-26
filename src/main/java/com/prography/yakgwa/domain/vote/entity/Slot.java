package com.prography.yakgwa.domain.vote.entity;

import com.prography.yakgwa.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static java.lang.Boolean.TRUE;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@NoArgsConstructor
public class Slot extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean confirm;

    public void confirm() {
        confirm = true;
    }

    public boolean isConfirm() {
        return this.confirm.equals(TRUE);
    }

    public Slot(Boolean confirm) {
        this.confirm = confirm;
    }
}
