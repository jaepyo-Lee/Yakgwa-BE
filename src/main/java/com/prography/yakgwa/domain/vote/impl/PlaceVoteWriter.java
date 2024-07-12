package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.impl.PlaceReader;
import com.prography.yakgwa.domain.place.impl.PlaceWriter;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ImplService
@RequiredArgsConstructor
public class PlaceVoteWriter {
    private final PlaceVoteJpaRepository placeVoteJpaRepository;
    private final PlaceWriter placeWriter;
    private final PlaceReader placeReader;
    private final PlaceSlotWriter placeSlotWriter;

    /**
     * Work) 테스트코드
     * Write-Date) 2024-07-12
     * Finish-Date) 2024-07-12
     */
    public List<PlaceVote> writeAll(User user, List<PlaceSlot> placeSlots) {
        List<PlaceVote> placeVotes = placeSlots.stream().map(placeSlot -> PlaceVote.builder()
                        .placeSlot(placeSlot)
                        .user(user)
                        .build())
                .toList();
        return placeVoteJpaRepository.saveAll(placeVotes);
    }

    public void confirmAndWrite(Meet meet, boolean isConfirmPlace, List<PlaceInfoDto> placeInfo) {
        if (isConfirmPlace && placeInfo.size() != 1) {
            throw new RuntimeException("확정된 장소는 1개이어야 합니다");
        }
        List<Place> placeList = placeInfo.stream()
                .map(placeInfoDto -> placeReader.readByMapxAndMapy(placeInfoDto.getMapx(), placeInfoDto.getMapy())
                        .orElseGet(() -> placeWriter.write(placeInfoDto.toEntity())))
                .toList();

        placeList.forEach(place ->
                placeSlotWriter.write(PlaceSlot.builder().meet(meet).confirm(isConfirmPlace).place(place).build()));
    }

    /**
     * Work) 테스트코드
     * Write-Date) 2024-07-12
     * Finish-Date) 2024-07-12
     * Memo) 다른로직 없어 레포지토리 테스트로 대체
     */
    public void deleteAllVoteOfUser(User user, Long meetId) {
        placeVoteJpaRepository.deleteAllByUserIdAndMeetId(user, meetId);
    }
}
