package com.hanghae.instakilogram.util;

import com.hanghae.instakilogram.entity.Feeds;
import com.hanghae.instakilogram.entity.Member;
import com.hanghae.instakilogram.repository.FeedsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Util {

    private final FeedsRepository feedsRepository;

    public Feeds getCurrentFeeds(Long feedsId) {

        Feeds feeds = feedsRepository.findById(feedsId).orElseThrow(() -> new IllegalStateException("존재하지 않는 게시글입니다."));
        return feeds;
    }

    public void checkFeedsMember(Feeds feeds, Member member) {

        if(!feeds.getMember().getMemberId().equals(member.getMemberId())){
            throw new IllegalStateException("작성자만 수정 가능합니다.");
        }
    }
}
