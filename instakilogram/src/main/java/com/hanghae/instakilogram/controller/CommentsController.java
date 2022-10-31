package com.hanghae.instakilogram.controller;

import com.hanghae.instakilogram.dto.request.CommentsRequestDto;
import com.hanghae.instakilogram.dto.response.ResponseDto;
import com.hanghae.instakilogram.security.UserDetailsImpl;
import com.hanghae.instakilogram.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class CommentsController {

    private final CommentsService commentsService;

    @PostMapping("/{feedsId}/comments")
    public ResponseDto<?> createComment(@RequestBody CommentsRequestDto commentRequestDto, @PathVariable("feedsId") Long feedsId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentsService.createComment(commentRequestDto, feedsId, userDetails);
    }

    @GetMapping("/{feedsId}/comments")
    public ResponseDto<?> getComments(@PathVariable("feedsId") Long feedsId) {
        return commentsService.getComments(feedsId);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseDto<?> deleteComment (@PathVariable(name = "commentId") Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentsService.deleteComment(commentId, userDetails);
    }
}
