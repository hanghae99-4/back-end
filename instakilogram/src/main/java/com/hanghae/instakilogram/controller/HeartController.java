package com.hanghae.instakilogram.controller;

import com.hanghae.instakilogram.dto.response.ResponseDto;
import com.hanghae.instakilogram.security.UserDetailsImpl;
import com.hanghae.instakilogram.service.HeartService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Getter
@RestController
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    @GetMapping("/feeds/{feedId}/heart")
    public ResponseDto<?> updateHeart(@PathVariable Long feedId, @AuthenticationPrincipal UserDetailsImpl userDetails){


        return heartService.updateHeart(feedId, userDetails.getMember());
    }

}
