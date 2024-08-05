package com.prography.yakgwa.domain.meet.controller.res;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Schema(description = "탈퇴한 사용자도 함께 조회될수 있습니다. <br>" +
        "탈퇴한 사용자의 경우 이름이 null로 반환되어 null인경우 (알수없는사용자) 이런식으로 탈퇴된 사용자임을 명시해주면 될거같습니다. 이름과 함께 이미지 url도 나가는데, url의 경우 기본이미지를 반환하도록 하였습니다!")
public class MeetInfoWithParticipantResponse {
    private MeetInfo meetInfo;
    private List<ParticipantInfo> participantInfo;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Schema(name = "MeetInfoWithParticipantResponse-meetInfo")
    private static class MeetInfo {
        @Schema(description = "모임 테마명", example = "데이트")
        private String ThemeName;
        @Schema(description = "모임명", example = "다음 세션 모임")
        private String meetTitle;
        @Schema(description = "모임설명", example = "설명")
        private String description;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Schema(name = "MeetInfoWithParticipantResponse-participantInfo")
    private static class ParticipantInfo {
        @Schema(description = "모임에서의 역할", example = "LEADER(약과장) 또는 PARTICIPANT(약과원)")
        private MeetRole meetRole;
        @Schema(description = "이미지 url")
        private String imageUrl;
        @Schema(description = "참여자의 이름")
        private String name;
    }

    public static MeetInfoWithParticipantResponse of(Meet meet, List<Participant> participants) {
        return MeetInfoWithParticipantResponse.builder()
                .participantInfo(participants.stream()
                        .map(participant -> ParticipantInfo.builder()
                                .name(participant.getUser().getName())
                                .meetRole(participant.getMeetRole())
                                .imageUrl(participant.getUser().getImageUrl())
                                .build())
                        .toList())
                .meetInfo(MeetInfo.builder()
                        .ThemeName(meet.getMeetTheme().getName())
                        .meetTitle(meet.getTitle())
                        .description(meet.getDescription())
                        .build())
                .build();
    }
}
