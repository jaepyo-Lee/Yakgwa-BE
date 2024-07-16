package com.prography.yakgwa.domain.magazine.service;

import com.prography.yakgwa.domain.magazine.entity.Image;
import com.prography.yakgwa.domain.magazine.entity.Magazine;
import com.prography.yakgwa.domain.magazine.impl.MagazineWriter;
import com.prography.yakgwa.domain.magazine.impl.dto.MagazineWriteDto;
import com.prography.yakgwa.domain.magazine.repository.ImageJpaRepository;
import com.prography.yakgwa.domain.magazine.repository.MagazineJpaRepository;
import com.prography.yakgwa.domain.magazine.repository.MagazineRepositoryImpl;
import com.prography.yakgwa.domain.magazine.service.req.CreateMagazineRequestDto;
import com.prography.yakgwa.domain.magazine.service.res.MagazineInfoEntity;
import com.prography.yakgwa.domain.magazine.service.res.MagazineInfoResponseDto;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.impl.PlaceReader;
import com.prography.yakgwa.domain.user.entity.Role;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.impl.UserReader;
import com.prography.yakgwa.global.format.exception.magazine.NotFoundMagazineException;
import com.prography.yakgwa.global.format.exception.user.NotMatchAdminRoleException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class MagazineService {
    private final MagazineJpaRepository magazineJpaRepository;
    private final PlaceReader placeReader;
    private final UserReader userReader;
    private final MagazineWriter magazineWriter;
    private final ImageJpaRepository imageJpaRepository;
    private final MagazineRepositoryImpl magazineRepositoryImpl;
    private static final int PAGE_SIZE = 3;

    /**
     * Work) 테스트코드 및 이미지업로드 로직
     * Finish-Date) 2024-07-15
     */
    /**
     * Todo
     * Work) 테스트코드 전체돌릴때 에러나는것 해결
     * Write-Date) 2024-07-16
     * Finish-Date)
     */
    public Magazine create(CreateMagazineRequestDto requestDto, MultipartFile thumbnail, MultipartFile content) {
        User user = userReader.read(requestDto.getUserId());
        Place place = placeReader.read(requestDto.getPlaceId());
        return magazineWriter.write(place, user, MagazineWriteDto.of(requestDto.getTitle(), thumbnail, content));
    }

    /**
     * Todo
     * Work) 테스트코드 및 페이징
     * Write-Date) 2024-07-15
     * Finish-Date)
     */
    public MagazineInfoResponseDto findPaging(int page) {
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<Magazine> magazinePage = magazineRepositoryImpl.searchPage(pageRequest);
        List<Magazine> magazines = magazinePage.getContent();
        List<MagazineInfoEntity> magazineInfoEntities = new ArrayList<>();
        for (Magazine magazine : magazines) {
            List<Image> image = imageJpaRepository.findAllByMagazineId(magazine.getId());
            magazineInfoEntities.add(MagazineInfoEntity.builder().magazine(magazine).images(image).build());
        }
        return MagazineInfoResponseDto.builder()
                .page(magazinePage.getNumber())
                .size(magazinePage.getContent().size())
                .totalPages(magazinePage.getTotalPages())
                .totalElements(magazinePage.getTotalElements())
                .magazineInfo(magazineInfoEntities)
                .build();
    }

    public Magazine modifyOpenState(Long userId, Long magazineId) {
        User user = userReader.read(userId);
        if (!user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new NotMatchAdminRoleException();
        }
        Magazine magazine = magazineJpaRepository.findById(magazineId)
                .orElseThrow(NotFoundMagazineException::new);
        magazine.changeOpenState();
        return magazine;
    }
}
