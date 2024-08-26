package com.prography.yakgwa.domain.vote.service.place;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.impl.ConfirmChecker;
import com.prography.yakgwa.domain.meet.impl.MeetConfirmChecker;
import com.prography.yakgwa.domain.meet.impl.PlaceConfirmChecker;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.domain.vote.service.place.res.PlaceSlotWithUserResponse;
import com.prography.yakgwa.global.format.exception.meet.NotFoundMeetException;
import com.prography.yakgwa.global.format.exception.slot.AlreadyAppendPlaceException;
import com.prography.yakgwa.global.format.exception.vote.AlreadyPlaceConfirmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class PlaceSlotService {
    private final MeetJpaRepository meetJpaRepository;
    private final PlaceJpaRepository placeJpaRepository;
    private final PlaceSlotJpaRepository placeSlotJpaRepository;
    private final PlaceVoteJpaRepository placeVoteJpaRepository;
    private final MeetConfirmChecker meetConfirmChecker;
    private final ConfirmChecker confirmChecker;

    @Autowired
    public PlaceSlotService(MeetJpaRepository meetJpaRepository,
                            PlaceJpaRepository placeJpaRepository,
                            PlaceSlotJpaRepository placeSlotJpaRepository,
                            PlaceVoteJpaRepository placeVoteJpaRepository,
                            MeetConfirmChecker meetConfirmChecker,
                            PlaceConfirmChecker confirmChecker) {
        this.meetJpaRepository = meetJpaRepository;
        this.placeJpaRepository = placeJpaRepository;
        this.placeSlotJpaRepository = placeSlotJpaRepository;
        this.placeVoteJpaRepository = placeVoteJpaRepository;
        this.meetConfirmChecker = meetConfirmChecker;
        this.confirmChecker = confirmChecker;
    }

    /**
     * Work) Test Code
     * Write-Date) 2024-07-29, 월, 14:20
     * Finish-Date) 2024-08-02
     */
    public Place appendPlaceSlotFrom(Long meetId, PlaceInfoDto placeInfo) {

        if (isExistSamePlaceSlotFrom(meetId, placeInfo)) {
            throw new AlreadyAppendPlaceException();
        }
        Meet meet = meetJpaRepository.findById(meetId)
                .orElseThrow(NotFoundMeetException::new);
        validateMeetingStatus(meet);
        Place place = placeJpaRepository.findByMapxAndMapy(placeInfo.getMapx(), placeInfo.getMapy())
                .orElseGet(() -> placeJpaRepository.save(placeInfo.toEntity()));

        placeSlotJpaRepository.save(PlaceSlot.of(meet, Boolean.FALSE, place));
        return place;
    }

    private boolean isExistSamePlaceSlotFrom(Long meetId, PlaceInfoDto placeInfo) {
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findAllByMeetId(meetId);
        return placeSlots.stream().anyMatch(placeSlot ->
                placeSlot.isSamePlaceSlot(placeInfo.getTitle(), placeInfo.getMapx(), placeInfo.getMapy()));
    }

    /**
     * Work) Test Code
     * Write-Date) 2024-07-29, 월, 14:20
     * Finish-Date) 2024-08-02
     */
    public List<PlaceSlotWithUserResponse> findPlaceSlotFrom(Long meetId) {
        Meet meet = meetJpaRepository.findById(meetId)
                .orElseThrow(NotFoundMeetException::new);
        validateMeetingStatus(meet);
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findAllByMeetId(meetId);
        return createPlaceSlotWithUserResponses(placeSlots);
    }

    private void validateMeetingStatus(Meet meet) {
        if (confirmChecker.isConfirm(meet)) {
            throw new AlreadyPlaceConfirmException();
        }
    }

    private List<PlaceSlotWithUserResponse> createPlaceSlotWithUserResponses(List<PlaceSlot> placeSlots) {
        List<PlaceSlotWithUserResponse> placeSlotWithUserResponses = new ArrayList<>();
        for (PlaceSlot placeSlot : placeSlots) {
            List<User> users = getUsersByPlaceSlot(placeSlot);
            placeSlotWithUserResponses.add(PlaceSlotWithUserResponse.of(placeSlot, users));
        }
        return placeSlotWithUserResponses;
    }

    private List<User> getUsersByPlaceSlot(PlaceSlot placeSlot) {
        List<PlaceVote> placeVotes = placeVoteJpaRepository.findAllByPlaceSlotId(placeSlot.getId());
        return placeVotes.stream()
                .map(PlaceVote::getUser)
                .toList();
    }
}
