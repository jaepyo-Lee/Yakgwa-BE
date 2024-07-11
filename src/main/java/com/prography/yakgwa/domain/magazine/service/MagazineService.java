package com.prography.yakgwa.domain.magazine.service;

import com.prography.yakgwa.domain.magazine.entity.Magazine;
import com.prography.yakgwa.domain.magazine.repository.MagazineJpaRepository;
import com.prography.yakgwa.domain.magazine.service.req.CreateMagazineRequestDto;
import com.prography.yakgwa.domain.magazine.service.res.MagazineInfoResponseDto;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.impl.PlaceReader;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.impl.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MagazineService {
    private final MagazineJpaRepository magazineJpaRepository;
    private final PlaceReader placeReader;
    private final UserReader userReader;

    /**
     * Todo
     * 이미지 업로드 로직 추가해야함
     */
    public Magazine create(CreateMagazineRequestDto requestDto, List<MultipartFile> multipartFiles) {
        User user = userReader.read(requestDto.getUserId());
        Place place = placeReader.read(requestDto.getPlaceId());
        Magazine magazine = Magazine.builder()
                .place(place).user(user).contents(requestDto.getContents())
                .build();
        //이미지 추가 로직 넣어야함
        return magazineJpaRepository.save(magazine);
    }

    /**
     * Todo
     * 이미지 조회추가
     */
    public MagazineInfoResponseDto find(Long meetId) {
        Magazine magazine = magazineJpaRepository.findById(meetId).orElseThrow(() -> new RuntimeException("해당 매거진을 찾을수 없습니다."));
        return MagazineInfoResponseDto.builder()
                .magazine(magazine).imageUrl(null)
                .build();
    }
}
