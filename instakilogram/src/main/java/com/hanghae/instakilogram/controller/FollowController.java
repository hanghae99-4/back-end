package com.hanghae.instakilogram.controller;

import com.hanghae.instakilogram.dto.response.ResponseDto;
import com.hanghae.instakilogram.security.UserDetailsImpl;
import com.hanghae.instakilogram.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @GetMapping("/follow/{toMemberId}")
    public ResponseDto<?> follow(@PathVariable String toMemberId, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return followService.updateFollow(toMemberId, userDetails.getMember());
    }

}
