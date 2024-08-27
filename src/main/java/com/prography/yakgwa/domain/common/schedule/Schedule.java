package com.prography.yakgwa.domain.common.schedule;

import com.prography.yakgwa.global.format.enumerate.AlarmType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "SCHEDULE_TABLE")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long meetId;
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;
    private boolean send;

    @Builder
    public Schedule(Long meetId, AlarmType alarmType) {
        this.meetId = meetId;
        this.alarmType = alarmType;
        this.send = false;
    }
    public void send(){
        this.send = true;
    }
}
