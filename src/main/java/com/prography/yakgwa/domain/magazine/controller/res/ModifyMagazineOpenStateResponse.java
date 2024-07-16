package com.prography.yakgwa.domain.magazine.controller.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ModifyMagazineOpenStateResponse {
    private Long magazineId;
    private boolean open;
}
