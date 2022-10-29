package com.hanghae.instakilogram.controller;

import com.hanghae.instakilogram.dto.TokenDto;
import com.hanghae.instakilogram.dto.request.*;
import com.hanghae.instakilogram.dto.response.ResponseDto;
import com.hanghae.instakilogram.security.UserDetailsImpl;
import com.hanghae.instakilogram.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseDto<?> signUp(@RequestBody SignupRequestDto signupRequestDto) {
        return memberService.signUp(signupRequestDto);
    }

    @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
        return memberService.login(loginRequestDto, httpServletResponse);
    }

    @GetMapping("/signup/checkid")
    public ResponseDto<?> checkId(@RequestBody CheckIdRequestDto checkIdRequestDto) {
        return memberService.checkId(checkIdRequestDto);
    }

    @GetMapping("/signup/checknickname")
    public ResponseDto<?> checkNickname(@RequestBody CheckNicknameRequestDto checkNicknameRequestDto) {
        return memberService.checkNickname(checkNicknameRequestDto);
    }
}
