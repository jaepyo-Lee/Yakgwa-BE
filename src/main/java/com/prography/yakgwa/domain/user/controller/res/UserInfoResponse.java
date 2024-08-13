package com.prography.yakgwa.domain.user.controller.res;

import com.prography.yakgwa.domain.user.entity.User;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    @Schema(description = "사용자 이름")
    private String name;
    @Schema(description = "사용자 이미지 url")
    private String imageUrl;

    public static UserInfoResponse of(User user) {
        return UserInfoResponse.builder().name(user.getName()).imageUrl(user.getImageUrl()).build();
    }
}
