package com.prography.yakgwa.domain.common.redis;

import com.prography.yakgwa.global.format.enumerate.AlarmType;
import lombok.*;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MessageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long meetId ;
    private AlarmType alarmType;
}
