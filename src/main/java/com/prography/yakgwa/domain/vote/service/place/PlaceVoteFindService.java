package com.prography.yakgwa.domain.vote.service.place;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.impl.MeetStatusJudger;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.domain.vote.service.VoteFinder;
import com.prography.yakgwa.domain.vote.service.impl.VoteCounter;
import com.prography.yakgwa.domain.vote.service.req.PlaceInfosByMeetStatus;
import com.prography.yakgwa.global.format.exception.meet.NotFoundMeetException;
import com.prography.yakgwa.global.format.exception.param.DataIntegrateException;
import com.prography.yakgwa.global.format.exception.participant.NotFoundParticipantException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus.BEFORE_CONFIRM;
import static com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus.VOTE;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceVoteFindService implements VoteFinder<PlaceInfosByMeetStatus> {

    private final MeetJpaRepository meetJpaRepository;
    private final PlaceVoteJpaRepository placeVoteJpaRepository;
    private final VoteCounter voteCounter;
    private final PlaceSlotJpaRepository placeSlotJpaRepository;
    private final MeetStatusJudger meetStatusJudger;
    private final ParticipantJpaRepository participantJpaRepository;

    /**
     * Work) 테스트코드
     * Write-Date) 2024-07-13
     * Finish-Date)
     * 원래 BEFORE_VOTE이면 PLACE가 NULL인데 빈리스트 반환으로 변경
     */
    @Override
    public PlaceInfosByMeetStatus findVoteInfoWithStatusOf(Long userId, Long meetId) {
        Meet meet = findMeetById(meetId);
        Participant participant = participantJpaRepository.findByUserIdAndMeetId(userId, meetId)
                .orElseThrow(NotFoundParticipantException::new);

        boolean isConfirm = meetStatusJudger.verifyConfirmAndConfirmPlacePossible(meet);

        if (isConfirm) { //장소확정되었을때
            List<PlaceSlot> placeSlots = placeSlotJpaRepository.findConfirmByMeetId(meetId);
            if (isCorrectConfirmPlaceSlotSize(placeSlots)) {
                log.info("{}번 모임의 장소투표 데이터확인", meetId);
                throw new DataIntegrateException();
            }
            return PlaceInfosByMeetStatus.of(VoteStatus.CONFIRM, placeSlots);
        } else {
            if (isOverVotePeriodFrom(meet)) { //시간은 지났지만 확정은 안됌 BEFROE_CONFIRM
                List<PlaceVote> allInMeet = placeVoteJpaRepository.findAllInMeet(meet.getId());
                List<PlaceSlot> placeSlotList = voteCounter.findMaxVotePlaceSlotFrom(meet);
                return PlaceInfosByMeetStatus.of(BEFORE_CONFIRM, placeSlotList);
            }
            List<PlaceVote> placeVoteOfUserInMeet = placeVoteJpaRepository.findAllByUserIdAndMeetId(userId, meetId);
            if (isUserVotePlace(placeVoteOfUserInMeet)) { //사용자가 투표했을때
                return PlaceInfosByMeetStatus.of(VOTE, placeVoteOfUserInMeet.stream()
                        .map(PlaceVote::getPlaceSlot)
                        .toList());
            } else { //사용자가 투표 안했을때
                return PlaceInfosByMeetStatus.of(VoteStatus.BEFORE_VOTE, List.of());
            }
        }
    }


    private Meet findMeetById(Long meetId) {
        return meetJpaRepository.findById(meetId)
                .orElseThrow(NotFoundMeetException::new);
    }


    private static boolean isUserVotePlace(List<PlaceVote> placeVoteOfUserInMeet) {
        return !placeVoteOfUserInMeet.isEmpty();
    }

    private static boolean isOverVotePeriodFrom(Meet meet) {
        return meet.getCreatedDate().plusHours(meet.getValidInviteHour()).isBefore(LocalDateTime.now());
    }

    private static boolean isCorrectConfirmPlaceSlotSize(List<PlaceSlot> placeSlots) {
        return placeSlots.size() > 1;
    }
}
