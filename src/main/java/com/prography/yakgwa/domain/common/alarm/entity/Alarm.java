package com.prography.yakgwa.domain.common.alarm.entity;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.global.entity.BaseTimeEntity;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
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
@Entity(name = "ALARM_TABLE")
public class Alarm extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(STRING)
    private AlarmType alarmType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
