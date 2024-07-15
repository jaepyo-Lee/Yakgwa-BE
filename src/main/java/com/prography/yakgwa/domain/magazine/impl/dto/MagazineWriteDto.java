package com.prography.yakgwa.domain.magazine.impl.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class MagazineWriteDto {
    private String title;
    private MultipartFile thumbnail;
    private MultipartFile content;

    public static MagazineWriteDto of(String title, MultipartFile thumbnail, MultipartFile content) {
        return MagazineWriteDto.builder()
                .content(content)
                .title(title)
                .thumbnail(thumbnail)
                .build();
    }
}
