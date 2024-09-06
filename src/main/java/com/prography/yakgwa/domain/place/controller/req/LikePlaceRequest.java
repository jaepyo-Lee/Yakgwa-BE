package com.prography.yakgwa.domain.place.controller.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class LikePlaceRequest {
    @Schema(description = "장소명")
    private String title;
    @Schema(description = "장소X")
    private String mapx;
    @Schema(description = "장소y")
    private String mapy;
}
