package com.hanghae.instakilogram.util;

import com.hanghae.instakilogram.entity.Feeds;
import com.hanghae.instakilogram.entity.Member;
import com.hanghae.instakilogram.repository.FeedsRepository;
import com.hanghae.instakilogram.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Verification {

    private final FeedsRepository feedsRepository;

    private final TokenProvider tokenProvider;

    public Member validateMember(HttpServletRequest httpServletRequest) {

        if(!tokenProvider.validateToken(httpServletRequest.getHeader("Authorization").substring(7))){
            return null;
        }

        return tokenProvider.getMemberFromAuthentication();
    }

    public Feeds getCurrentFeeds(Long feedsId) {

        Optional<Feeds> feeds = feedsRepository.findById(feedsId);

        return feeds.orElse(null);
    }

    public void checkFeedsMember(Feeds feeds, Member member) {

        if(!feeds.getMember().getMemberId().equals(member.getMemberId())){
            throw new RuntimeException("작성자만 수정 가능합니다.");
        }
    }
}
