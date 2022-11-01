package com.hanghae.instakilogram.util;

import com.hanghae.instakilogram.dto.response.ResponseDto;
import com.hanghae.instakilogram.entity.Feeds;
import com.hanghae.instakilogram.entity.Member;
import com.hanghae.instakilogram.repository.FeedsRepository;
import com.hanghae.instakilogram.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Util {

    private final FeedsRepository feedsRepository;
    private final MemberRepository memberRepository;

    public Member getMember(String memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));
        return member;
    }


    public Feeds getCurrentFeeds(Long feedsId) {

        Feeds feeds = feedsRepository.findById(feedsId).orElseThrow(() -> new IllegalStateException("존재하지 않는 게시글입니다."));
        return feeds;
    }

    public boolean checkFeedsMember(Feeds feeds, Member member) {

        if(feeds.getMember().getMemberId().equals(member.getMemberId())){
            return false;
        }
        return true;
    }

}
