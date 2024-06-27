package com.prography.yakgwa.domain.meet.controller.res;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MeetInfoWithParticipantResponse {
    private MeetInfo meetInfo;
    private List<ParticipantInfo> participantInfo;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    private static class MeetInfo {
        private String ThemeName;
        private String meetTitle;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    private static class ParticipantInfo {
        private MeetRole meetRole;
        private String imageUrl;
        private Long participantId;
    }

    public static MeetInfoWithParticipantResponse of(Meet meet, List<Participant> participants) {
        return MeetInfoWithParticipantResponse.builder()
                .participantInfo(participants.stream()
                        .map(participant -> ParticipantInfo.builder()
                                .participantId(participant.getId())
                                .meetRole(participant.getMeetRole())
                                .imageUrl(participant.getUser().getImageUrl())
                                .build())
                        .toList())
                .meetInfo(MeetInfo.builder()
                        .ThemeName(meet.getMeetTheme().getName())
                        .meetTitle(meet.getTitle())
                        .build())
                .build();
    }
}
