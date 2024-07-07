package com.prography.yakgwa.global.controller;

import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/health-check")
    public String healthCheck(@AuthenticationPrincipal CustomUserDetail user){
        System.out.println(user.getUsername());
        return "health-check";
    }
}
