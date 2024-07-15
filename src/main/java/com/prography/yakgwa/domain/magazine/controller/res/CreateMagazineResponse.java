package com.prography.yakgwa.domain.magazine.controller.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "매거진 생성 응답값")
public class CreateMagazineResponse {
    @Schema(description = "저장된 매거진 ID")
    private Long magazineId;
}
