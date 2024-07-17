package com.prography.yakgwa.domain.magazine.impl;

import com.prography.yakgwa.domain.magazine.entity.Magazine;
import com.prography.yakgwa.domain.magazine.impl.dto.MagazineWriteDto;
import com.prography.yakgwa.domain.magazine.repository.MagazineJpaRepository;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.user.entity.Role;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.global.format.exception.user.NotMatchAdminRoleException;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@ImplService
@RequiredArgsConstructor
public class MagazineWriter {
    private final MagazineJpaRepository magazineJpaRepository;
    private final ImageWriter imageWriter;

    public Magazine write(Place place, User user, MagazineWriteDto magazineWriteDto) throws IOException {
        if (user.getRole() != Role.ROLE_ADMIN) {
            throw new NotMatchAdminRoleException();
        }
        Magazine magazine = Magazine.builder()
                .place(place).user(user).title(magazineWriteDto.getTitle())
                .build();
        Magazine saveMagazine = magazineJpaRepository.save(magazine);
        imageWriter.write(saveMagazine, magazineWriteDto.getThumbnail(), magazineWriteDto.getContent());
        return magazine;
    }
}
