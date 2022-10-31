package com.hanghae.instakilogram.service;

import com.hanghae.instakilogram.dto.request.CommentsRequestDto;
import com.hanghae.instakilogram.dto.response.CommentsResponseDto;
import com.hanghae.instakilogram.dto.response.ResponseDto;
import com.hanghae.instakilogram.entity.Comments;
import com.hanghae.instakilogram.entity.Feeds;
import com.hanghae.instakilogram.entity.Member;
import com.hanghae.instakilogram.repository.CommentsRepository;
import com.hanghae.instakilogram.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final CommentsRepository commentsRepository;
    private final FeedsService feedsService;
    private final TokenProvider tokenProvider;

    public ResponseDto<?> createComment(CommentsRequestDto commentsRequestDto, Long feedId, HttpServletRequest request) {


        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("로그인 후 사용가능합니다.");
        }

        Feeds feed = feedsService.isPresentFeed(commentsRequestDto.getFeedId());
        if (null == feed) {
            return ResponseDto.fail("피드가 존재하지 않습니다.");
        }


            Comments comment = Comments.builder()
                    .member(member)
                    .feeds(feed)
                    .contents(commentsRequestDto.getContents())
                    .build();
            commentsRepository.save(comment);

            return ResponseDto.success(
                    CommentsResponseDto.builder()
                            .id(comment.getId())
                            .nickname(member.getNickname())
                            .contents(comment.getContents())
                            .feedId(comment.getId())
                            .memberId()                             //  String memberId
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
    }




    public ResponseDto<?> getComments(Long feedId) {
        Feeds feed = feedsService.isPresentFeed(feedId);
        if (null == feed) {
            return ResponseDto.fail("피드가 존재하지 않습니다.");
        }

        List<Comments> commentList = commentsRepository.findAllByFeedId(feedId);
        List<CommentsResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comments comment : commentList) {
            commentResponseDtoList.add(CommentsResponseDto.builder()
                    .id(comment.getId())
                    .nickname(comment.getMember().getNickname())
                    .contents(comment.getContents())
                    .feedId(comment.getFeeds().getId())
                    .memberId()                                 //  String memberId
                    .createdAt(comment.getCreatedAt())
                    .modifiedAt(comment.getModifiedAt())
                    .build());
        }
        return ResponseDto.success(commentResponseDtoList);
    }

    public ResponseDto<?> updateComment(CommentsRequestDto commentsRequestDto, Long feedId, Long commentId, HttpServletRequest request) {
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("로그인 후 수정 가능합니다.");
        }

        Feeds feed = feedsService.isPresentFeed(commentsRequestDto.getFeedId());
        if (null == feed) {
            return ResponseDto.fail("피드가 존재하지 않습니다.");
        }

        Comments comment = isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail("댓글을 찾을 수 없습니다.");
        }

        if (comment.validateMember(member)) {
            return ResponseDto.fail("작성자만 수정 가능합니다.");
        }

        comment.update(commentsRequestDto);
        return ResponseDto.success(
                CommentsResponseDto.builder()
                        .id(comment.getId())
                        .nickname(member.getNickname())
                        .memberId()                            // String memberId
                        .contents(comment.getContents())
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .feedId(feed.getId())
                        .build()
        );
    }



    public ResponseDto<?> deleteComment(Long feedId, Long commentId, HttpServletRequest request) {

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("로그인 후 수정 가능합니다.");
        }


        Comments comment = isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail("댓글을 찾을 수 없습니다.");
        }

        if (comment.validateMember(member)) {
            return ResponseDto.fail("댓글 작성자만 수정 가능합니다.");
        }

        commentsRepository.delete(comment);
        return ResponseDto.success(true);
    }


    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    private Comments isPresentComment(Long id) {
        Optional<Comments> optionalComment = commentsRepository.findById(id);
        return optionalComment.orElse(null);
    }
}
