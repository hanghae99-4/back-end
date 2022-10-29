package com.hanghae.instakilogram.controller;

import com.hanghae.instakilogram.dto.TokenDto;
import com.hanghae.instakilogram.dto.request.*;
import com.hanghae.instakilogram.dto.response.ResponseDto;
import com.hanghae.instakilogram.security.UserDetailsImpl;
import com.hanghae.instakilogram.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Tag(name = "Member", description = "회원관리 API")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    @Operation(summary = "Sign Up", description = "회원가입")
    public ResponseDto<?> signUp(@RequestBody SignupRequestDto signupRequestDto) {
        return memberService.signUp(signupRequestDto);
    }

    @PostMapping("/login")
    @Operation(summary = "Log In", description = "로그인")
    public ResponseDto<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
        return memberService.login(loginRequestDto, httpServletResponse);
    }

    @GetMapping("/signup/checkid")
    @Operation(summary = "ID Check", description = "멤버 ID 확인")
    public ResponseDto<?> checkId(@RequestBody CheckIdRequestDto checkIdRequestDto) {
        return memberService.checkId(checkIdRequestDto);
    }

    @GetMapping("/signup/checknickname")
    @Operation(summary = "Nickname Check", description = "멤버 Nickname 확인")
    public ResponseDto<?> checkNickname(@RequestBody CheckNicknameRequestDto checkNicknameRequestDto) {
        return memberService.checkNickname(checkNicknameRequestDto);
    }
}
