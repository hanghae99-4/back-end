package com.hanghae.instakilogram.controller;

import com.hanghae.instakilogram.dto.request.FeedsRequestDto;
import com.hanghae.instakilogram.dto.request.FeedsUpdateDto;
import com.hanghae.instakilogram.dto.response.ResponseDto;
import com.hanghae.instakilogram.security.UserDetailsImpl;
import com.hanghae.instakilogram.service.FeedsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
public class FeedsController {

    private final FeedsService feedsService;

    // 게시글 생성
    @PostMapping("/feeds")
    public ResponseDto<?> createFeeds(@ModelAttribute FeedsRequestDto feedsRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        return feedsService.createFeeds(feedsRequestDto, userDetails.getMember());
    }
    // 게시글 전체 조회
    @GetMapping("/feeds")
    public ResponseDto<?> getAllFeeds(@AuthenticationPrincipal UserDetailsImpl userDetails){

        return feedsService.getAllFeeds(userDetails.getMember());
    }

    // 게시글 상세 조회
    @GetMapping("/feeds/{feedsId}")
    public ResponseDto<?> getDetailFeeds(@PathVariable("feedsId") Long feedsId, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return feedsService.getDetailFeeds(feedsId, userDetails.getMember());
    }

    // 게시글 수정
    @PutMapping("/feeds/{feedsId}")
    public ResponseDto<?> updateFeeds(@PathVariable("feedsId") Long feedsId, @ModelAttribute FeedsUpdateDto feedsUpdateDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return feedsService.updateFeeds(feedsId, feedsUpdateDto, userDetails.getMember());
    }

    // 게시글 삭제
    @DeleteMapping("/feeds/{feedsId}")
    public ResponseDto<?> deleteFeeds(@PathVariable Long feedsId, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return feedsService.deleteFeeds(feedsId, userDetails.getMember());
    }


}
