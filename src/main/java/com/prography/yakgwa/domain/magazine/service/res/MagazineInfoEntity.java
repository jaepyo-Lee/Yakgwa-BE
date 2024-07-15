package com.prography.yakgwa.domain.magazine.service.res;

import com.prography.yakgwa.domain.magazine.entity.Image;
import com.prography.yakgwa.domain.magazine.entity.Magazine;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MagazineInfoEntity {
    private Magazine magazine;
    private List<Image> images;
}
