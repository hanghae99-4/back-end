package com.hanghae.instakilogram.service;

import com.hanghae.instakilogram.dto.request.CommentsRequestDto;
import com.hanghae.instakilogram.dto.response.CommentsResponseDto;
import com.hanghae.instakilogram.dto.response.ResponseDto;
import com.hanghae.instakilogram.entity.Comments;
import com.hanghae.instakilogram.entity.Feeds;
import com.hanghae.instakilogram.entity.Member;
import com.hanghae.instakilogram.repository.CommentsRepository;
import com.hanghae.instakilogram.security.UserDetailsImpl;
import com.hanghae.instakilogram.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final CommentsRepository commentsRepository;
    private final FeedsService feedsService;

    @Transactional
    public ResponseDto<?> createComment(CommentsRequestDto commentsRequestDto, Long feedsId, UserDetailsImpl userDetails) {

            Feeds feed = feedsService.getFeed(feedsId);
            Comments comment = Comments.builder()
                    .member(userDetails.getMember())
                    .feeds(feed)
                    .contents(commentsRequestDto.getContents())
                    .build();
            commentsRepository.save(comment);
            return ResponseDto.success("댓글을 등록하셨습니다.");
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getComments(Long feedId) {
        Feeds feed = feedsService.getFeed(feedId);

        List<Comments> commentList = commentsRepository.findAllByFeeds(feed);
        List<CommentsResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comments comment : commentList) {
            commentResponseDtoList.add(CommentsResponseDto.builder()
                    .id(comment.getId())
                    .contents(comment.getContents())
                    .feedId(comment.getFeeds().getId())
                    .memberId(comment.getMember().getMemberId())
                    .build());
        }
        return ResponseDto.success(commentResponseDtoList);
    }

    @Transactional
    public ResponseDto<?> deleteComment(Long commentId, UserDetailsImpl userDetails) {

        Comments comment = getComment(commentId);

        if (!comment.getMember().getMemberId().equals(userDetails.getMember().getMemberId())) {
            return ResponseDto.fail("댓글 작성자만 수정 가능합니다.");
        }

        commentsRepository.delete(comment);
        return ResponseDto.success("댓글을 삭제하셨습니다.");
    }

    @Transactional(readOnly = true)
    public Comments getComment(Long id) {
        Comments comments = commentsRepository.findById(id).orElseThrow(() -> new IllegalStateException("댓글이 존재하지 않습니다."));
        return comments;
    }
}
