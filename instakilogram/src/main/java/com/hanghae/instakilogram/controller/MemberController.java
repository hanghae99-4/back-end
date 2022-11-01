package com.hanghae.instakilogram.controller;

import com.hanghae.instakilogram.dto.request.*;
import com.hanghae.instakilogram.dto.response.ResponseDto;
import com.hanghae.instakilogram.security.UserDetailsImpl;
import com.hanghae.instakilogram.service.FeedsService;
import com.hanghae.instakilogram.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final FeedsService feedsService;

    @PostMapping("/signup")
    @Operation(summary = "Sign Up", description = "회원가입")
    public ResponseDto<?> signUp(@RequestBody SignupRequestDto signupRequestDto) {
        return memberService.signUp(signupRequestDto);
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "FeedList by Member", description = "내가 쓴 게시글")
    public ResponseDto<?> getFeeds(@PathVariable String memberId) {
        return feedsService.getFeedsByMember(memberId);
    }


    @GetMapping("/signup/checkid/{id}")
    @Operation(summary = "ID Check", description = "멤버 ID 확인")
    public ResponseDto<?> checkId(@PathVariable String id) {
        return memberService.checkId(id);
    }

    @GetMapping("/signup/checknickname/{id}")
    @Operation(summary = "Nickname Check", description = "멤버 Nickname 확인")
    public ResponseDto<?> checkNickname(@PathVariable String id) {
        return memberService.checkNickname(id);
    }

    @PutMapping("/{memberId}")
    @Operation(summary = "Member Info Update", description = "멤버 정보 업데이트")
    public ResponseDto<?> memberUpdate(@ModelAttribute MemberUpdateRequestDto memberUpdateRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable String memberId) throws IOException {
        return memberService.memberUpdate(memberUpdateRequestDto, userDetails.getMember(), memberId);
    }

    @PostMapping("/login")
    @Operation(summary = "Log In", description = "로그인")
    public ResponseDto<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
        return memberService.login(loginRequestDto, httpServletResponse);
    }


}
