package com.prography.yakgwa.domain.vote.entity.time;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.vote.entity.Slot;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.lang.Boolean.TRUE;
import static java.time.LocalDateTime.now;

@Getter
@Entity(name = "TIMESLOT_TABLE")
@DiscriminatorValue("time")
@NoArgsConstructor
public class TimeSlot extends Slot {

    private LocalDateTime time;

    public TimeSlot(Long id) {
        super(id);
    }

    @Builder
    public TimeSlot(Meet meet, LocalDateTime time, boolean isConfirm) {
        super(isConfirm, meet); // Slot의 생성자를 호출하여 'meet' 설정
        this.time = time;
    }

    public boolean isMeetFinish() {
        return time.plusHours(3L).isBefore(LocalDateTime.now());
    }

    public boolean isTimeEquals(LocalDateTime time) {
        return this.time.isEqual(time);
    }

    @Override
    public boolean equals(Object obj) {
        Slot slot = (Slot) obj;
        return super.getId().equals(slot.getId());
    }
}
/*

//@AllArgsConstructor
@Entity(name = "TIMESLOT_TABLE")
@DiscriminatorValue("time-slot")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TimeSlot extends Slot {
    */
/*@Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;*//*

    @Getter
    private LocalDateTime time;
//    private Boolean confirm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meet_id")
    private Meet meet;

    */
/*public void confirm() {
        confirm = true;
    }*//*


    @Builder
    public TimeSlot(Meet meet, LocalDateTime time,boolean isConfirm) {
        super(isConfirm);
        this.time = time;
        this.meet = meet;
    }

    */
/*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return Objects.equals(id, timeSlot.id);
    }*//*


    public static TimeSlot ofConfirm(Meet meet, LocalDateTime time) {
        return TimeSlot.builder()
                .meet(meet)
                .time(time)
                .isConfirm(true)
//                .confirm(true)
                .build();
    }

    public boolean isMeetFinish() {
        return time.plusHours(3L).isBefore(now());
    }
//    public boolean isConfirm(){
//        return this.confirm.equals(TRUE);
//    }

    public boolean isTimeEquals(LocalDateTime time) {
        return this.time.isEqual(time);
    }
}
*/
