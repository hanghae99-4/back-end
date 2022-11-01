package com.hanghae.instakilogram.service;

import com.hanghae.instakilogram.dto.request.FeedsRequestDto;
import com.hanghae.instakilogram.dto.response.CommentsResponseDto;
import com.hanghae.instakilogram.dto.response.FeedsResponseDto;
import com.hanghae.instakilogram.dto.response.HeartResponseDto;
import com.hanghae.instakilogram.dto.response.ResponseDto;
import com.hanghae.instakilogram.entity.*;
import com.hanghae.instakilogram.repository.*;
import com.hanghae.instakilogram.util.S3Uploader;
import com.hanghae.instakilogram.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedsService {

    private final FeedsRepository feedsRepository;
    private final FollowRepository followRepository;

    private final HeartRepository heartRepository;
    private final CommentsRepository commentsRepository;
    private final Util util;
    private final S3Uploader s3Uploader;

    @Transactional(readOnly = true)
    public Feeds getFeed(Long id) {
        Feeds feed = feedsRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("게시글이 존재하지 않습니다."));
        return feed;
    }

    public ResponseDto<?> createFeeds(FeedsRequestDto feedsRequestDto, Member member) throws IOException {

        Feeds feeds = Feeds.builder()
                .member(member)
                .imageUrl(s3Uploader.uploadFiles(feedsRequestDto.getImage(),"feeds", member, "feed"))
                .contents(feedsRequestDto.getContents())
                .build();

        feedsRepository.save(feeds);

        return ResponseDto.success("게시물 생성 성공");
    }

    public ResponseDto<?> getFeedsByMember(String memberId) {
        Member member = util.getMember(memberId);
        List<Feeds> feedsList = feedsRepository.findAllByMember(member);
        List<FeedsResponseDto> feedsResponseDtoList = new ArrayList<>();
        return ResponseDto.success(getFeeds(member, feedsList, feedsResponseDtoList));
    }



    public ResponseDto<?> getAllFeeds(Member member) {

        List<Follow> followList = followRepository.findAllByFromMember(member);
        List<FeedsResponseDto> feedsResponseDtoList = new ArrayList<>();

        for (Follow following:followList){
            Member byMe = util.getMember(following.getToMember().getMemberId());
            List<Feeds> feedsList = feedsRepository.findAllByMember(byMe);

            getFeeds(member, feedsList, feedsResponseDtoList);
        }

        return ResponseDto.success(feedsResponseDtoList);
    }

    public ResponseDto<?> updateFeeds(Long feedsId, FeedsRequestDto feedsRequestDto, Member member) throws IOException {

        Feeds feeds = util.getCurrentFeeds(feedsId);
        if (util.checkFeedsMember(feeds, member)) {
            return ResponseDto.fail("작성자만 수정 가능합니다.");
        }

        Feeds newFeed = Feeds.builder()
                .id(feeds.getId())
                .imageUrl(s3Uploader.uploadFiles(feedsRequestDto.getImage(),"feeds", member, "feed"))
                .contents(feedsRequestDto.getContents())
                .build();

        feedsRepository.save(newFeed);

        return ResponseDto.success("게시물 수정에 성공했습니다.");
    }

    public ResponseDto<?> deleteFeeds(Long feedsId, Member member) {

        Feeds feeds = util.getCurrentFeeds(feedsId);
        if (util.checkFeedsMember(feeds, member)) {
            return ResponseDto.fail("작성자만 수정 가능합니다.");
        }
        feedsRepository.delete(feeds);
        return ResponseDto.success("삭제가 완료되었습니다.");
    }

    public ResponseDto<?> getDetailFeeds(Long feedsId, Member member) {

        Feeds feeds = util.getCurrentFeeds(feedsId);

        List<Comments> commentsList = commentsRepository.findAllByFeeds(feeds);
        List<CommentsResponseDto> commentsResponseDtoList = new ArrayList<>();

        for (Comments comments : commentsList) {
            commentsResponseDtoList.add(CommentsResponseDto.builder()
                    .id(comments.getId())
                    .contents(comments.getContents())
                    .memberImage(comments.getMember().getMemberImage())
                    .memberId(comments.getMember().getMemberId())
                    .feedId(comments.getFeeds().getId())
                    .build());
        }

        List<Heart> heartList = heartRepository.findByFeeds(feeds);
        List<HeartResponseDto> heartResponseDtoList = new ArrayList<>();

        for (Heart heart : heartList) {
            heartResponseDtoList.add(HeartResponseDto.builder()
                    .ninkname(heart.getMember().getNickname())
                    .memberImage(heart.getMember().getMemberImage())
                    .build());
        }

        Optional<Heart> heartOptional = heartRepository.findByMemberAndFeeds(member, feeds);
        boolean heartByMe = heartOptional.isPresent();

        return ResponseDto.success(FeedsResponseDto.builder()
                .memberId(feeds.getMember().getMemberId())
                .memberImage(feeds.getMember().getMemberImage())
                .nickname(feeds.getMember().getNickname())
                .feedId(feeds.getId())
                .feedImage(feeds.getImageUrl())
                .contents(feeds.getContents())
                .commentsList(commentsResponseDtoList)
                .heartByMe(heartByMe)
                .heartNum(heartList.size())
                .createdAt(feeds.getCreatedAt())
                .modifiedAt(feeds.getModifiedAt())
                .build());
    }

    private List<FeedsResponseDto> getFeeds(Member member, List<Feeds> feedsList, List<FeedsResponseDto> feedsResponseDtoList) {
        for (int i = feedsList.size()-1; i >= 0; i--){

            List<Comments> commentsList = commentsRepository.findAllByFeeds(feedsList.get(i));
            List<CommentsResponseDto> commentsResponseDtoList = new ArrayList<>();

            for (Comments comments : commentsList) {
                commentsResponseDtoList.add(CommentsResponseDto.builder()
                        .id(comments.getId())
                        .feedId(comments.getFeeds().getId())
                        .memberId(comments.getMember().getMemberId())
                        .memberImage(comments.getMember().getMemberImage())
                        .contents(feedsList.get(i).getContents())
                        .build());
            }

            List<Heart> heartList = heartRepository.findByFeeds(feedsList.get(i));
            List<HeartResponseDto> heartResponseDtoList = new ArrayList<>();

            for (Heart hearts : heartList) {
                heartResponseDtoList.add(HeartResponseDto.builder()
                        .memberImage(hearts.getMember().getMemberImage())
                        .ninkname(hearts.getMember().getNickname())
                        .build());
            }

            Optional<Heart> heart = heartRepository.findByMemberAndFeeds(member, feedsList.get(i));
            boolean heartByMe = heart.isPresent();
            feedsResponseDtoList.add(FeedsResponseDto.builder()
                    .feedId(feedsList.get(i).getId())
                    .memberId(feedsList.get(i).getMember().getMemberId())
                    .memberImage(feedsList.get(i).getMember().getMemberImage())
                    .feedImage(feedsList.get(i).getImageUrl())
                    .createdAt(feedsList.get(i).getCreatedAt())
                    .heartByMe(heartByMe)
                    .heartNum(heartList.size())
                    .nickname(feedsList.get(i).getMember().getNickname())
                    .commentsList(commentsResponseDtoList)
                    .contents(feedsList.get(i).getContents())
                    .build());
        }
        return feedsResponseDtoList;
    }
}
