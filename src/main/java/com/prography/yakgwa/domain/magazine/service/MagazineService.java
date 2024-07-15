package com.prography.yakgwa.domain.magazine.service;

import com.prography.yakgwa.domain.magazine.entity.Magazine;
import com.prography.yakgwa.domain.magazine.impl.MagazineWriter;
import com.prography.yakgwa.domain.magazine.impl.dto.MagazineWriteDto;
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
    private final MagazineWriter magazineWriter;

    /**
     * Work) 테스트코드 및 이미지업로드 로직
     * Write-Date) 2024-07-15
     * Finish-Date) 2024-07-15
     */
    public Magazine create(CreateMagazineRequestDto requestDto, MultipartFile thumbnail, MultipartFile content) {
        User user = userReader.read(requestDto.getUserId());
        Place place = placeReader.read(requestDto.getPlaceId());
        return magazineWriter.write(place, user, MagazineWriteDto.of(requestDto.getTitle(), thumbnail, content));
    }

}
