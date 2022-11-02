package com.hanghae.instakilogram.service;

import com.hanghae.instakilogram.dto.request.FeedsRequestDto;
import com.hanghae.instakilogram.dto.request.FeedsUpdateDto;
import com.hanghae.instakilogram.dto.response.*;
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
    private final Util util;
    private final S3Uploader s3Uploader;

    @Transactional(readOnly = true)
    public Feeds getFeed(Long id) {
        Feeds feed = feedsRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("게시글이 존재하지 않습니다."));
        return feed;
    }

    @Transactional
    public ResponseDto<?> createFeeds(FeedsRequestDto feedsRequestDto, Member member) throws IOException {

        Feeds feeds = Feeds.builder()
                .member(member)
                .feedImage(s3Uploader.uploadFiles(feedsRequestDto.getImage(),"feeds", member, "feed"))
                .contents(feedsRequestDto.getContents())
                .build();

        feedsRepository.save(feeds);

        return ResponseDto.success(getResponseDto(member, feeds));
    }

    @Transactional
    public ResponseDto<?> getFeedsByMember(String memberId) {
        Member member = util.getMember(memberId);
        List<Feeds> feedsList = feedsRepository.findAllByMember(member);
        List<FeedResponseDto> feedResponseDtos = new ArrayList<>();
        getFeeds(member, feedsList, feedResponseDtos);
        return ResponseDto.success(getFeedDto(member, feedResponseDtos));
    }

    @Transactional
    public ResponseDto<?> getAllFeeds(Member member) {
        List<Follow> followList = followRepository.findAllByFromMember(member);
        List<FeedResponseDto> feedResponseDtos = new ArrayList<>();

        for (Follow following:followList){
            Member byMe = util.getMember(following.getToMember().getMemberId());
            List<Feeds> feedsList = feedsRepository.findAllByMember(byMe);

            getFeeds(member, feedsList, feedResponseDtos);
        }

        return ResponseDto.success(feedResponseDtos);
    }

    @Transactional
    public ResponseDto<?> updateFeeds(Long feedsId, FeedsUpdateDto feedsUpdateDto, Member member) {

        Feeds feeds = util.getCurrentFeeds(feedsId);
        if (util.checkFeedsMember(feeds, member)) {
            return ResponseDto.fail("작성자만 수정 가능합니다.");
        }

        Feeds newFeed = Feeds.builder()
                .id(feeds.getId())
                .member(member)
                .feedImage(feeds.getFeedImage())
                .contents(feedsUpdateDto.getContents())
                .build();

        feedsRepository.save(newFeed);

        return ResponseDto.success(getResponseDto(member, newFeed));
    }

    @Transactional
    public ResponseDto<?> deleteFeeds(Long feedsId, Member member) {

        Feeds feeds = util.getCurrentFeeds(feedsId);
        if (util.checkFeedsMember(feeds, member)) {
            return ResponseDto.fail("작성자만 수정 가능합니다.");
        }
        feedsRepository.delete(feeds);
        return ResponseDto.success("삭제가 완료되었습니다.");
    }

    @Transactional
    public ResponseDto<?> getDetailFeeds(Long feedsId, Member member) {

        Feeds feeds = util.getCurrentFeeds(feedsId);

        return ResponseDto.success(getResponseDto(member, feeds));
    }

    public FeedResponseDto getResponseDto(Member member, Feeds feeds) {
        Optional<Heart> heartOptional = heartRepository.findByMemberAndFeeds(member, feeds);
        boolean heartByMe = heartOptional.isPresent();
        FeedResponseDto feedResponseDto = FeedResponseDto.builder()
                .feedId(feeds.getId())
                .memberId(feeds.getMember().getMemberId())
                .memberImage(feeds.getMember().getMemberImage())
                .feedImage(feeds.getFeedImage())
                .contents(feeds.getContents())
                .commentsList(feeds.getCommentsList())
                .createdAt(feeds.getCreatedAt())
                .modifiedAt(feeds.getModifiedAt())
                .heartByMe(heartByMe)
                .heartList(feeds.getHeartList())
                .nickname(feeds.getMember().getNickname())
                .build();
        return feedResponseDto;
    }

    private List<FeedResponseDto> getFeeds(Member member, List<Feeds> feedsList, List<FeedResponseDto> feed) {
        for (int i = feedsList.size()-1; i >= 0; i--){
            Optional<Heart> heart = heartRepository.findByMemberAndFeeds(member, feedsList.get(i));
            boolean heartByMe = heart.isPresent();
            feed.add(FeedResponseDto.builder()
                    .feedId(feedsList.get(i).getId())
                    .memberId(feedsList.get(i).getMember().getMemberId())
                    .memberImage(feedsList.get(i).getMember().getMemberImage())
                    .feedImage(feedsList.get(i).getFeedImage())
                    .contents(feedsList.get(i).getContents())
                    .commentsList(feedsList.get(i).getCommentsList())
                    .createdAt(feedsList.get(i).getCreatedAt())
                    .modifiedAt(feedsList.get(i).getModifiedAt())
                    .heartByMe(heartByMe)
                    .heartList(feedsList.get(i).getHeartList())
                    .nickname(feedsList.get(i).getMember().getNickname())
                    .build());
        }
        return feed;
    }

    public FeedsListResponseDto getFeedDto(Member member, List<FeedResponseDto> feed) {
        FeedsListResponseDto feedsListResponseDto = FeedsListResponseDto.builder()
                .memberId(member.getMemberId())
                .username(member.getUsername())
                .memberImage(member.getMemberImage())
                .introduce(member.getIntroduce())
                .nickname(member.getNickname())
                .feedsList(feed)
                .followList(member.getFollowList())
                .followerList(member.getFollowerList())
                .build();
        return feedsListResponseDto;
    }
}
