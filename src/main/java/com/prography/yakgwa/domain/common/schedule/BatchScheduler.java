package com.prography.yakgwa.domain.common.schedule;

import com.prography.yakgwa.domain.common.redis.PlaceRedisRepository;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.PlaceLike;
import com.prography.yakgwa.domain.place.repository.PlaceLikeJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.global.format.exception.user.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchScheduler {
    private final PlaceRedisRepository placeRedisRepository;
    private final PlaceLikeJpaRepository placeLikeJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Transactional
    @Scheduled(cron = "${schedule.cron.cacheToDB}", zone = "Asia/Seoul")
    public void saveCacheToDB() {
        log.info("캐시-DB 장소 좋아요 동기화 스케줄링 실행");
        // 캐싱된것을 DB로 저장, 캐시 만료보다 이른시간에 저장시켜줘야함
        List<Integer> goodPlaceUserId = placeRedisRepository.getGoodPlaceUserId();
        for (Integer userId : goodPlaceUserId) {
            Long userIdLong = Long.valueOf(userId);

            // 캐시에서 유저가 좋아요한 장소 목록을 가져옴
            List<Place> likePlaceInfos = placeRedisRepository.findLikePlaceInfos(userIdLong);

            // 유저 정보를 DB에서 조회 (유저가 없으면 예외 발생)
            User user = userJpaRepository.findById(userIdLong).orElseThrow(NotFoundUserException::new);

            // 해당 유저의 기존 좋아요 정보 삭제
            placeLikeJpaRepository.deleteAllByUserId(userIdLong);

            // 새로 좋아요한 장소 정보를 PlaceLike 엔티티로 변환
            List<PlaceLike> placeLikes = likePlaceInfos.stream()
                    .map(place -> PlaceLike.builder()
                            .user(user)
                            .place(place)
                            .build())
                    .toList();

            // 새로운 좋아요 정보 DB에 저장 (배치 처리)
            placeLikeJpaRepository.saveAll(placeLikes);
            placeRedisRepository.deletePlaceLikeByUserId(userIdLong);
        }
    }
}
