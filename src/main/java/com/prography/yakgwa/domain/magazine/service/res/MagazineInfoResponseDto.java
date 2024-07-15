package com.prography.yakgwa.domain.magazine.service.res;

import com.prography.yakgwa.domain.magazine.entity.Magazine;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class MagazineInfoResponseDto {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private List<MagazineInfoEntity> magazineInfo;
}
