package com.prography.yakgwa.domain.common.alarm.entity;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "ALARM_TABLE")
public class Alarm extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String taskID;
    private String title;
    private String body;

    @Builder.Default
    private boolean send=false;

    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "meet_id")
    private Meet meet;

    public void changeTime(LocalDateTime time){
        this.time = time;
    }
}
