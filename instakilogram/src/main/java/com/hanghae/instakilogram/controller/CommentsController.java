package com.hanghae.instakilogram.controller;

import com.hanghae.instakilogram.dto.request.CommentsRequestDto;
import com.hanghae.instakilogram.dto.response.ResponseDto;
import com.hanghae.instakilogram.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/feeds")
public class CommentsController {

    private final CommentsService commentsService;

    @PostMapping("/{feeds_id}/Comments")
    public ResponseDto<?> createComment(@RequestBody CommentsRequestDto commentRequestDto, @PathVariable Long feedId, HttpServletRequest request) {
        return commentsService.createComment(commentRequestDto, feedId, request);
    }

    @GetMapping("/{feeds_id}/comments")
    public ResponseDto<?> getComments(@PathVariable Long feedId) {
        return commentsService.getComments(feedId);
    }

    @PutMapping("/{feeds_id}/comments/{comments_id}")
    public ResponseDto<?> updateComment(@RequestBody CommentsRequestDto commentRequestDto, @PathVariable(name = "feeds_id") Long feedId, @PathVariable(name = "comments_id") Long commentId, HttpServletRequest request){
        return commentsService.updateComment(commentRequestDto, feedId, commentId, request);
    }

    @DeleteMapping("/{feeds_id}/comments/{comments_id}")
    public ResponseDto<?> deleteComment (@PathVariable(name = "feeds_id") Long feedId, @PathVariable(name = "comments_id") Long commentId, HttpServletRequest request) {
        return commentsService.deleteComment(feedId,commentId, request);
    }


}
