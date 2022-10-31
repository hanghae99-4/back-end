package com.hanghae.instakilogram.service;

import com.hanghae.instakilogram.dto.request.FeedsRequestDto;
import com.hanghae.instakilogram.dto.response.CommentsResponseDto;
import com.hanghae.instakilogram.dto.response.FeedsResponseDto;
import com.hanghae.instakilogram.dto.response.HeartResponseDto;
import com.hanghae.instakilogram.dto.response.ResponseDto;
import com.hanghae.instakilogram.entity.Comments;
import com.hanghae.instakilogram.entity.Feeds;
import com.hanghae.instakilogram.entity.Heart;
import com.hanghae.instakilogram.entity.Member;
import com.hanghae.instakilogram.repository.CommentsRepository;
import com.hanghae.instakilogram.repository.FeedsRepository;
import com.hanghae.instakilogram.repository.HeartRepository;
import com.hanghae.instakilogram.security.UserDetailsImpl;
import com.hanghae.instakilogram.util.Verification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedsService {

    private final FeedsRepository feedsRepository;

    private final HeartRepository heartRepository;
    private final CommentsRepository commentsRepository;
    private final Verification verification;

    @Transactional(readOnly = true)
    public Feeds isPresentFeed(Long id) {
        Optional<Feeds> optionalFeed = feedsRepository.findById(id);
        return optionalFeed.orElse(null);
    }

    public ResponseDto<?> createFeeds(FeedsRequestDto feedsRequestDto, Member member) {


        Feeds feeds = Feeds.builder()
                .member(member)
                .contents(feedsRequestDto.getContent())
                .build();

        feedsRepository.save(feeds);

        return ResponseDto.success("게시물 생성 성공");
    }

    public ResponseDto<?> getAllFeeds(Member member) {

        List<Feeds> feedsList = feedsRepository.findAllByOrderByCreatedAtDesc();
        List<FeedsResponseDto> feedsResponseDtoList = new ArrayList<>();


        for (Feeds feeds : feedsList) {

            List<Comments> commentsList = commentsRepository.findAllByFeeds(feeds);
            List<CommentsResponseDto> commentsResponseDtoList = new ArrayList<>();

            for (Comments comments : commentsList) {
                commentsResponseDtoList.add(CommentsResponseDto.builder()
                        .id(comments.getId())
                        .feedId(comments.getFeeds().getId())
                        .memeberImage(comments.getFeeds().getMember().getMemberImage())
                        .nickname(comments.getMember().getNickname())
                        .createdAt(comments.getCreatedAt())
                        .memberId(comments.getMember().getMemberId())
                        .contents(feeds.getContents())
                        .build());
            }

            List<Heart> heartList = heartRepository.findByFeeds(feeds);
            List<HeartResponseDto> heartResponseDtoList = new ArrayList<>();

            for (Heart hearts : heartList) {
                heartResponseDtoList.add(HeartResponseDto.builder()
                        .FeedsId(hearts.getFeeds().getId())
                        .ninkname(hearts.getMember().getNickname())
                        .build());
            }

            Optional<Heart> heart = heartRepository.findByMemberAndFeeds(member, feeds);
            boolean heartByMe;
            heartByMe = heart.isPresent();
            feedsResponseDtoList.add(FeedsResponseDto.builder()
                    .feedId(feeds.getId())
                    .memberId(feeds.getMember().getMemberId())
                    .memberImage(feeds.getMember().getMemberImage())
                    .createdAt(feeds.getCreatedAt())
                    .heartByMe(heartByMe)
                    .heartNum(heartRepository.countByFeeds(feeds))
                    .nickname(feeds.getMember().getNickname())
                    .commentsList(commentsResponseDtoList)
                    .contents(feeds.getContents())
                    .build());
        }
        return ResponseDto.success(feedsResponseDtoList);
    }

    public ResponseDto<?> updateFeeds(Long feedsId, FeedsRequestDto feedsRequestDto, Member member) {

        Feeds feeds = verification.getCurrentFeeds(feedsId);
        verification.checkFeedsMember(feeds, member);

        feeds.update(feedsRequestDto);

        feedsRepository.save(feeds);

        return ResponseDto.success("게시물 수정에 성공했습니다.");
    }

    public ResponseDto<?> deleteFeeds(Long feedsId, Member member) {

        Feeds feeds = verification.getCurrentFeeds(feedsId);
        verification.checkFeedsMember(feeds, member);

        feedsRepository.delete(feeds);
        return ResponseDto.success("삭제가 완료되었습니다.");
    }

    public ResponseDto<?> getDetailFeeds(Long feedsId, Member member) {

        Feeds feeds = verification.getCurrentFeeds(feedsId);

        List<Comments> commentsList = commentsRepository.findAllByFeeds(feeds);
        List<CommentsResponseDto> commentsResponseDtoList = new ArrayList<>();

        for (Comments comments : commentsList) {
            commentsResponseDtoList.add(CommentsResponseDto.builder()
                    .id(comments.getId())
                    .contents(comments.getContents())
                    .memeberImage(comments.getFeeds().getMember().getMemberImage())
                    .memberId(comments.getMember().getMemberId())
                    .createdAt(comments.getCreatedAt())
                    .nickname(comments.getMember().getNickname())
                    .feedId(comments.getFeeds().getId())
                    .build());
        }

        List<Heart> heartList = heartRepository.findByFeeds(feeds);
        List<HeartResponseDto> heartResponseDtoList = new ArrayList<>();

        for (Heart heart : heartList) {
            heartResponseDtoList.add(HeartResponseDto.builder()
                    .ninkname(heart.getMember().getNickname())
                    .FeedsId(heart.getFeeds().getId())
                    .build());
        }

        Optional<Heart> heartOptional = heartRepository.findByMemberAndFeeds(member, feeds);
        boolean heartByMe;
        heartByMe = heartOptional.isPresent();

        return ResponseDto.success(FeedsResponseDto.builder()
                .memberId(feeds.getMember().getMemberId())
                .commentsList(commentsResponseDtoList)
                .heartByMe(heartByMe)
                .contents(feeds.getContents())
                .createdAt(feeds.getCreatedAt())
                .memberImage(feeds.getMember().getMemberImage())
                .feedId(feeds.getId())
                .heartNum(feeds.getHeartNum())
                .nickname(feeds.getMember().getNickname())
                .build());
    }
}
