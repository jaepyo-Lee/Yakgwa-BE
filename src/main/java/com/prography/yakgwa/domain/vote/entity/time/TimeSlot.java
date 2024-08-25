package com.prography.yakgwa.domain.vote.entity.time;

import com.prography.yakgwa.domain.meet.entity.Meet;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.lang.Boolean.TRUE;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity(name = "TIMESLOT_TABLE")
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime time;
    private Boolean confirm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meet_id")
    private Meet meet;

    public void confirm() {
        confirm = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return Objects.equals(id, timeSlot.id);
    }

    public static TimeSlot ofConfirm(Meet meet,LocalDateTime time){
        return TimeSlot.builder()
                .meet(meet)
                .time(time)
                .confirm(true)
                .build();
    }

    public boolean isConfirm(){
        return this.confirm.equals(TRUE);
    }
}
